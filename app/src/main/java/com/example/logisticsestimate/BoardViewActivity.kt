package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.data.*
import com.example.logisticsestimate.databinding.ActivityBoardViewBinding
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardViewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardViewBinding
    private val items = ArrayList<Comment>()
    private lateinit var adapter : CommentRecyclerViewAdapter
    private var boardData : BoardData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        boardData = intent.getSerializableExtra("boardData") as BoardData?

        val call = BoardRetrofitBuilder
            .getInstance()
            .getComments(CommentRequestDto(intent.getLongExtra("boardId", -1)))

        call.enqueue(object : Callback<CommentResponseDto> {
            override fun onResponse(
                call: Call<CommentResponseDto>,
                response: Response<CommentResponseDto>
            ) {
                if(!response.isSuccessful) {
                    Toast.makeText(this@BoardViewActivity, "댓글을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    dataConversion(ArrayList<CommentData>())
                    setRecyclerView()
                } else {
                    dataConversion(response.body()!!.comments)
                    setRecyclerView()
                }
            }

            override fun onFailure(call: Call<CommentResponseDto>, t: Throwable) {
                Toast.makeText(this@BoardViewActivity, "댓글을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        binding.activityBoardViewBtnSubmit.setOnClickListener {
            if(binding.activityBoardViewEtNew.text.toString() == "") {
                return@setOnClickListener
            }

            val content = binding.activityBoardViewEtNew.text.toString()
            val commentDto = CommentDto(content, null, false)
            val call = BoardRetrofitBuilder.getInstance().putNewComment(App.prefs.getAccessToken(""), commentDto)

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!response.isSuccessful) {

                    } else {

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })
        }
    }

    private fun setRecyclerView() {
        runOnUiThread {
            adapter = CommentRecyclerViewAdapter(items, boardData!!)
            binding.activityBoardViewRv.adapter = adapter
            binding.activityBoardViewRv.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun dataConversion(comments : ArrayList<CommentData>) {
        items.add(Comment(CommentRecyclerViewAdapter.VIEW_TYPE_HEADER,
            null, null, null, null, null, null, null))
        for(comment in comments) {
            items.add(Comment(CommentRecyclerViewAdapter.VIEW_TYPE_COMMENT,
                comment.content, comment.date, comment.nickname, comment.id, comment.uid, null, null))

            for(nested in comment.nestedComments) {
                items.add(Comment(CommentRecyclerViewAdapter.VIEW_TYPE_NESTED_COMMENT,
                    nested.content, nested.date, nested.nickname, nested.id, nested.uid, comment.id, comment.uid))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(App.prefs.getUid(-1) == boardData!!.uid) {
            menuInflater.inflate(R.menu.board_view_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.update_board -> {
            }
            R.id.delete_board -> {
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}