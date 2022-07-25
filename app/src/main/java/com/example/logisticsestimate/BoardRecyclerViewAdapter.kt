package com.example.logisticsestimate

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.data.BoardData
import com.example.logisticsestimate.databinding.ItemLoadingBinding
import com.example.logisticsestimate.databinding.ItemBoardBinding

class BoardRecyclerViewAdapter(items : ArrayList<BoardData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    private val items = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBoardBinding.inflate(layoutInflater, parent, false)
                ItemViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder) {
            populateItemRows(holder, position)
        } else if(holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(items[position].category == -1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun populateItemRows(holder : ItemViewHolder, position: Int) {
        val item = items[position]
        holder.setText(item)
    }

    private fun showLoadingView(holder : LoadingViewHolder, position: Int) {

    }


    inner class ItemViewHolder(binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        var title = binding.itemPostTvTitle
        var content = binding.itemPostTvContent
        var date = binding.itemPostTvDate
        var nickname = binding.itemPostTvNickname

        fun setText(boardData: BoardData) {
            title.text = boardData.postTitle
            content.text = boardData.postContent
            date.text = boardData.date
            nickname.text = boardData.nickname
        }
    }

    inner class LoadingViewHolder(binding : ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var progressBar : ProgressBar = binding.progressBar
    }
}