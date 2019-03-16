package kz.validol.hacknu

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.validol.hacknu.auth.LoginActivity
import kz.validol.hacknu.onboarding.OnboardingActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long = 1000
    private val sharedPref: SharedPreferences by inject()
    private val api: Api by inject()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.token?.let {token->
                        App.fcmDeviceId = token
                        if(sharedPref.getString(Constants.FCM_TOKEN, "") == ""){
                            sharedPref.edit().putString(Constants.FCM_TOKEN, token).apply()
                        }
                        Log.d("token accepted", token)
                    }
                }
            }

        FirebaseMessaging.getInstance().subscribeToTopic("task").addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("subscription", "SUCCESS")
            }
        }
        App.fcmDeviceId = sharedPref.getString(Constants.FCM_TOKEN, "")
        if(sharedPref.getBoolean(LoginActivity.IS_LOGINED, false)){
            val username = sharedPref.getString(LoginActivity.USERNAME, "")
            val password = sharedPref.getString(LoginActivity.PASSW, "")
            App.fcmDeviceId = sharedPref.getString(LoginActivity.FCMTOKEN, "")
            api.login(username, password, App.fcmDeviceId, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == 0) {
                        App.user = it.user
                        startActivity(Intent(this, MenuActivity::class.java))
                        finish()
                    }
                }, {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                })
        }else{
            Handler().postDelayed({
                run {
                    val loginIntent = Intent(this, OnboardingActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                }
            }, SPLASH_TIME_OUT)
        }
    }
}
