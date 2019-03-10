package kz.validol.hacknu.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import kz.validol.hacknu.R
import retrofit2.http.POST

class OnBoardingFragment: Fragment(){

    private var position: Int? = null
    private var lottie: LottieAnimationView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        position = arguments?.getInt(Values.POSITION)
        val view = inflater.inflate(R.layout.fragment_onboarding,container,false)
        lottie = view.findViewById(R.id.lottie)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindView(){
        when(position){
            0 -> {
                lottie?.setAnimation(R.raw.animation_second)
            }
            1 -> {
                lottie?.setAnimation(R.raw.cycle_animation)
            }
            else -> {
                lottie?.setAnimation(R.raw.okay)
            }
        }
    }

    companion object {
        fun newInstance(data: Bundle?): OnBoardingFragment{
            val fragment = OnBoardingFragment()
            fragment.arguments = data
            return fragment
        }
    }
}

object Values{
    const val POSITION = "POSITION"
}
