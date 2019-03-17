package kz.validol.hacknu.genre_list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_genre_list.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.R
import kz.validol.hacknu.book.BookActivity
import kz.validol.hacknu.entities.Book
import kz.validol.hacknu.home.AllBooksAdapter
import org.koin.android.ext.android.inject

class GenreListActivity : AppCompatActivity(), AllBooksAdapter.OnItemClickListener {

    val api: Api by inject()
    lateinit var booksAdapter: AllBooksAdapter

    companion object {
        val KEY_GENRE = "KEY_GENRE"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_list)

        booksAdapter = AllBooksAdapter(this, this)
        listGenres.layoutManager = GridLayoutManager(this, 2)
        listGenres.adapter = booksAdapter

        intent.getStringExtra(KEY_GENRE)?.let {
            titleGenre.text = "Genre: $it"
            api.getGenreBooks(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    if (items.code == 0) {
                        if (items.books?.size!! > 0) {
                            booksAdapter.set(items.books as ArrayList<Book>)
                            booksAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(this, "Genre list is empty!", Toast.LENGTH_LONG).show()
                        }
                    }
                }, { error ->
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                })
        }

    }

    override fun onBookItemClicked(item: Book) {
        val intt = Intent(this, BookActivity::class.java)
        intt.putExtra(BookActivity.BOOK_ISNB, item.isbn)
        startActivity(intt)
    }


}
