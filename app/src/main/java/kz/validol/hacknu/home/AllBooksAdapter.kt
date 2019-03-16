package kz.validol.hacknu.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_book.view.*
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.Book


class AllBooksAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AllBooksAdapter.MyViewHolder>() {

    private var dataset: ArrayList<Book> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(kz.validol.hacknu.R.layout.item_book, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    fun set(allBooks: ArrayList<Book>) {
        dataset = allBooks
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(item: Book?) {

            Log.d("accepted", item?.author)
            Log.d("accepted", item?.photo)

            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_color_lens_black_24dp)
                .error(R.drawable.ic_color_lens_black_24dp)

            itemView.tvTitleBook.text = item?.name
            itemView.authorBook.text = item?.author
            itemView.rating.rating = item?.rating!!

            Glide.with(context)
                .load(item?.photo)
                .apply(options)
                .into(itemView.ivBook)

            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onBookItemClicked(dataset[adapterPosition])
        }
    }

    interface OnItemClickListener {
        fun onBookItemClicked(item: Book)
    }
}