package kz.validol.hacknu.auth

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.R
import org.koin.android.ext.android.inject

class RegisterActivity : AppCompatActivity() {

    private val api: Api by inject()
    private val sharedPref: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edit_text_sign_in_password.setTintColor(Color.parseColor("#c0c5ce"))
    }
}
