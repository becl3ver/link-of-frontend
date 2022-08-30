package com.example.logisticsestimate.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.logisticsestimate.R

/**
 * 로그인한 사용자의 정보를 표시하고, 수정할 수 있게 한다.
 */
class MyInfoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
    }
}