package com.example.logisticsestimate

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logisticsestimate.data.TrafficResponseDto
import com.example.logisticsestimate.databinding.ActivityShipSearchBinding
import com.example.logisticsestimate.request.TrafficService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Marine Traffic API와 통신해서 선박의 위도, 경도를 조회한다.
 * 조회한 정보를 다시 TrackingFragment로 전달한다.
 */
class ShipSearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShipSearchBinding
    private var API_URL = BuildConfig.MARINE_TRAFFIC_API_URL + BuildConfig.MARINE_TRAFFIC_API_KEY
    private var searchType = -1
    private var latitude = 1000.0
    private var longitude = 1000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShipSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter.createFromResource(this, R.array.ship_search_type, android.R.layout.simple_spinner_dropdown_item)
        binding.activityShipSearchSpnType.adapter = adapter

        binding.activityShipSearchSpnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchType = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.activityShipSearchBtnSearch.setOnClickListener {
            val id = binding.activityShipSearchEtShipid.text.toString().toInt()

            val service = Retrofit.Builder().baseUrl(API_URL).build().create(TrafficService::class.java)
            var call = service.getShipCoordinateById(5, id, "json")
            when(searchType) {
                0 -> call = service.getShipCoordinateById(5, id, "json")
                1 -> call = service.getShipCoordinateByImo(5, id, "json")
                2 -> call = service.getShipCoordinateByMmsi(5, id, "json")
            }

            call.enqueue(object : Callback<TrafficResponseDto> {
                override fun onResponse(
                    call: Call<TrafficResponseDto>,
                    response: Response<TrafficResponseDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@ShipSearchActivity, "서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        latitude = response.body()!!.LAT?.toDouble() ?: 1000.0
                        longitude = response.body()!!.LON?.toDouble() ?: 1000.0

                        if(latitude == 1000.0 || longitude == 1000.0) {
                            Toast.makeText(this@ShipSearchActivity, "위치를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<TrafficResponseDto>, t: Throwable) {
                    Toast.makeText(this@ShipSearchActivity, "연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.activityShipSearchBtnResult.setOnClickListener {
            if(latitude != 1000.0 && longitude != 1000.0) {
                val intent = Intent()
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                setResult(RESULT_OK, intent)
            } else {
                setResult(RESULT_CANCELED)
            }

            finish()
        }
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