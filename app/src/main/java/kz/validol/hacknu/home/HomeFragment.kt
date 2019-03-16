package kz.validol.hacknu.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import kz.validol.hacknu.R

class HomeFragment : Fragment(), GenresAdapter.OnItemClickListener {

    var genreList = ArrayList<GenreItem>()

    override fun onItemClicked(item: GenreItem) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        genreList.apply {
            add(GenreItem("Romance", R.drawable.romance))
            add(GenreItem("Science", R.drawable.science))
            add(GenreItem("Fiction", R.drawable.romance))
            add(GenreItem("Drama", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
            add(GenreItem("romance", R.drawable.romance))
        }
        return  inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genreRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        genreRecycler.adapter = GenresAdapter(genreList, this@HomeFragment)
    }

}
