package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.logisticsestimate.databinding.ActivityPostListBinding

/**
 * 게시판 번호에 해당하는 글 목록을 불러오는 액티비티
 *
 * @author 최재훈
 * @version 1.0
 */
class BoardListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostListBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}