package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.logisticsestimate.databinding.ActivityNewPostBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TemporaryPostEntity
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

        if(intent.hasExtra("title")) {
            binding.activityNewPostEtTitle.setText(intent.getStringExtra("title"))
        }

        if(intent.hasExtra("content")) {
            binding.activityNewPostEtContent.setText(intent.getStringExtra("content"))
        }

        category = intent.getIntExtra("category", -1)
    }

    /**
     * 뒤로가기 시, 작성 중인 글이 없다면 그대로 종료
     * 작성 중인 글이 있다면 임시 저장 확인
     */
    override fun onBackPressed() {
        if (binding.activityNewPostEtTitle.text == null || binding.activityNewPostEtContent.text == null) {
            finish()
        }

        AlertDialog.Builder(this)
            .setTitle("알림")
            .setMessage("작성하시던 글이 있습니다. 글을 저장하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                val db = AppDatabase.getInstance(this)!!
                val temporaryPostDao = db.getTemporaryPostDado()
                temporaryPostDao.insertTemporaryPost(
                    TemporaryPostEntity(
                        null, category, binding.activityNewPostEtTitle.text.toString(),
                        binding.activityNewPostEtContent.text.toString(), LocalDate.now().toString()
                    )
                )
            }
            .setNegativeButton("네") { _, _ ->
                finish()
            }
            .create()
            .show()

    }
}