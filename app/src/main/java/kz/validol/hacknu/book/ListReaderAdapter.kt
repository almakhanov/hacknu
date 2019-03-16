package kz.validol.hacknu.book

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.validol.hacknu.R

class ListReaderAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ReaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_reader,parent,false))
    }

    override fun getItemCount(): Int = 7

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    class ReaderViewHolder(v:View):RecyclerView.ViewHolder(v)

}