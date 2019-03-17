package kz.validol.hacknu.profile.my_reading

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my_reading.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.App
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.BookReading
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MyReadingFragment(): Fragment(),KoinComponent, OnClickListener {

    override fun itemClick(book: BookReading) {

    }

    val api: Api by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_reading,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getMyReading()
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("CheckResult")
    fun getMyReading(){
        api.myReadingBooks(App.user?.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                bookList.adapter = ReadingBookAdapter(it.book as ArrayList<BookReading>,this,context!!)
                bookList.layoutManager = LinearLayoutManager(context)
            },{

            })
    }
}