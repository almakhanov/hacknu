package kz.validol.hacknu.auth

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_login.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import org.koin.android.ext.android.inject
import java.util.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.R.attr.data
import android.content.Context
import com.google.android.gms.common.api.ApiException
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.zxing.integration.android.IntentIntegrator
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*


class LoginActivity : AppCompatActivity() {

    private val api: Api by inject()
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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


//        api.register(User(-1,"ezhan9800@gmail.com", "Yerzhan", "1234", 21, "Helllo2", App.fcmDeviceId))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()

        googleSignIn.setOnClickListener {
            signInGoogle()
        }
        sign_in_button.setOnClickListener {

        }
        signInVK.setOnClickListener {
            VKSdk.login(this, "ds","ds")
        }
        btnSignUp.setOnClickListener {
            IntentIntegrator(this).apply {
                setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)

                setPrompt("Scan a barcode")
                setCameraId(0)  // Use a specific camera of the device
                setBeepEnabled(false)
                setBarcodeImageEnabled(true)
                initiateScan()
            }
        }
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
                    obj.getString("id")?.let {
                        Log.v("accepted", it)
                    }
                    obj.getString("birthday")?.let {
                        Log.v("accepted", it)
                    }
                    obj.getString("first_name")?.let {
                        Log.v("accepted", it)
                    }
                    if (obj.has("gender")) {
                        obj.getString("gender")?.let {
                            Log.v("accepted", it)
                        }
                    }
                    obj.getString("last_name")?.let {
                        Log.v("accepted", it)
                    }
                    if (obj.has("link")) {
                        obj.getString("link")?.let {
                            Log.v("accepted", it)
                        }
                    }
                    if (obj.has("location")) {
                        obj.getString("location")?.let {
                            Log.v("accepted", it)
                        }
                    }
                    if (obj.has("locale")) {
                        obj.getString("locale")?.let {
                            Log.v("accepted", it)
                        }
                    }
                    obj.getString("name")?.let {
                        Log.v("accepted", it)
                    }
                    if (obj.has("email")) {
                        obj.getString("email")?.let {
                            Log.v("accepted", it)
                        }
                    }
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

}

const val RC_SIGN_IN = 1001
