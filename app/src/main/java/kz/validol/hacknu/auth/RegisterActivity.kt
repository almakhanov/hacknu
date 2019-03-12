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
import kotlinx.android.synthetic.main.activity_register.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import org.koin.android.ext.android.inject
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private val api: Api by inject()
    private val sharedPref: SharedPreferences by inject()
    private val faceBookCallbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(kz.validol.hacknu.R.layout.activity_register)

        edit_text_sign_in_password.setTintColor(Color.parseColor("#c0c5ce"))

        loginViaFacebook()

        loginViaTwitter()

//        api.register(User(-1,"ezhan9800@gmail.com", "Yerzhan", "1234", 21, "Helllo2", App.fcmDeviceId))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
    }

    private fun loginViaTwitter() {
        twitter.setOnClickListener{

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
        faceBookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
