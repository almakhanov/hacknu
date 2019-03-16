package kz.validol.hacknu.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.fragment_onboarding.*
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
                title.text = "Search book"
                desc.text = "Search and recognize which books are accepted in the library for you."

            }
            1 -> {
                lottie?.setAnimation(R.raw.scann)
                title.text = "Borrow book"
                desc.text = "Scan barcode of the book and hire it fast and easily."
            }
            else -> {
                lottie?.setAnimation(R.raw.cycle_animation)
                title.text = "Finally!"
                desc.text = "You can take and read book at home."
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