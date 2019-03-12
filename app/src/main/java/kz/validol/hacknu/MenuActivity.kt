package kz.validol.hacknu

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kz.validol.hacknu.chat.ChatFragment
import kz.validol.hacknu.community.CommunityFragment
import kz.validol.hacknu.home.HomeFragment
import kz.validol.hacknu.profile.ProfileFragment
import kz.validol.hacknu.scanner.ScannerFragment

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, HomeFragment())
            .commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment = HomeFragment()
        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = HomeFragment()
            R.id.navigation_community -> selectedFragment = CommunityFragment()
            R.id.navigation_scanner -> selectedFragment = ScannerFragment()
            R.id.navigation_chat -> selectedFragment = ChatFragment()
            R.id.navigation_profile -> selectedFragment = ProfileFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, selectedFragment)
            .commit()
        true
    }

}
