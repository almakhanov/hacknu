package kz.validol.hacknu.chat

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_chat.view.*
import kz.validol.hacknu.R
import kz.validol.hacknu.entities.ChatObject
import java.text.SimpleDateFormat

class ChatAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    private var dataset: ArrayList<ChatObject> = ArrayList()

    fun set(imtes: ArrayList<ChatObject>) {
        dataset = imtes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataset[position])
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: ChatObject) {

            val sdfSource = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            val date = sdfSource.parse(item.sended_time?.substring(0, 20))
            val sdfDestination = SimpleDateFormat("dd/MM/yyyy, hh:mm")
            itemView.sendDAte.text = sdfDestination.format(date)

            val options2 = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_color_lens_black_24dp)
                .error(R.drawable.ic_color_lens_black_24dp)
            Glide.with(context)
                .load(item.book?.photo)
                .apply(options2)
                .into(itemView.bookImg)


            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
            Glide.with(context)
                .load(item.author?.photo)
                .apply(options)
                .into(itemView.avatarUser)

            itemView.message.text = item.text

            when (item.status) {
                0 -> {
                    itemView.buttonLayout.visibility = View.VISIBLE
                    itemView.acceptedText.visibility = View.GONE
                    itemView.declinedText.visibility = View.GONE

                    itemView.declineBtn.setOnClickListener {
                        listener.onRequestDecline(item, adapterPosition)
                        dataset[adapterPosition].status = 2
                        notifyItemChanged(adapterPosition)
                    }
                    itemView.acceptBtn.setOnClickListener {
                        listener.onRequsetAccept(item, adapterPosition)
                        dataset[adapterPosition].status = 1
                        notifyItemChanged(adapterPosition)
                    }
                }
                1 -> {
                    itemView.buttonLayout.visibility = View.GONE
                    itemView.acceptedText.visibility = View.VISIBLE
                    itemView.declinedText.visibility = View.GONE
                }
                2 -> {
                    itemView.buttonLayout.visibility = View.GONE
                    itemView.acceptedText.visibility = View.GONE
                    itemView.declinedText.visibility = View.VISIBLE
                }
                3 -> {
                    itemView.buttonLayout.visibility = View.GONE
                    itemView.acceptedText.visibility = View.GONE
                    itemView.declinedText.visibility = View.GONE
                }
            }


        }
    }

    interface OnItemClickListener {
        fun onRequsetAccept(item: ChatObject, position: Int)
        fun onRequestDecline(item: ChatObject, position: Int)
    }
}