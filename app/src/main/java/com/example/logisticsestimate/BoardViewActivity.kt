package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.data.board.BoardData
import com.example.logisticsestimate.data.board.BoardDeleteDto
import com.example.logisticsestimate.data.comment.*
import com.example.logisticsestimate.databinding.ActivityBoardViewBinding
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardViewActivity : AppCompatActivity() {
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        when(result.resultCode) {
            RESULT_OK -> {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private lateinit var binding : ActivityBoardViewBinding
    private lateinit var adapter : CommentRecyclerViewAdapter
    private lateinit var boardData : BoardData

    private val items = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        boardData = intent.getSerializableExtra("boardData") as BoardData

        initComment(false)

        binding.activityBoardViewBtnSubmit.setOnClickListener {
            if(binding.activityBoardViewEtNew.text.toString() == "") {
                return@setOnClickListener
            }

            val content = binding.activityBoardViewEtNew.text.toString()
            val commentDto = CommentDto(content, parentCommentId ?: boardData.id, parentCommentId != null)
            val token = App.prefs.getAccessToken("")
            val call = BoardRetrofitBuilder.getInstance().putNewComment(token, commentDto)

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!response.isSuccessful) {
                        if(response.code() == 401) {

                        } else {

                        }
                    } else {
                        Toast.makeText(this@BoardViewActivity, "작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        binding.activityBoardViewEtNew.setText("")
                        if(parentCommentId != null) {
                            CommentRecyclerViewAdapter.selectedContainer?.setBackgroundColor(getColor(R.color.white))
                            parentCommentId = null
                        }
                        initComment(true)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("http", t.message!!)
                }
            })
        }
    }

    private fun initComment(isUpdate : Boolean) {
        val call = BoardRetrofitBuilder
            .getInstance()
            .getComments(CommentRequestDto(boardData.id))

        call.enqueue(object : Callback<CommentResponseDto> {
            override fun onResponse(
                call: Call<CommentResponseDto>,
                response: Response<CommentResponseDto>
            ) {
                if(!response.isSuccessful) {
                    Toast.makeText(this@BoardViewActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    if(isUpdate) {
                        items.clear()
                        dataConversion(response.body()!!.comments)
                        adapter.notifyItemRangeChanged(0, items.size)
                    } else {
                        dataConversion(response.body()!!.comments)
                        setRecyclerView()
                    }
                }
            }

            override fun onFailure(call: Call<CommentResponseDto>, t: Throwable) {
                Log.d("http", t.message!!)
                Toast.makeText(this@BoardViewActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setRecyclerView() {
        runOnUiThread {
            adapter = CommentRecyclerViewAdapter(items, boardData, this)
            binding.activityBoardViewRv.adapter = adapter
            binding.activityBoardViewRv.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun dataConversion(comments : ArrayList<CommentData>) {
        items.add(
            Comment(CommentRecyclerViewAdapter.VIEW_TYPE_HEADER,
            null, null, null, null, null, null, null)
        )
        for(comment in comments) {
            items.add(
                Comment(CommentRecyclerViewAdapter.VIEW_TYPE_COMMENT,
                comment.content, comment.date, comment.nickname, comment.id, comment.uid, null, null)
            )

            for(nested in comment.nestedComments) {
                items.add(
                    Comment(CommentRecyclerViewAdapter.VIEW_TYPE_NESTED_COMMENT,
                    nested.content, nested.date, nested.nickname, nested.id, nested.uid, comment.id, comment.uid)
                )
            }
        }
    }

    override fun onBackPressed() {
        if(parentCommentId != null) {
            AlertDialog.Builder(this).let {
                it.setMessage("작성하시던 대댓글이 있습니다.\n작성을 취소하시겠습니까?")
                it.setPositiveButton("확인") { _, _ ->
                    adapter.notifyItemChanged(parentPosition)
                    parentCommentId = null
                }
                it.setNegativeButton("취소") { _, _ -> }
                it.create()
                it.show()
            }
        } else if(binding.activityBoardViewEtNew.text.toString() != "") {
            AlertDialog.Builder(this).let {
                it.setMessage("작성하시던 댓글이 있습니다.\n작성을 취소하시겠습니까?")
                it.setPositiveButton("확인") { _, _ ->
                    setResult(RESULT_CANCELED)
                    finish()
                }
                it.setNegativeButton("취소") { _, _ -> }
                it.create()
                it.show()
            }
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(App.prefs.getUid(-1) == boardData.uid) {
            menuInflater.inflate(R.menu.board_view_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.update_board -> {
                val intent = Intent(this, NewBoardActivity::class.java)
                intent.putExtra("title", boardData.boardTitle)
                intent.putExtra("content", boardData.boardContent)
                intent.putExtra("category", boardData.category)
                intent.putExtra("boardId", boardData.id)
                getResult.launch(intent)
            }
            R.id.delete_board -> {
                AlertDialog.Builder(this).let {
                    it.setMessage("글을 삭제하시겠습니까?")
                    it.setPositiveButton("확인") { _, _ ->
                        val call = BoardRetrofitBuilder.getInstance().deleteBoard(App.prefs.getAccessToken(""), BoardDeleteDto(boardData.id))

                        call.enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if(!response.isSuccessful) {
                                    if(response.code() == 401) {

                                    } else {
                                        Toast.makeText(this@BoardViewActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this@BoardViewActivity, "글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                                    setResult(RESULT_OK)
                                    finish()
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.d("http", t.message!!)
                                Toast.makeText(this@BoardViewActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    it.setNegativeButton("취소") { _, _ -> }
                    it.create()
                    it.show()
                }

            }
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        var parentCommentId : Long? = null
        var parentPosition = 0
    }
}