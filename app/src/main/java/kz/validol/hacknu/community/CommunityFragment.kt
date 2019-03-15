package kz.validol.hacknu.community


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_community.*

import kz.validol.hacknu.R

class CommunityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setData(){
        listNews.adapter = NewsListAdapter()
        listNews.layoutManager = LinearLayoutManager(context)
    }

}
