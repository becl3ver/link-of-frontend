package com.example.logisticsestimate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.data.BoardData
import com.example.logisticsestimate.data.Comment
import com.example.logisticsestimate.databinding.FooterBoardBinding
import com.example.logisticsestimate.databinding.HeaderBoardBinding
import com.example.logisticsestimate.databinding.ItemCommentBinding
import com.example.logisticsestimate.databinding.ItemNestedCommentBinding

class CommentRecyclerViewAdapter(private val items : ArrayList<Comment>, private val boardData: BoardData)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_HEADER -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBoardBinding.inflate(layoutInflater, parent, false)
                HeaderViewHolder(binding)
            }
            VIEW_TYPE_COMMENT -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
                CommentViewHolder(binding)
            }
            VIEW_TYPE_NESTED_COMMENT -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNestedCommentBinding.inflate(layoutInflater, parent, false)
                NestedCommentViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FooterBoardBinding.inflate(layoutInflater, parent, false)
                FooterViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CommentViewHolder) {
            holder.setData(items[position]!!)
        } else if(holder is NestedCommentViewHolder) {
            holder.setData(items[position]!!)
        }
    }

    inner class HeaderViewHolder(binding : HeaderBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.headerBoardTitle.text = boardData.boardTitle
            binding.headerBoardContent.text = boardData.boardContent
            binding.headerBoardNickname.text = boardData.nickname
            binding.headerBoardDate.text = boardData.date
        }
    }

    inner class FooterViewHolder(binding : FooterBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    inner class CommentViewHolder(binding : ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val container = binding.itemCommentCl
        private val content = binding.itemCommentTvContent
        private val date = binding.itemCommentTvDate
        private val nickname = binding.itemCommentTvNickname
        private var commentId : Long? = -1

        fun setData(comment : Comment) {
            content.text = comment.content
            date.text = comment.date
            nickname.text = comment.nickname
            commentId = comment.id

            container.setOnClickListener {
                TODO("누르면 대댓글 달 수 있게 구현")
            }

            container.setOnLongClickListener {
                TODO("길게 누르면 삭제할 수 있게 구현")
                return@setOnLongClickListener true
            }
        }
    }

    inner class NestedCommentViewHolder(binding : ItemNestedCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val container = binding.itemNestedCommentCl
        private val content = binding.itemNestedCommentTvContent
        private val date = binding.itemNestedCommentTvDate
        private val nickname = binding.itemNestedCommentTvNickname
        private var commentId : Long? = -1
        private var parentId : Long? = -1

        fun setData(comment: Comment) {
            content.text = comment.content
            date.text = comment.date
            nickname.text = comment.nickname
            commentId = comment.id
            parentId = comment.parentId!!

            container.setOnClickListener {
                TODO("누르면 대댓글 달 수 있게 구현")
            }

            container.setOnLongClickListener {
                TODO("길게 누르면 삭제할 수 있게 구현")
                return@setOnLongClickListener true
            }
        }
    }

    companion object {
        val VIEW_TYPE_HEADER = 0
        val VIEW_TYPE_COMMENT = 1
        val VIEW_TYPE_NESTED_COMMENT = 2
        val VIEW_TYPE_FOOTER = 3
    }
}