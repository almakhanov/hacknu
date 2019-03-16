package kz.validol.hacknu.profile.my_reading

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_my_reading.*
import kz.validol.hacknu.R

class MyReadingFragment(): Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_reading,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bookList.adapter = ReadingBookAdapter()
        bookList.layoutManager = LinearLayoutManager(context)
        super.onViewCreated(view, savedInstanceState)
    }
}