package kz.validol.hacknu.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kz.validol.hacknu.Constants
import kz.validol.hacknu.R
import kz.validol.hacknu.local_storage.Customer
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity(), LoginContract.LoginView {
    override fun success(user: Customer) {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private val sharedPref: SharedPreferences by inject()
    override val presenter: LoginContract.LoginPresenter by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.attachView(this)

        if (sharedPref.getInt(Constants.DOWNLOADED, 0) == 0) {
            sharedPref.edit().putInt(Constants.DOWNLOADED, 1).apply()

        }
    }
}
