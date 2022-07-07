package com.example.logisticsestimate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.databinding.ItemTemporaryBinding
import com.example.logisticsestimate.db.TemporaryPostEntity
import com.example.logisticsestimate.fragment.CommunityFragment

class TemporaryRecyclerViewAdapter(private val entities : ArrayList<TemporaryPostEntity>, private val context: Context)
    :RecyclerView.Adapter<TemporaryRecyclerViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: ItemTemporaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val container = binding.itemTemporaryCl
        val itemTemporaryTvTitle = binding.itemTemporaryTvTitle
        val itemTemporaryTvContent = binding.itemTemporaryTvContent
        val itemTemporaryTvDate = binding.itemTemporaryTvDate
        val itemTemporaryTvCategory = binding.itemTemporaryTvCategory
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTemporaryBinding = ItemTemporaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }
}