package com.example.logisticsestimate.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.databinding.ItemTermBinding
import com.example.logisticsestimate.data.db.entity.TermEntity

class TermRecyclerViewAdapter (
    private val entities: ArrayList<TermEntity>,
    val context: Context
    ): RecyclerView.Adapter<TermRecyclerViewAdapter.TermViewHolder>() {
    inner class TermViewHolder(val binding: ItemTermBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val binding: ItemTermBinding = ItemTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val curData = entities[position]

        holder.binding.itemTermTitle.text = curData.title
        holder.binding.itemTermContent.text = curData.content
    }

    override fun getItemCount() = entities.size
}