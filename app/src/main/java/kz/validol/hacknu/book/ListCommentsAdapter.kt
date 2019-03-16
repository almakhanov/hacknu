package kz.validol.hacknu.book

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_comment.view.*
import kz.validol.hacknu.entities.BookComment
import java.text.SimpleDateFormat


class ListCommentsAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ListCommentsAdapter.MyViewHolder>() {

    private var dataset: ArrayList<BookComment> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(kz.validol.hacknu.R.layout.item_comment, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    fun set(allBooks: ArrayList<BookComment>) {
        dataset = allBooks
    }

    fun addComment(comment: BookComment?) {
        comment?.let {
            dataset.add(0, it)
            this.notifyItemInserted(0)
        }
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: BookComment) {



            itemView.userName.text = item.author!!.name
            itemView.commentText.text = item.text

            val sdfSource = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            val date = sdfSource.parse(item.date?.substring(0,20))
            val sdfDestination = SimpleDateFormat("dd/MM/yyyy, hh:mm")
            itemView.date.text = sdfDestination.format(date)

            val options = RequestOptions()
                .centerCrop()
                .placeholder(kz.validol.hacknu.R.drawable.ic_person_black_24dp)
                .error(kz.validol.hacknu.R.drawable.ic_person_black_24dp)

            Glide.with(context)
                .load(item.author.photo)
                .apply(options)
                .into(itemView.avatar)

            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onBookCommentItemClicked(dataset[adapterPosition])
        }
    }

    interface OnItemClickListener {
        fun onBookCommentItemClicked(item: BookComment)
    }
}