package kz.validol.hacknu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kz.validol.hacknu.chat.ChatFragment
import kz.validol.hacknu.community.CommunityFragment
import kz.validol.hacknu.home.HomeFragment
import kz.validol.hacknu.profile.FROM
import kz.validol.hacknu.profile.MENU
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
            R.id.navigation_scanner -> {
                if (ContextCompat.checkSelfPermission(applicationContext!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    val bunlde = Bundle()
                    selectedFragment = ScannerFragment()
                    bunlde.putString(FROM, MENU)
                    selectedFragment.arguments = bunlde

                }
                else {
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_CODE)
                }
            }
            R.id.navigation_chat -> selectedFragment = ChatFragment()
            R.id.navigation_profile -> selectedFragment = ProfileFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, selectedFragment)
            .commit()
        true
    }

}

const val CAMERA_CODE = 12
