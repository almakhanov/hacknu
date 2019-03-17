package kz.validol.hacknu.book

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_reader.view.*
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.User

class ListReaderAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ListReaderAdapter.MyViewHolder>() {

    private var dataset: ArrayList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reader, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    fun set(allBooks: ArrayList<User>) {
        dataset = allBooks
    }

    fun addUser(comment: User?) {
        comment?.let {
            dataset.add(0, it)
            this.notifyItemInserted(0)
        }
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: User) {

            itemView.fullName.text = item.name
            itemView.position.text = item.position

            val options = RequestOptions()
                .centerCrop()
                .placeholder(kz.validol.hacknu.R.drawable.ic_person_black_24dp)
                .error(kz.validol.hacknu.R.drawable.ic_person_black_24dp)

            Glide.with(context)
                .load(item.photo)
                .apply(options)
                .into(itemView.avatar)

            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onUserReaderItemClicked(dataset[adapterPosition])
        }
    }

    interface OnItemClickListener {
        fun onUserReaderItemClicked(item: User)
    }
}