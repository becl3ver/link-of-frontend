package com.example.logisticsestimate.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logisticsestimate.BuildConfig
import com.example.logisticsestimate.R
import com.example.logisticsestimate.base.App.Companion.context
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.TrackingHistoryEntity
import com.example.logisticsestimate.data.remote.model.traffic.ShipInfo
import com.example.logisticsestimate.data.remote.model.traffic.TrafficResponseDto
import com.example.logisticsestimate.databinding.ActivityShipTrackingBinding
import com.example.logisticsestimate.data.remote.api.service.TrafficService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Marine Traffic API 이용해서 선박의 위도, 경도를 조회
 */
class ShipTrackingActivity : AppCompatActivity() {
    private val SEARCH_OPTION_NAME = arrayOf("선박 ID", "IMO", "MMSI")

    private lateinit var binding : ActivityShipTrackingBinding
    private lateinit var db : AppDatabase

    private var searchType = -1
    private var shipInfo : ShipInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShipTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = "선박 조회"

        searchType = intent.getIntExtra("history_category", -1)
        val shipId = intent.getStringExtra("history_shipId")

        if(shipId != null) {
            getTrackingInfo(searchType, shipId.toString())
        }

        db = AppDatabase.getInstance(this)!!

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.ship_search_type,
            R.layout.ship_search_option_spinner
        )
        adapter.setDropDownViewResource(R.layout.ship_search_option_spinner_dropdown)
        binding.activityShipTrackingSpnType.adapter = adapter

        binding.activityShipTrackingSpnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchType = position
                binding.activityShipTrackingEtId.hint = SEARCH_OPTION_NAME[position] + "를 입력하세요."
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.activityShipTrackingBtnSearch.setOnClickListener {

            val shipId = binding.activityShipTrackingEtId.text.toString()

            insertHistory(TrackingHistoryEntity(searchType, shipId))

            getTrackingInfo(searchType, shipId)
        }

        binding.activityShipTrackingBtnCheck.setOnClickListener {
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

    private fun getTrackingInfo(searchType: Int, id: String) {

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
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@ShipTrackingActivity,
                        "서비스를 이용할 수 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (response.body()!!.size == 0) {
                        Toast.makeText(
                            this@ShipTrackingActivity,
                            "48시간 이내에 해당 선박의 위치가 확인되지 않았습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        shipInfo = ShipInfo(response.body()!!)

                        binding.activityShipTrackingTvMmsiResult.text = shipInfo!!.mmsi
                        binding.activityShipTrackingTvTimestampResult.text = shipInfo!!.timestamp
                        binding.activityShipTrackingTvLatitudeResult.text =
                            shipInfo!!.lat.toString()
                        binding.activityShipTrackingTvLongitudeResult.text =
                            shipInfo!!.lon.toString()
                    }
                }
            }

            override fun onFailure(call: Call<TrafficResponseDto>, t: Throwable) {
                Log.d("http", t.message!!)
            }
        })
    }

    private fun insertHistory(trackingHistoryEntity: TrackingHistoryEntity) {
        Thread {
            db.getTrackingHistoryDao().insert(trackingHistoryEntity)
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}