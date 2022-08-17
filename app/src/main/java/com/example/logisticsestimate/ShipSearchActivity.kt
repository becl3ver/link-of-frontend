package com.example.logisticsestimate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logisticsestimate.data.traffic.ShipInfo
import com.example.logisticsestimate.data.traffic.TrafficResponseDto
import com.example.logisticsestimate.databinding.ActivityShipSearchBinding
import com.example.logisticsestimate.request.TrafficService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Marine Traffic API와 통신해서 선박의 위도, 경도를 조회한다.
 * 조회한 정보를 다시 TrackingFragment로 전달한다.
 */
class ShipSearchActivity : AppCompatActivity() {
    private val SEARCH_OPTION_NAME = arrayOf("선박 ID", "IMO", "MMSI")
    private lateinit var binding : ActivityShipSearchBinding
    private var searchType = -1

    private var shipInfo : ShipInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShipSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter.createFromResource(this, R.array.ship_search_type, R.layout.ship_search_option_spinner)
        adapter.setDropDownViewResource(R.layout.ship_search_option_spinner_dropdown)
        binding.activityShipSearchSpnType.adapter = adapter

        binding.activityShipSearchSpnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchType = position
                binding.activityShipSearchEtId.hint = SEARCH_OPTION_NAME[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.activityShipSearchBtnSearch.setOnClickListener {
            val id = binding.activityShipSearchEtId.text.toString()

            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val builder = OkHttpClient.Builder()
            val client = builder.addInterceptor(interceptor).build()
            val service = Retrofit.Builder()
                .baseUrl(BuildConfig.MARINE_TRAFFIC_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrafficService::class.java)

            val call = when(searchType) {
                0 -> service.getShipCoordinateById(BuildConfig.MARINE_TRAFFIC_API_KEY, 2880, id)
                1 -> service.getShipCoordinateByImo(BuildConfig.MARINE_TRAFFIC_API_KEY, 2880, id)
                else -> service.getShipCoordinateByMmsi(BuildConfig.MARINE_TRAFFIC_API_KEY, 2880, id)
            }

            call.enqueue(object : Callback<TrafficResponseDto> {
                override fun onResponse(
                    call: Call<TrafficResponseDto>,
                    response: Response<TrafficResponseDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@ShipSearchActivity, "서비스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        shipInfo = ShipInfo(response.body()!!)
                        
                        binding.activityShipSearchTvMmsiResult.text = shipInfo!!.mmsi
                        binding.activityShipSearchTvTimestampResult.text = shipInfo!!.timestamp
                        binding.activityShipSearchTvLatitudeResult.text = shipInfo!!.lat.toString()
                        binding.activityShipSearchTvLongitudeResult.text = shipInfo!!.lon.toString()
                    }
                }

                override fun onFailure(call: Call<TrafficResponseDto>, t: Throwable) {
                    Log.d("http", t.message!!)
                }
            })
        }

        binding.activityShipSearchBtnResult.setOnClickListener {
            if(shipInfo != null) {
                val intent = Intent()
                intent.putExtra("shipInfo", shipInfo)
                setResult(RESULT_OK, intent)
            } else {
                setResult(RESULT_CANCELED)
            }

            finish()
        }
    }
}