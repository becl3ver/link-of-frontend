package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.logisticsestimate.data.board.BoardDto
import com.example.logisticsestimate.data.board.BoardUpdateDto
import com.example.logisticsestimate.databinding.ActivityNewBoardBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TemporaryEntity
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

/**
 * 게시판에 대한 새로운 글을 작성할 수 있게 한다.
 * 작성 중 뒤로 이동하면 작성 중이던 내용을 Room 지속성 라이브러리에 저장할지 AlertDialog를 통해서 확인한다.
 */
class NewBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewBoardBinding
    private var category = 0
    private var boardId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        category = intent.getIntExtra("category", 0)
        boardId = intent.getLongExtra("boardId", -1)
        binding.activityNewPostEtTitle.setText(intent.getStringExtra("title"))
        binding.activityNewPostEtContent.setText(intent.getStringExtra("content"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.create_board -> {
                if(binding.activityNewPostEtTitle.text.toString() == "" || binding.activityNewPostEtContent.text.toString() == "") {
                    Toast.makeText(this@NewBoardActivity, "제목과 내용은 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val title = binding.activityNewPostEtTitle.text.toString()
                    val content = binding.activityNewPostEtContent.text.toString()
                    val token = App.prefs.getAccessToken("")
                    val service = BoardRetrofitBuilder.getInstance()

                    val call = when(boardId) {
                        (-1).toLong() -> {
                            val boardDto = BoardDto(title, content, category)
                            service.putNewBoard(token, boardDto)
                        }
                        else -> {
                            val boardUpdateDto = BoardUpdateDto(title, content, category, boardId)
                            service.updateBoard(token, boardUpdateDto)
                        }
                    }

                    call.enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (!response.isSuccessful) {
                                if(response.code() == 401) {

                                } else {
                                    Toast.makeText(this@NewBoardActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@NewBoardActivity, "작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d("http", t.message!!)
                            Toast.makeText(this@NewBoardActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.activityNewPostEtTitle.text.toString() == "" && binding.activityNewPostEtContent.text.toString() == "") {
            finish()
        } else {
            AlertDialog.Builder(this).let {
                it.setTitle("알림")
                it.setMessage("작성하시던 글이 있습니다. 글을 저장하시겠습니까?")
                it.setPositiveButton("네") { _, _ ->
                    val db = AppDatabase.getInstance(this)!!
                    val temporaryPostDao = db.getTemporaryPostDao()

                    Thread {
                        temporaryPostDao.insertEntity(
                            TemporaryEntity(
                                0,
                                category,
                                binding.activityNewPostEtTitle.text.toString(),
                                binding.activityNewPostEtContent.text.toString(),
                                LocalDate.now().toString()
                            )
                        )
                    }.start()

                    finish()
                }
                it.setNegativeButton("아니오") { _, _ ->
                    finish()
                }
                it.create()
                it.show()
            }
        }
    }
}