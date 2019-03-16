package kz.validol.hacknu.home


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kz.validol.hacknu.Api
import kz.validol.hacknu.MenuActivity
import kz.validol.hacknu.R
import kz.validol.hacknu.Singleton
import kz.validol.hacknu.entities.Book
import kz.validol.hacknu.genre_list.GenreListActivity
import org.koin.android.ext.android.inject


class HomeFragment : Fragment(), GenresAdapter.OnItemClickListener, AllBooksAdapter.OnItemClickListener {

    var genreList = ArrayList<GenreItem>()
    private val api: Api by inject()
    var allBooksAdapter: AllBooksAdapter? = null
    var genresAdapter: GenresAdapter? = null
    var recommendedAdapter: AllBooksAdapter? = null


    override fun onGenreItemClicked(item: GenreItem) {
        val tmpIntent = Intent(activity, GenreListActivity::class.java)
        tmpIntent.putExtra(GenreListActivity.KEY_GENRE, item.value)
        startActivity(tmpIntent)
    }

    override fun onBookItemClicked(item: Book) {

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
        allBooksAdapter = AllBooksAdapter(activity as MenuActivity, this@HomeFragment)
        genresAdapter = GenresAdapter(genreList, this@HomeFragment)
        recommendedAdapter = AllBooksAdapter(activity as MenuActivity, this@HomeFragment)

        getGenres()
        getRecommendations()
        getFreeBooks()
    }

    @SuppressLint("CheckResult")
    private fun getFreeBooks() {
        freeBooksRecycler.layoutManager = GridLayoutManager(activity, 2)
        freeBooksRecycler.adapter = allBooksAdapter
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

    @SuppressLint("CheckResult")
    private fun getGenres() {
        genreRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        genreRecycler.adapter = genresAdapter
//        api.getBooks().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                Singleton.allBooks = it as ArrayList<Book>
//                allBooksAdapter.set(Singleton.allBooks)
//                allBooksAdapter.notifyDataSetChanged()
//            }, {
//                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
//            })
    }

    @SuppressLint("CheckResult")
    private fun getRecommendations() {
        recommendRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recommendRecycler.adapter = recommendedAdapter
        api.getBooks().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Singleton.recommendedBooks = it as ArrayList<Book>
                recommendedAdapter?.set(Singleton.recommendedBooks)
                recommendedAdapter?.notifyDataSetChanged()
            }, {
                Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
            })
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
