package com.example.logisticsestimate

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.data.board.BoardData
import com.example.logisticsestimate.data.comment.Comment
import com.example.logisticsestimate.data.comment.CommentDeleteDto
import com.example.logisticsestimate.databinding.HeaderBoardBinding
import com.example.logisticsestimate.databinding.ItemCommentBinding
import com.example.logisticsestimate.databinding.ItemNestedCommentBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.BookmarkEntity
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentRecyclerViewAdapter(private val items : ArrayList<Comment>, private val boardData: BoardData, private val context : Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val uid = App.prefs.getUid(-1)
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
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNestedCommentBinding.inflate(layoutInflater, parent, false)
                NestedCommentViewHolder(binding)
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

    inner class HeaderViewHolder(private val binding : HeaderBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isUsing = false
        private var isMarked = false

        init {
            binding.headerBoardTitle.text = boardData.boardTitle
            binding.headerBoardContent.text = boardData.boardContent
            binding.headerBoardNickname.text = boardData.nickname
            binding.headerBoardDate.text = boardData.date

            isUsing = true
            Thread {
                val db = AppDatabase.getInstance(context)!!
                val bookmarkDao = db.getBookmarkDao()
                val entities = bookmarkDao.selectEntity(boardData.id)

                isMarked = if(entities.isEmpty()) {
                    false
                } else {
                    binding.headerBoardBtnBookmark.setImageResource(R.drawable.header_board_ic_star_24)
                    true
                }

                isUsing = false
            }.start()

            binding.headerBoardBtnBookmark.setOnClickListener {
                if(!isUsing) {
                    isUsing = true

                    Toast.makeText(context, if(isMarked) "북마크가 해제되었습니다." else "북마크 되었습니다.", Toast.LENGTH_SHORT).show()

                    Thread {
                        val db = AppDatabase.getInstance(context)!!
                        val bookmarkDao = db.getBookmarkDao()

                        if(isMarked) {
                            bookmarkDao.deleteEntity(BookmarkEntity(boardData.id))
                            isMarked = false
                            binding.headerBoardBtnBookmark.setImageResource(R.drawable.header_board_ic_star_border_24)
                        } else {
                            bookmarkDao.insertEntity(BookmarkEntity(boardData.id))
                            isMarked = true
                            binding.headerBoardBtnBookmark.setImageResource(R.drawable.header_board_ic_star_24)
                        }

                        isUsing = false
                    }.start()
                }
            }
        }
    }

    inner class CommentViewHolder(private val binding : ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        private var commentId : Long = 0

        fun setData(comment : Comment) {
            binding.itemCommentTvContent.text = comment.content
            binding.itemCommentTvDate.text = comment.date
            binding.itemCommentTvNickname.text = comment.nickname

            if(comment.uid != uid) {
                binding.itemCommentBtnDelete.visibility = View.GONE
            }

            commentId = comment.id!!

            binding.itemCommentCl.setOnClickListener {
                AlertDialog.Builder(context).let {
                    it.setMessage("대댓글을 작성하시겠습니까?")
                    it.setPositiveButton("확인") { _, _ ->
                        selectedContainer?.setBackgroundColor(context.getColor(R.color.white))
                        selectedContainer = binding.itemCommentCl
                        selectedContainer!!.setBackgroundColor(context.getColor(R.color.sky_blue))

                        BoardViewActivity.parentCommentId = commentId
                        BoardViewActivity.parentPosition = adapterPosition
                    }
                    it.setNegativeButton("취소") { _, _ ->}
                    it.create()
                    it.show()
                }
            }

            binding.itemCommentBtnDelete.setOnClickListener {
                deleteComment(commentId, VIEW_TYPE_COMMENT, adapterPosition)
            }
        }
    }

    inner class NestedCommentViewHolder(private val binding : ItemNestedCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        private var commentId : Long = 0
        private var parentId : Long = 0

        fun setData(comment: Comment) {
            binding.itemNestedCommentTvContent.text = comment.content
            binding.itemNestedCommentTvDate.text = comment.date
            binding.itemNestedCommentTvNickname.text = comment.nickname
            commentId = comment.id!!
            parentId = comment.parentId!!

            if(comment.uid != uid) {
                binding.itemNestedCommentBtnDelete.visibility = View.GONE
            }

            binding.itemNestedCommentBtnDelete.setOnClickListener {
                deleteComment(commentId, VIEW_TYPE_NESTED_COMMENT, adapterPosition)
            }
        }
    }

    private fun deleteComment(commentId : Long, type : Int, position: Int) {
        AlertDialog.Builder(context).let {
            it.setMessage("댓글을 삭제하시겠습니까?")
            it.setPositiveButton("확인") { _, _ ->
                val call = BoardRetrofitBuilder.getInstance().deleteComment(App.prefs.getAccessToken(""), CommentDeleteDto(commentId))

                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(!response.isSuccessful) {
                            if(response.code() == 401) {
                                AlertDialog.Builder(context).let {
                                    it.setMessage("로그인 세션이 만료되었습니다. 다시 로그인 하시겠습니까?")
                                    it.setPositiveButton("확인") { _, _ ->

                                    }
                                    it.setNegativeButton("취소") { _, _ -> }
                                    it.create()
                                    it.show()
                                }
                            } else {
                                Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            if(type == VIEW_TYPE_NESTED_COMMENT) {
                                items.removeAt(position)
                                notifyItemRemoved(position)
                            } else {
                                var cnt = 1
                                for(i in position + 1 until items.size) {
                                    if(items[i].parentId == null) {
                                        break;
                                    }
                                    cnt++
                                }

                                for(i in 1..cnt) {
                                    items.removeAt(position)
                                }

                                notifyItemRangeRemoved(position, cnt)
                            }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(context, "연결할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("http", t.message!!)
                    }
                })
            }
            it.setNegativeButton("취소") { _, _ -> }
            it.create()
            it.show()
        }
    }

    companion object {
        val VIEW_TYPE_HEADER = 0
        val VIEW_TYPE_COMMENT = 1
        val VIEW_TYPE_NESTED_COMMENT = 2

        var selectedContainer : ConstraintLayout? = null
        var parentCommentId : Long? = null
        var parentPosition = 0
    }
}