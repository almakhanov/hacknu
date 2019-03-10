package kz.validol.hacknu.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fr: FragmentManager): FragmentPagerAdapter(fr){

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt(Values.POSITION,position)
        return OnBoardingFragment.newInstance(bundle)
    }

    override fun getCount(): Int = 3

}