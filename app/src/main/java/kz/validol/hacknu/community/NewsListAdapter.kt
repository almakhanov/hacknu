package kz.validol.hacknu.community

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_news.view.*
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.CommunityResponse

class NewsListAdapter(val context: Context, val list: ArrayList<CommunityResponse>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(kz.validol.hacknu.R.drawable.ic_color_lens_black_24dp)
            .error(kz.validol.hacknu.R.drawable.ic_color_lens_black_24dp)

        Glide.with(context)
            .load(list[position].author.photo)
            .apply(options)
            .into(viewHolder.itemView.avatar)

        viewHolder.itemView.name.text = list[position].author.name
        viewHolder.itemView.bookName.text = list[position].book.name

        Glide.with(context)
            .load(list[position].book.photo)
            .apply(options)
            .into(viewHolder.itemView.boomImg)

    }

    class NewsViewHolder(v: View):RecyclerView.ViewHolder(v)
}