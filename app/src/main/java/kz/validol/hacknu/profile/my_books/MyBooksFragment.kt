package kz.validol.hacknu.profile.my_books

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my_books.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.MenuActivity
import kz.validol.hacknu.R
import kz.validol.hacknu.Singleton
import kz.validol.hacknu.community.NewsListAdapter
import kz.validol.hacknu.entities.Book
import kz.validol.hacknu.home.AllBooksAdapter
import org.koin.android.ext.android.inject
import org.koin.standalone.KoinComponent

class MyBooksFragment: Fragment(),KoinComponent, AllBooksAdapter.OnItemClickListener {

    override fun onBookItemClicked(item: Book) {

    }

    var allBooksAdapter: AllBooksAdapter? = null
    private val api: Api by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_books,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allBooksAdapter = AllBooksAdapter(activity as MenuActivity, this@MyBooksFragment)
        getFreeBooks()
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("CheckResult")
    private fun getFreeBooks() {
//        bookList.layoutManager = LinearLayoutManager(context)
//        bookList.adapter = NewsListAdapter()
        bookList.layoutManager = GridLayoutManager(activity!!, 2)
        bookList.adapter = allBooksAdapter
        api.getBooks().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Singleton.allBooks = it as ArrayList<Book>
                allBooksAdapter?.set(Singleton.allBooks)
                allBooksAdapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }

}