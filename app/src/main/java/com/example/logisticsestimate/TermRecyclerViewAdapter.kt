package com.example.logisticsestimate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.databinding.ItemTermBinding
import com.example.logisticsestimate.db.TermEntity

class TermRecyclerViewAdapter (private val entities: ArrayList<TermEntity>, val context: Context): RecyclerView.Adapter<TermRecyclerViewAdapter.TermViewHolder>() {

    inner class TermViewHolder(binding: ItemTermBinding) : RecyclerView.ViewHolder(binding.root) {
        val termName = binding.termName
        val termContent = binding.termContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val binding: ItemTermBinding = ItemTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TermViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val curData = entities!![position]

        holder.termName.text = curData.name
        holder.termContent.text = curData.content
    }

    override fun getItemCount() = entities.size
}