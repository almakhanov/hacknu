package kz.validol.hacknu.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_home.*
import kz.validol.hacknu.R

class HomeFragment : Fragment(), GenresAdapter.OnItemClickListener {

    var genreList = arrayListOf<GenreItem>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val homeView = inflater.inflate(R.layout.fragment_home, container, false)
        return homeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genreList.apply {
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("science", R.drawable.science))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
        }

        genreRecycler.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
        genreRecycler.adapter = GenresAdapter(genreList, this)
    }


    override fun onItemClicked(item: GenreItem) {

    }


}
