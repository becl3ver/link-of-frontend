package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.logisticsestimate.data.BoardDto
import com.example.logisticsestimate.databinding.ActivityNewPostBinding
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
class NewPostActivity : AppCompatActivity() {
    val TAG = NewPostActivity::class.java.name
    private lateinit var binding: ActivityNewPostBinding
    private var category: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        category = intent.getIntExtra("category", -1)

        if(category == -1) {
            Log.d(NewPostActivity::class.java.name, "category value out of range")
            Toast.makeText(this@NewPostActivity, "글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

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
                if(binding.activityNewPostEtTitle.text.equals("") || binding.activityNewPostEtContent.equals("")) {
                    Toast.makeText(this@NewPostActivity, "제목과 내용은 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val title = binding.activityNewPostEtTitle.text.toString()
                    val content = binding.activityNewPostEtContent.text.toString()
                    val boardDto = BoardDto(title, content, category)
                    val token = App.prefs.getAccessToken("")

                    val call = BoardRetrofitBuilder.getInstance().putNewBoard(token, boardDto)
                    call.enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (!response.isSuccessful) {
                                Log.d(TAG, "Response is not successful")
                                setResult(RESULT_CANCELED)
                                finish()
                            } else {
                                Log.d(TAG, response.body()!!)
                                setResult(RESULT_OK)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d(TAG, "FAILED");
                            setResult(RESULT_CANCELED)
                            finish()
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