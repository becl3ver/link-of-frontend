package com.example.logisticsestimate

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.App.Companion.context
import com.example.logisticsestimate.data.board.BoardData
import com.example.logisticsestimate.databinding.ItemLoadingBinding
import com.example.logisticsestimate.databinding.ItemBoardBinding

/**
 * 글 목록을 불러오는 중이라면 LoadingViewHolder 객체를 생성하고, 그렇지 않다면 ItemViewHolder 객체를 생성한다.
 */
class BoardRecyclerViewAdapter(private val items : ArrayList<BoardData>, private val getResult : ActivityResultLauncher<Intent>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    // 리스트의 마지막 원소의 category 멤버 변수가 -1로 설정되어 있다면 LoadingViewHolder 객체를 생성하게 한다.
    override fun getItemViewType(position: Int): Int {
        return if(items[position].category == -1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun populateItemRows(holder : ItemViewHolder, position: Int) {
        val item = items[position]
        holder.setData(item)
    }

    private fun showLoadingView(holder : LoadingViewHolder, position: Int) {

    }


    inner class ItemViewHolder(binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val container = binding.itemBoardCl
        private val title = binding.itemPostTvTitle
        private val content = binding.itemPostTvContent
        private val date = binding.itemPostTvDate
        private val nickname = binding.itemPostTvNickname

        private var boardId : Long = -1
        private var _title = ""
        private var _content = ""

        fun setData(boardData: BoardData) {
            _title = boardData.boardTitle
            _content = boardData.boardContent

            title.text = if(_title.length > 20) _title.substring(0, 20) + "..." else _title
            content.text = if(_content.length > 30) _content.substring(0, 30) + "..." else _content

            date.text = boardData.date
            nickname.text = boardData.nickname

            boardId = boardData.id

            container.setOnClickListener {
                getResult.launch(Intent(context, BoardViewActivity::class.java).let {
                    it.putExtra("boardData", boardData)
                })
            }
        }
    }

    inner class LoadingViewHolder(binding : ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        private val progressBar : ProgressBar = binding.progressBar
    }
}