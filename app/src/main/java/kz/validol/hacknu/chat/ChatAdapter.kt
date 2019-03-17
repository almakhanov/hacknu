package kz.validol.hacknu.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.validol.hacknu.R

class ChatAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat,parent,false))
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    class ChatViewHolder(v: View):RecyclerView.ViewHolder(v)

}