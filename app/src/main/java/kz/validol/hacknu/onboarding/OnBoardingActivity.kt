package kz.validol.hacknu.onboarding

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_onboarding.*
import kz.validol.hacknu.R
import kz.validol.hacknu.auth.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager!!)
        dotsIndicator.setViewPager(viewPager)

        start.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}