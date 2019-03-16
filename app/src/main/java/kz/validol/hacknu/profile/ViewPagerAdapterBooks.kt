package kz.validol.hacknu.profile

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import kz.validol.hacknu.profile.finished.FinishedFragment
import kz.validol.hacknu.profile.my_books.MyBooksFragment
import kz.validol.hacknu.profile.my_reading.MyReadingFragment

class ViewPagerAdapterBooks(fm: FragmentManager): FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return MyBooksFragment()
            }
            1 -> {
                return  MyReadingFragment()
            }
            else ->{
                return FinishedFragment()
            }
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {
                return "My Books"
            }
            1 -> {
                return "My reading books"
            }
            else -> {
                return "Finished Books"
            }
        }
    }

}