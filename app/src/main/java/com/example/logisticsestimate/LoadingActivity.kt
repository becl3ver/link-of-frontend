package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.logisticsestimate.databinding.ActivityLoadingBinding

/**
 * 앱 시작 시 로딩 화면 이미지 2000ms간 출력
 *
 * @author 최재훈
 * @version 1.0, 로딩 화면 출력, 사진 설정 안됨
 */
class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var bar = supportActionBar
//        bar!!.hide()

        startLoadingScreen()
    }

    private fun startLoadingScreen() {
        Handler(Looper.getMainLooper()).postDelayed({finish()}, 2000)
    }
}