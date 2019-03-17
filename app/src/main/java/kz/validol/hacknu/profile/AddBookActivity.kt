package kz.validol.hacknu.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kz.validol.hacknu.R
import kz.validol.hacknu.scanner.ScannerFragment

class AddBookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        val fragment = ScannerFragment()
        val bundle = Bundle()
        bundle.putString(FROM, PROFILE)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }


}

const val FROM = "FROM"
const val PROFILE = "PROFILE"
const val MENU = "MENU"
