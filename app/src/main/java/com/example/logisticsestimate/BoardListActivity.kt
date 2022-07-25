package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.data.BoardData
import com.example.logisticsestimate.data.BoardRequestDto
import com.example.logisticsestimate.data.BoardResponseDto
import com.example.logisticsestimate.databinding.ActivityBoardListBinding
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 게시판 번호에 해당하는 글 목록을 불러오는 액티비티
 *
 * @author 최재훈
 * @version 1.0
 */
class BoardListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardListBinding
    private lateinit var adapter : BoardRecyclerViewAdapter
    private lateinit var request : BoardRequestDto

    private val boards = ArrayList<BoardData>()

    private var isLoading = false
    private var category = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getIntExtra("category", -1)

        if(category == -1) {
            Toast.makeText(this@BoardListActivity, "게시판에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        request = BoardRequestDto("", "", Long.MAX_VALUE, category, 20)

        initAdapter()
        initScrollListener()
        addTemporaryBoardsTest() // 테스트용
        // initBoardList()

        binding.activityBoardListFab.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }
    }

    private fun initAdapter() {
        adapter = BoardRecyclerViewAdapter(boards)
        val layoutManager = LinearLayoutManager(this)
        binding.activityBoardListRvList.adapter = adapter
        binding.activityBoardListRvList.layoutManager = layoutManager
    }

    private fun initScrollListener() {
        binding.activityBoardListRvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)

                if(!isLoading) {
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == boards.size - 1) {
                        getMoreTest()
                        //getMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun getMoreTest() {
        boards.add(BoardData(-1, 0, 0, "", "", "", ""))
        adapter.notifyItemInserted(boards.size - 1)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            boards.removeLast()
            val scrollPosition = boards.size
            adapter.notifyItemRemoved(scrollPosition)

            addTemporaryBoardsTest()

            adapter.notifyDataSetChanged()
            isLoading = false
        }, 2000)
    }

    private var i : Long = 1
    private fun addTemporaryBoardsTest() {
        for(i in i..(i + 19)) {
            val boardData = BoardData(2, i, i, "something$i", "something$i", "something$i", "someone$i")
            boards.add(boardData)
        }
        i += 20
    }

    private fun initBoardList() {
        boards.add(BoardData(-1, 0, 0, "", "", "", ""))
        adapter.notifyItemInserted(0)

        val call = BoardRetrofitBuilder.getInstance().getBoards(request)
        call.enqueue(object : Callback<BoardResponseDto> {
            override fun onResponse(
                call: Call<BoardResponseDto>,
                response: Response<BoardResponseDto>
            ) {
                if(!response.isSuccessful) {
                    Toast.makeText(this@BoardListActivity, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    if(response.body()!!.boards.size == 0) {
                        Toast.makeText(this@BoardListActivity, "마지막 글입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        boards.removeLast()
                        adapter.notifyItemRemoved(0)

                        for(bd in response.body()!!.boards) {
                            boards.add(bd)
                        }

                        adapter.notifyDataSetChanged()

                        initScrollListener()
                    }
                }
            }

            override fun onFailure(call: Call<BoardResponseDto>, t: Throwable) {
                Toast.makeText(this@BoardListActivity, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMore(isInit : Boolean) {
        request.range = boards.last().postId.toLong()

        boards.add(BoardData(-1, 0, 0, "", "", "", ""))
        adapter.notifyItemInserted(boards.size - 1)

        val call = BoardRetrofitBuilder.getInstance().getBoards(request)
        call.enqueue(object : Callback<BoardResponseDto> {
            override fun onResponse(
                call: Call<BoardResponseDto>,
                response: Response<BoardResponseDto>
            ) {
                if(!response.isSuccessful) {
                    Toast.makeText(this@BoardListActivity, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    if(response.body()!!.boards.size == 0) {
                        Toast.makeText(this@BoardListActivity, "마지막 글입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        boards.removeLast()
                        val scrollPosition = boards.size
                        adapter.notifyItemRemoved(scrollPosition)

                        for(bd in response.body()!!.boards) {
                            boards.add(bd)
                        }

                        adapter.notifyDataSetChanged()
                        isLoading = false

                        if(isInit) {
                            initScrollListener()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BoardResponseDto>, t: Throwable) {
                Toast.makeText(this@BoardListActivity, "글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}