package kz.validol.hacknu.genre_list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.validol.hacknu.Api
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.Book
import kz.validol.hacknu.home.AllBooksAdapter
import org.koin.android.ext.android.inject

class GenreListActivity : AppCompatActivity(), AllBooksAdapter.OnItemClickListener {

    val api: Api by inject()
    lateinit var booksAdapter: AllBooksAdapter

    companion object {
        val KEY_GENRE = "KEY_GENRE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_list)

        booksAdapter = AllBooksAdapter(this, this)
        intent.getStringExtra(KEY_GENRE)?.let {
            api.getGenreBooks(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items->
                    booksAdapter.set(items as ArrayList<Book>)
                },{ error->
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                })
        }

    }

    override fun onBookItemClicked(item: Book) {

    }


}
