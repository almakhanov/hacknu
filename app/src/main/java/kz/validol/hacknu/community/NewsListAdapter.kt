package kz.validol.hacknu.community

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.validol.hacknu.R

class NewsListAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false))
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    class NewsViewHolder(v: View):RecyclerView.ViewHolder(v)
}