package com.example.logisticsestimate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.data.BoardData
import com.example.logisticsestimate.databinding.ItemPostBinding

class BoardRecyclerViewAdapter(private val posts : ArrayList<BoardData>)
    :RecyclerView.Adapter<BoardRecyclerViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemPostTvTitle = binding.itemPostTvTitle
        val itemPostTvNickname = binding.itemPostTvNickname
        val itemPostTvDate = binding.itemPostTvDate

        val itemPostIvMain = binding.itemPostIvMain
        val itemPostTvContent = binding.itemPostTvContent
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val curData = posts[position]

        //TODO(set onclicklistener)

        holder.itemPostTvTitle.text = curData.postTitle
        holder.itemPostTvNickname.text = curData.nickname
        holder.itemPostTvDate.text = curData.date

        // holder.imageViewPicture

        holder.itemPostTvContent.text = curData.postContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemPostBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}