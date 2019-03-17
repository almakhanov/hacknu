package kz.validol.hacknu.profile.my_reading

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_book.view.*
import kotlinx.android.synthetic.main.item_reading.view.*
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.BookReading

class ReadingBookAdapter(val list: ArrayList<BookReading>, val listener: OnClickListener,val context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ReadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_reading,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_color_lens_black_24dp)
            .error(R.drawable.ic_color_lens_black_24dp)

        Glide.with(context)
            .load(list[position].photo)
            .apply(options)
            .into(viewHolder.itemView.bookImg)

        viewHolder.itemView.bookTitle.text = list[position].name
        viewHolder.itemView.bookGenre.text = list[position].genre[0].name

        viewHolder.itemView.finishBook.setOnClickListener {
            listener.itemClick(list[position])
        }

    }

    class ReadingViewHolder(v:View):RecyclerView.ViewHolder(v)

}

interface OnClickListener{
    fun itemClick(book: BookReading)
}