package kz.validol.hacknu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("token accepted", it.result?.token)
                }
            }

        FirebaseMessaging.getInstance().subscribeToTopic("task").addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("subscription", "SUCCESS")
            }
        }
    }
}
