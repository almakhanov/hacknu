package kz.validol.hacknu.profile


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_book.view.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.R
import kz.validol.hacknu.auth.LoginActivity
import org.koin.android.ext.android.inject
import org.koin.standalone.KoinComponent

class ProfileFragment : Fragment(),KoinComponent {

    private val sharedPref: SharedPreferences by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = ViewPagerAdapterBooks(childFragmentManager)
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position==0){
                    fab.visibility = View.VISIBLE
                }else{
                    fab.visibility = View.GONE
                }
            }

        })

        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_color_lens_black_24dp)
            .error(R.drawable.ic_color_lens_black_24dp)

        Glide.with(context!!)
            .load(App.user?.photo)
            .apply(options)
            .into(avatar)

        avatarName.text = App.user?.name

        position.text = App.user?.position


        fab.setOnClickListener {
            val intent = Intent(context,AddBookActivity::class.java)
            startActivity(intent)
        }
        tabLayout.setupWithViewPager(viewPager)
//        signout.setOnClickListener{
//            logout()
//        }
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
