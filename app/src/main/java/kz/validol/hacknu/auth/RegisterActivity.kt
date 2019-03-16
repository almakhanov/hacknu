package kz.validol.hacknu.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.gson.Gson
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.MenuActivity
import kz.validol.hacknu.entities.User
import okhttp3.*
import org.koin.android.ext.android.inject
import java.io.IOException
import java.math.BigInteger
import java.security.SecureRandom
import java.util.*


class RegisterActivity : AppCompatActivity() {

    companion object {
        val REDIRECT_URL_CALLBACK = "epam://git.oauth2token"
        var signedGIthub = false
        val GOOGLE = "google"
        val GIT = "git"
        val FACEBOOK= "fb"
        val VK = "vk"
    }

    private val random = SecureRandom()
    private val api: Api by inject()
    private val sharedPref: SharedPreferences by inject()
    private val faceBookCallbackManager = CallbackManager.Factory.create()
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onStart() {
        super.onStart()
        mAuth?.addAuthStateListener(mAuthListener!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(kz.validol.hacknu.R.layout.activity_register)

        edit_text_sign_in_password.setTintColor(Color.parseColor("#c0c5ce"))
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        loginViaFacebook()
        loginViaGithub()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signInTextRight.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        google_sign_in.setOnClickListener {
            signInGoogle()
        }

        vk_sign_in.setOnClickListener {
            VKSdk.login(this, "ds", "ds")
        }

        btnSignUp.setOnClickListener{
            val user= User(
                name = mName.text.toString(),
                email = mPhone.unmaskedText,
                //mPosition
                password = edit_text_sign_in_password.text.toString()
            )
            signUp(user)
        }
    }

    @SuppressLint("CheckResult")
    private fun signUp(user: User) {
        user.FCMToken = App.fcmDeviceId
        api.register(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    App.user = user
                    startActivity(Intent(this, MenuActivity::class.java))
                    finish()
                }
            }, {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }


    private fun loginViaGithub() {
        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                signedGIthub = true
                Log.d("accepted", user.email)
                val gitUser = User(
                    name = user.displayName,
                    email = user.email,
                    password = GIT
                )
                App.profilePhotoUri = user.photoUrl
                signUp(gitUser)
                FirebaseAuth.getInstance().signOut()
            }
        }

        //Called after the github server redirect us to REDIRECT_URL_CALLBACK
        val uri = intent.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URL_CALLBACK)) {
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            if (code != null && state != null)
                sendPost(code, state)
        }

        github.setOnClickListener {
            signInOut()
        }
    }

    private fun loginViaFacebook() {
        facebookLogin.setOnClickListener {
            login_button.performClick()
        }
        login_button.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"))
        login_button.registerCallback(faceBookCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                App.facebookToken = loginResult.accessToken.token

                val request = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { obj, response ->
                    val facebookUser = User()
                    obj.getString("id")?.let {
                        Log.v("accepted", it)
                        facebookUser.email = it
                        //http://graph.facebook.com/67563683055/picture?type=square
                        facebookUser.photo = "http://graph.facebook.com/" + it + "/picture?type=square"
                    }
                    obj.getString("birthday")?.let {
                        val strs = it.split('/')
                        facebookUser.age = 2019 - strs[2].toInt()
                    }
                    obj.getString("name")?.let {
                        Log.v("accepted", it)
                        facebookUser.name = it
                    }
                    if (obj.has("email")) {
                        obj.getString("email")?.let {
                            Log.v("accepted", it)
                        }
                    }
                    facebookUser.password = FACEBOOK
                    signUp(facebookUser)
                    LoginManager.getInstance().logOut()
                }
                val parameters = Bundle()
                parameters.putString(
                    "fields",
                    "id,name,email,gender,birthday,first_name,last_name,link,location,locale"
                )
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        faceBookCallbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                override fun onResult(res: VKAccessToken?) {
//                    val request = VKApi.users().get(VKParameters.from(res?.userId))
//                    request.executeWithListener(object : VKRequest.VKRequestListener() {
//                        override fun onComplete(response: VKResponse?) {
//                            Log.d("response_vk", response?.responseString)
//                            val vkUserObj: VKObject = Gson().fromJson(
//                                response?.responseString, VKObject::class.java)
//
//                            val vkUser = User(
//                                email = vkUserObj.response[0].id.toString(),
//                                name = vkUserObj.response[0].first_name+ ' '+
//                                        vkUserObj.response[0].last_name,
//                                password = VK
//                            )
//                            signUp(vkUser)
//                            super.onComplete(response)
//                        }
//                    })
                    val requestphoto = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"photo_400"))
                    requestphoto.executeWithListener(object : VKRequest.VKRequestListener() {
                        override fun onComplete(response: VKResponse?) {
                            Log.d("response_vk_image", response?.responseString)
                            val vkUserObj: VKObject = Gson().fromJson(
                                response?.responseString, VKObject::class.java)

                            val vkUser = User(
                                email = vkUserObj.response[0].id.toString(),
                                name = vkUserObj.response[0].first_name+ ' '+
                                        vkUserObj.response[0].last_name,
                                password = VK
                            )
                            signUp(vkUser)
                            super.onComplete(response)
                        }
                    })
                }

                override fun onError(error: VKError?) {

                }

            }))
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                Log.d("result_google", account.displayName)
                val gUser = User(
                    name = account.displayName,
                    email = account.email,
                    password = GOOGLE
                )
                App.profilePhotoUri = account.photoUrl
                signUp(gUser)
            }
        } catch (e: ApiException) {
        }

    }

    fun signInOut() {
        if (!signedGIthub) {
            //https://developer.github.com/apps/building-integrations/setting-up-and-registering-oauth-apps/about-authorization-options-for-oauth-apps/
            //GET http://github.com/login/oauth/authorize
            val httpUrl = HttpUrl.Builder()
                .scheme("http")
                .host("github.com")
                .addPathSegment("login")
                .addPathSegment("oauth")
                .addPathSegment("authorize")
                .addQueryParameter("client_id", "2d22cb3f15cda9027eb1")
                .addQueryParameter("redirect_uri", REDIRECT_URL_CALLBACK)
                .addQueryParameter("state", getRandomString())
                .addQueryParameter("scope", "user:email")
                .build()

            Log.d("accepted", httpUrl.toString())

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl.toString()))
            startActivity(intent)

        } else {
            mAuth?.signOut()
        }
    }

    private fun getRandomString(): String {
        return BigInteger(130, random).toString(32)
    }

    private fun sendPost(code: String, state: String) {
        //POST https://github.com/login/oauth/access_token

        val okHttpClient = OkHttpClient()
        val form = FormBody.Builder()
            .add("client_id", "2d22cb3f15cda9027eb1")
            .add("client_secret", "aca85697d9f0d5b1b1342e349f8786548df5689c")
            .add("code", code)
            .add("redirect_uri", REDIRECT_URL_CALLBACK)
            .add("state", state)
            .build()

        val request = Request.Builder()
            .url("https://github.com/login/oauth/access_token")
            .post(form)
            .build()


        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
//access_token=e72e16c7e42f292c6912e7710c838347ae178b4a&token_type=bearer
                val responseBody = response.body()?.string()
                val splitted = responseBody?.split("=|&".toRegex())?.dropLastWhile({ it.isEmpty() })!!.toTypedArray()
                if (splitted[0].equals("access_token", ignoreCase = true))
                    signInWithToken(splitted[1])
                else
                    Toast.makeText(this@RegisterActivity, "splitted[0] =>" + splitted[0], Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@RegisterActivity, "onFailure: " + e.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun signInWithToken(token: String) {
        val credential = GithubAuthProvider.getCredential(token)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) {
                Log.d("accepted", "signInWithCredential:onComplete:" + it.isSuccessful)

                if (!it.isSuccessful) {
                    it.exception?.printStackTrace()
                    Log.w("accepted", "signInWithCredential", it.exception)
                    Toast.makeText(this@RegisterActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
