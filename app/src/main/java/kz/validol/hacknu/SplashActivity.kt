package kz.validol.hacknu

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kz.validol.hacknu.auth.LoginActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long = 1000
    private val sharedPref: SharedPreferences by inject()

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

        Handler().postDelayed({
            run {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }, SPLASH_TIME_OUT)


    }
}
