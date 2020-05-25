package kz.validol.hacknu.profile.my_reading

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.validol.hacknu.R

class ReadingBookAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ReadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_reading,parent,false))
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    class ReadingViewHolder(v:View):RecyclerView.ViewHolder(v)

}