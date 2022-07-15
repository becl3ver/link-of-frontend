package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.data.BoardData
import com.example.logisticsestimate.data.BoardRequestDto
import com.example.logisticsestimate.data.BoardResponseDto
import com.example.logisticsestimate.databinding.ActivityBoardListBinding
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Long.MAX_VALUE

/**
 * 게시판 번호에 해당하는 글 목록을 불러오는 액티비티
 *
 * @author 최재훈
 * @version 1.0
 */
class BoardListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardListBinding
    private lateinit var adapter : BoardRecyclerViewAdapter

    private var category = -1
    private var minPostNumber = MAX_VALUE
    private var lastBoardPosition = -1;
    private var boardDataList = ArrayList<BoardData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getIntExtra("category", -1)

        if(category == -1) {
            Log.d(BoardListActivity::class.java.name, "카테고리 정보가 인텐트에 포함되지 않음")
            finish()
        }

        binding.activityBoardListFab.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }

        setBoardDataList()
        lastBoardPosition = boardDataList.size
    }

    private fun setBoardDataList() {
        val request = BoardRequestDto("", "", minPostNumber, category, 30)
        val call = BoardRetrofitBuilder.getInstance().getBoards(request)

        call.enqueue(object : Callback<BoardResponseDto> {
            override fun onResponse(
                call: Call<BoardResponseDto>,
                response: Response<BoardResponseDto>
            ) {
                if(!response.isSuccessful) {
                    Toast.makeText(this@BoardListActivity, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    for(bd in response.body()?.boards!!) {
                        boardDataList.add(bd)
                    }

                    Toast.makeText(this@BoardListActivity, "글을 불러왔습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BoardResponseDto>, t: Throwable) {
                Toast.makeText(this@BoardListActivity, "로딩 실패", Toast.LENGTH_SHORT).show()
            }
        })

        Thread {
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        runOnUiThread {
            adapter = BoardRecyclerViewAdapter(boardDataList)
            binding.activityBoardListRvList.adapter = adapter
            binding.activityBoardListRvList.layoutManager = LinearLayoutManager(this)
        }
    }
}