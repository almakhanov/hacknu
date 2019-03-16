package kz.validol.hacknu.profile


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.validol.hacknu.R
import kz.validol.hacknu.auth.LoginActivity
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {

    private val sharedPref: SharedPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signout.setOnClickListener{
            logout()
        }
    }

    fun logout(){
        val editor = sharedPref.edit()
        editor.putBoolean(LoginActivity.IS_LOGINED, false)
        editor.putString(LoginActivity.USERNAME, "")
        editor.putString(LoginActivity.PASSW, "")
        editor.putString(LoginActivity.FCMTOKEN, "")
        editor.apply()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity!!.finish()
    }

}
