package com.example.logisticsestimate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap

/**
 * Marine Traffic API와 통신해서 선박의 위도, 경도를 조회한다.
 * 조회한 정보를 다시 TrackingFragment로 전달한다.
 */
class ShipSearchActivity : AppCompatActivity() {
    private lateinit var mMap : GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ship_search)

        test()
    }

    // 테스팅을 위한 임시 함수
    private fun test() {
        val intent = Intent()
        intent.putExtra("latitude", 37.0)
        intent.putExtra("longitude", 160.0)
        setResult(RESULT_OK, intent)
        finish()
    }
}