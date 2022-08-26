package com.example.logisticsestimate.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.view.activity.BoardViewActivity
import com.example.logisticsestimate.base.App.Companion.context
import com.example.logisticsestimate.data.remote.model.board.BoardData
import com.example.logisticsestimate.databinding.ItemLoadingBinding
import com.example.logisticsestimate.databinding.ItemBoardBinding

/**
 * 글 목록을 불러오는 중이라면 ProgressBar 표시, 그 외에는 글 목록 표시
 */
class BoardRecyclerViewAdapter(
    private val items : ArrayList<BoardData>,
    private val getResult : ActivityResultLauncher<Intent>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

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

    private fun populateItemRows(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.setData(item)
    }

    private fun showLoadingView(holder: LoadingViewHolder, position: Int) {

    }


    inner class ItemViewHolder(private val binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root) {
        private var boardId : Long = -1
        private var title = ""
        private var content = ""

        fun setData(boardData: BoardData) {
            title = boardData.boardTitle
            content = boardData.boardContent

            binding.itemBoardTvTitle.text = if(title.length > 20) title.substring(0, 20) + "..." else title
            binding.itemBoardTvContent.text = if(content.length > 30) content.substring(0, 30) + "..." else content

            binding.itemBoardTvDate.text = boardData.date
            binding.itemBoardTvNickname.text = boardData.nickname
            binding.itemBoardTvRecommend.text = boardData.recommend.toString()

            boardId = boardData.id

            binding.itemBoardCl.setOnClickListener {
                getResult.launch(Intent(context, BoardViewActivity::class.java).putExtra("boardData", boardData))
            }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)
}