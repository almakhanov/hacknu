package kz.validol.hacknu.home


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kz.validol.hacknu.*
import kz.validol.hacknu.book.BookActivity
import kz.validol.hacknu.entities.Book
import kz.validol.hacknu.genre_list.GenreListActivity
import org.koin.android.ext.android.inject


class HomeFragment : Fragment(), GenresAdapter.OnItemClickListener, AllBooksAdapter.OnItemClickListener {

    var genreList = ArrayList<GenreItem>()
    private val api: Api by inject()
    var freeBooksAdapter: AllBooksAdapter? = null
    var genresAdapter: GenresAdapter? = null
    var recommendedAdapter: AllBooksAdapter? = null
    var allBooksAdapter: AllBooksAdapter? = null
    var disposables = CompositeDisposable()


    override fun onGenreItemClicked(item: GenreItem) {
        val tmpIntent = Intent(activity, GenreListActivity::class.java)
        tmpIntent.putExtra(GenreListActivity.KEY_GENRE, item.name)
        startActivity(tmpIntent)
    }

    override fun onBookItemClicked(item: Book) {
        val intt = Intent(activity, BookActivity::class.java)
        intt.putExtra(BookActivity.BOOK_ISNB, item.isbn)
        startActivity(intt)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        genreList.apply {
            add(GenreItem("Romance", R.drawable.rrr))
            add(GenreItem("Science", R.drawable.sss))
            add(GenreItem("Fiction", R.drawable.fff))
            add(GenreItem("Detective", R.drawable.detective))
            add(GenreItem("Drama", R.drawable.drama))
            add(GenreItem("Novel", R.drawable.novel))
            add(GenreItem("Mystery", R.drawable.mystery))
            add(GenreItem("Fantasy", R.drawable.fantasy))
            add(GenreItem("Thriller", R.drawable.thriller))
            add(GenreItem("History", R.drawable.history))
            add(GenreItem("Horror", R.drawable.horror))
        }
        return inflater.inflate(kz.validol.hacknu.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMetrics()
        freeBooksAdapter = AllBooksAdapter(activity as MenuActivity, this@HomeFragment)
        genresAdapter = GenresAdapter(genreList, this@HomeFragment)
        recommendedAdapter = AllBooksAdapter(activity as MenuActivity, this@HomeFragment)
        allBooksAdapter = AllBooksAdapter(activity as MenuActivity, this@HomeFragment)


        getGenres()
        getRecommendations()
        getFreeBooks()
        getAllBooks()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("CheckResult")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 0) {
                    freeBooksBox.visibility = View.GONE
                    recommendBox.visibility = View.GONE
                    genreBox.visibility = View.GONE

                    api.searchBooks(s.toString()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            allBooksAdapter?.set(it.books as ArrayList<Book>)
                            allBooksAdapter?.notifyDataSetChanged()
                        }, {
                            Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
                        })

                } else {
                    freeBooksBox.visibility = View.VISIBLE
                    recommendBox.visibility = View.VISIBLE
                    genreBox.visibility = View.VISIBLE

                    api.getBooks().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            allBooksAdapter?.set(it as ArrayList<Book>)
                            allBooksAdapter?.notifyDataSetChanged()
                        }, {
                            Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
                        })
                }
            }

        })
    }

    @SuppressLint("CheckResult")
    private fun getAllBooks() {
        allBooksRecycler.layoutManager = GridLayoutManager(activity, 2)
        allBooksRecycler.adapter = allBooksAdapter
        api.getBooks().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                allBooksAdapter?.set(it as ArrayList<Book>)
                allBooksAdapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    @SuppressLint("CheckResult")
    private fun getFreeBooks() {
        freeBooksRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        freeBooksRecycler.adapter = freeBooksAdapter
        api.getFreeBooks().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                freeBooksAdapter?.set(it.books as ArrayList<Book>)
                freeBooksAdapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
    }

    @SuppressLint("CheckResult")
    private fun getGenres() {
        genreRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        genreRecycler.adapter = genresAdapter
//        api.getBooks().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                Singleton.allBooks = it as ArrayList<Book>
//                freeBooksAdapter.set(Singleton.allBooks)
//                freeBooksAdapter.notifyDataSetChanged()
//            }, {
//                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
//            })
    }

    @SuppressLint("CheckResult")
    private fun getRecommendations() {

        recommendRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recommendRecycler.adapter = recommendedAdapter
        disposables.add(api.getRecommendations(App.user?.id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loading.visibility = View.VISIBLE
            }
            .doFinally {
                loading.visibility = View.GONE
            }
            .subscribe({
                Singleton.recommendedBooks = it.books as ArrayList<Book>
                recommendedAdapter?.set(Singleton.recommendedBooks)
                recommendedAdapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }))
    }


    private fun getMetrics() {
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        pHeight = displayMetrics.heightPixels
        pWidth = displayMetrics.widthPixels
    }

    companion object {
        var pHeight = 0
        var pWidth = 0
    }


}
