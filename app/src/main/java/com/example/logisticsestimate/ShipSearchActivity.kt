package com.example.logisticsestimate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap

class ShipSearchActivity : AppCompatActivity() {
    private lateinit var mMap : GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ship_search)

        test()
    }

    private fun test() {
        val intent = Intent()
        intent.putExtra("latitude", 37.0)
        intent.putExtra("longitude", 160.0)
        setResult(RESULT_OK, intent)
        finish()
    }
}