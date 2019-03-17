package kz.validol.hacknu.community


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_community.*
import kz.validol.hacknu.Api

import kz.validol.hacknu.R
import kz.validol.hacknu.entities.CommunityResponse
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class CommunityFragment : Fragment(),KoinComponent {

    val api:Api by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getComunnity()
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("CheckResult")
    fun getComunnity(){
        api.getCommunity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listNews.adapter = NewsListAdapter(context!!,it as ArrayList<CommunityResponse>)
                listNews.layoutManager = LinearLayoutManager(context)
            },{

            })
    }

    private fun setData(){

    }

}
