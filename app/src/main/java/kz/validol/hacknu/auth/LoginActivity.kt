package kz.validol.hacknu.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.User
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val api: Api by inject()
    private val sharedPref: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        api.register(User(-1,"ezhan9800@gmail.com", "Yerzhan", "1234", 21, "Helllo2", App.fcmDeviceId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}
