package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.logisticsestimate.data.BoardDto
import com.example.logisticsestimate.databinding.ActivityNewPostBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TemporaryPostEntity
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

/**
 * 새 글 작성하는 액티비티
 * 작성 중 뒤로 이동하면 작성 중이던 내용을 Room 지속성 라이브러리에 저장할지 AlertDialog를 통해서 확인
 * @author 최재훈
 * @version 1.0
 */
class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private var category: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getIntExtra("category", -1)

        if(category == -1) {
            Log.d(NewPostActivity::class.java.name, "category value out of range")
            Toast.makeText(this@NewPostActivity, "글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.activityNewPostBtnSubmit.setOnClickListener {
            if(binding.activityNewPostEtTitle.text.equals("") || binding.activityNewPostEtContent.equals("")) {
                Toast.makeText(this@NewPostActivity, "제목과 내용은 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            val title = binding.activityNewPostEtTitle.text.toString()
            val content = binding.activityNewPostEtContent.text.toString()
            val boardDto = BoardDto(title, content, category)
            val token = App.prefs.getAccessToken("")

            val call = BoardRetrofitBuilder.getInstance().putNewBoard(token, boardDto)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                   if(!response.isSuccessful) {

                   } else {

                       finish()
                   }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })
        }
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
                    val temporaryPostDao = db.getTemporaryPostDado()
                    temporaryPostDao.insertTemporaryPost(
                        TemporaryPostEntity(
                            null,
                            category,
                            binding.activityNewPostEtTitle.text.toString(),
                            binding.activityNewPostEtContent.text.toString(),
                            LocalDate.now().toString()
                        )
                    )
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