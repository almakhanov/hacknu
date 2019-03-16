package kz.validol.hacknu.auth

//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
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
import com.google.zxing.integration.android.IntentIntegrator
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.MenuActivity
import kz.validol.hacknu.auth.RegisterActivity.Companion.FACEBOOK
import kz.validol.hacknu.auth.RegisterActivity.Companion.GIT
import kz.validol.hacknu.auth.RegisterActivity.Companion.GOOGLE
import kz.validol.hacknu.auth.RegisterActivity.Companion.VK
import okhttp3.*
import org.koin.android.ext.android.inject
import java.io.IOException
import java.math.BigInteger
import java.security.SecureRandom
import java.util.*


class LoginActivity : AppCompatActivity() {

    companion object {
        const val IS_LOGINED = "IS_LOGINED"
        const val USERNAME = "USERNAME"
        const val PASSW = "PASSW"
        const val FCMTOKEN = "FCMTOKEN"
    }

    private val api: Api by inject()
    private val random = SecureRandom()
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val sharedPref: SharedPreferences by inject()
    private val faceBookCallbackManager = CallbackManager.Factory.create()
    private var mGoogleSignInClient:GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(kz.validol.hacknu.R.layout.activity_login)
        edit_text_sign_in_password.setTintColor(Color.parseColor("#c0c5ce"))
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        loginViaFacebook()
        loginViaGithub()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignIn.setOnClickListener {
            signInGoogle()
        }
        sign_in_button.setOnClickListener {

        }
        signInVK.setOnClickListener {
            VKSdk.login(this, "ds","ds")
        }
        btnSignUp.setOnClickListener {
            login(mPhone.unmaskedText, edit_text_sign_in_password.text.toString())
        }

        signInTextRight.setOnClickListener{
            val loginIntent = Intent(this, RegisterActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun putLogined(){
        val editor = sharedPref.edit()
        editor.putBoolean(IS_LOGINED, true)
        editor.putString(USERNAME, App.user?.phone)
        editor.putString(PASSW, App.user?.password)
        editor.putString(FCMTOKEN, App.user?.token)
        editor.apply()
    }

    @SuppressLint("CheckResult")
    private fun login(username: String, password: String){
//        startActivity(Intent(this, MenuActivity::class.java))
//        finish()
        api.login(username, password, App.fcmDeviceId, username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    App.user = it.user
                    putLogined()
                    startActivity(Intent(this, MenuActivity::class.java))
                    finish()
                }
            }, {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onStart() {
        val account = GoogleSignIn.getLastSignedInAccount(this)

        super.onStart()
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
                    login(obj.getString("id"), FACEBOOK)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show()
            }
        }
        if (!VKSdk.onActivityResult(requestCode,resultCode,data,object: VKCallback<VKAccessToken>{
                override fun onResult(res: VKAccessToken?) {
                    val request = VKApi.users().get(VKParameters.from(res?.userId))
                    request.executeWithListener(object: VKRequest.VKRequestListener(){
                        override fun onComplete(response: VKResponse?) {
                            Log.d("response_vk",response?.responseString)
                            val vkUserObj: VKObject = Gson().fromJson(
                                response?.responseString, VKObject::class.java)

                            login(vkUserObj.response[0].id.toString(),VK)
                            super.onComplete(response)
                        }
                    })
                }

                override fun onError(error: VKError?) {

                }

            }))
        faceBookCallbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                Log.d("result_google",account.displayName)
                App.profilePhotoUri = account.photoUrl
                login(account.email!!, GOOGLE)
            }
            // Signed in successfully, show authenticated UI.
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(FragmentActivity.TAG, "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }

    }

    private fun loginViaGithub() {
        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if(user != null){
                RegisterActivity.signedGIthub = true
                Log.d("accepted", user.email)
                App.profilePhotoUri = user.photoUrl
                login(user.email!!, GIT)
                FirebaseAuth.getInstance().signOut()
            }
        }

        //Called after the github server redirect us to REDIRECT_URL_CALLBACK
        val uri = intent.data
        if (uri != null && uri.toString().startsWith(RegisterActivity.REDIRECT_URL_CALLBACK)) {
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            if (code != null && state != null)
                sendPost(code, state)
        }

        github.setOnClickListener{
            signInOut()
        }
    }
    fun signInOut() {
        if (!RegisterActivity.signedGIthub) {
            //https://developer.github.com/apps/building-integrations/setting-up-and-registering-oauth-apps/about-authorization-options-for-oauth-apps/
            //GET http://github.com/login/oauth/authorize
            val httpUrl = HttpUrl.Builder()
                .scheme("http")
                .host("github.com")
                .addPathSegment("login")
                .addPathSegment("oauth")
                .addPathSegment("authorize")
                .addQueryParameter("client_id", "2d22cb3f15cda9027eb1")
                .addQueryParameter("redirect_uri", RegisterActivity.REDIRECT_URL_CALLBACK)
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
            .add("redirect_uri", RegisterActivity.REDIRECT_URL_CALLBACK)
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
                    Toast.makeText(this@LoginActivity, "splitted[0] =>" + splitted[0], Toast.LENGTH_SHORT).show()            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@LoginActivity, "onFailure: " + e.toString(), Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}

const val RC_SIGN_IN = 1001
