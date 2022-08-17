package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.data.board.BoardData
import com.example.logisticsestimate.data.board.BoardRequestDto
import com.example.logisticsestimate.data.board.BoardResponseDto
import com.example.logisticsestimate.databinding.ActivityBoardListBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.BookmarkEntity
import com.example.logisticsestimate.fragment.CommunityFragment
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

/**
 * 인텐트를 통해서 전달받은 게시판 번호에 해당되는 게시글 목록을 보여준다.
 * 글을 20개씩 불러와서 리사이클러뷰를 통해서 화면에 20개씩 표시한다.
 * 스크롤이 글 목록의 맨 아래에 도달하게 되면, 20개씩 추가적으로 로딩한다.
 */
class BoardListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardListBinding
    private lateinit var adapter : BoardRecyclerViewAdapter
    private lateinit var request : BoardRequestDto

    private val db = AppDatabase.getInstance(this)!!
    private val bookmarkDao = db.getBookmarkDao()

    private val items = ArrayList<BoardData>()
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when(result.resultCode) {
            RESULT_OK -> {
                refreshBoardList()
            }
        }
    }

    private var isLoading = false
    private var isSearched = false
    private var category = -1
    private var keyword : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getIntExtra("category", -1)
        keyword = intent.getStringExtra("keyword")
        isSearched = intent.getBooleanExtra("isSearched", false)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = CommunityFragment.BOARD_NAME[category]

        setRecyclerView()

        binding.activityBoardListSrl.setOnRefreshListener {
            if(category != 0) {
                refreshBoardList()
            }
            binding.activityBoardListSrl.isRefreshing = false
        }

        if(category == 0) {
            binding.activityBoardListFab.visibility = View.GONE

            Thread {
                val entities = ArrayList(bookmarkDao.getAll())
                val boards = ArrayList<Long>()

                for(entity in entities) {
                    boards.add(entity.boardId)
                }

                request = BoardRequestDto(null, null, null, null, boards)
                initBoardList()

                bookmarkDao.truncateEntity()
            }.start()
        } else {
            // 플로팅 버튼 클릭 리스너
            binding.activityBoardListFab.setOnClickListener {
                val intent = Intent(this, NewBoardActivity::class.java)
                intent.putExtra("category", category)
                getResult.launch(intent)
            }

            request = BoardRequestDto(keyword, Long.MAX_VALUE, category, 20, null)
            initBoardList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(category == 0) {
            Thread {
                for(item in items) {
                    bookmarkDao.insertEntity(BookmarkEntity(item.id))
                }
            }.start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(category != 0 && !isSearched) {
            menuInflater.inflate(R.menu.board_list_menu, menu)

            val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.queryHint = "글 제목, 내용"
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    val intent = Intent(this@BoardListActivity, BoardListActivity::class.java)
                    intent.putExtra("category", category)
                    intent.putExtra("keyword", query ?: "")
                    intent.putExtra("isSearched", true)
                    startActivity(intent)
                    return false
                }
            })

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_refresh -> {
                refreshBoardList()
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun refreshBoardList() {
        isLoading = true
        val position = items.size
        items.clear()
        adapter.notifyItemRangeRemoved(0, position)
        request = BoardRequestDto(keyword, Long.MAX_VALUE, category, 20, null)
        initBoardList()
    }

    private fun setRecyclerView() {
        runOnUiThread {
            adapter = BoardRecyclerViewAdapter(items, getResult)
            binding.activityBoardListRvList.adapter = adapter
            binding.activityBoardListRvList.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun initScrollListener() {
        binding.activityBoardListRvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            // 마지막으로 완전하게 보이는 아이템이 리스트의 마지막 원소라면 글을 추가로 불러온다.
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)

                if(!isLoading) {
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == items.size - 1) {
                        isLoading = true
                        getMore()
                    }
                }
            }
        })
    }

    private fun initBoardList() {
        items.add(BoardData(-1, 0, 0, "", "", "", ""))
        adapter.notifyItemInserted(0)

        val call = BoardRetrofitBuilder.getInstance().getBoards(request)
        call.enqueue(object : Callback<BoardResponseDto> {
            override fun onResponse(
                call: Call<BoardResponseDto>,
                response: Response<BoardResponseDto>
            ) {
                if(!response.isSuccessful) {
                    Toast.makeText(this@BoardListActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    if(response.body()!!.boards.size == 0) {
                        Toast.makeText(this@BoardListActivity, "마지막 글입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        items.removeLast()
                        adapter.notifyItemRemoved(0)

                        for(bd in response.body()!!.boards) {
                            items.add(bd)
                        }

                        adapter.notifyItemRangeInserted(0, response.body()!!.boards.size)

                        if(category != 0) {
                            initScrollListener()
                            isLoading = false
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BoardResponseDto>, t: Throwable) {
                Toast.makeText(this@BoardListActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMore() {
        request.range = items.last().id - 1

        items.add(BoardData(-1, 0, 0, "", "", "", ""))
        adapter.notifyItemInserted(items.size - 1)

        val call = BoardRetrofitBuilder.getInstance().getBoards(request)
        call.enqueue(object : Callback<BoardResponseDto> {
            override fun onResponse(
                call: Call<BoardResponseDto>,
                response: Response<BoardResponseDto>
            ) {
                if(!response.isSuccessful) {
                    Toast.makeText(this@BoardListActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    items.removeLast()
                    var scrollPosition = items.size
                    adapter.notifyItemRemoved(scrollPosition)

                    if(response.body()!!.boards.size == 0) {
                        Toast.makeText(this@BoardListActivity, "마지막 글입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        scrollPosition = items.size

                        for(bd in response.body()!!.boards) {
                            items.add(bd)
                        }

                        adapter.notifyItemRangeInserted(scrollPosition, response.body()!!.boards.size)
                        isLoading = false
                    }
                }
            }

            override fun onFailure(call: Call<BoardResponseDto>, t: Throwable) {
                Toast.makeText(this@BoardListActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}