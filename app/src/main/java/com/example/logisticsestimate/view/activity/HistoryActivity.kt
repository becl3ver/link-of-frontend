package com.example.logisticsestimate.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.logisticsestimate.R
import com.example.logisticsestimate.databinding.ActivityHistoryBinding
import com.example.logisticsestimate.view.fragment.EstimateHistoryFragment
import com.example.logisticsestimate.view.fragment.TrackingHistoryFragment
import com.google.android.material.tabs.TabLayout

/**
 * 화물 견적과 선박 위치 조회에 대한 기록을 조회
 */
class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding
    private lateinit var estimateTab : EstimateHistoryFragment
    private lateinit var trackingTab : TrackingHistoryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = "검색 기록"

        estimateTab = EstimateHistoryFragment()
        trackingTab = TrackingHistoryFragment()

        supportFragmentManager.beginTransaction().add(R.id.activity_history_frameLayout, estimateTab).commit()

        binding.activityHistoryTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> replaceView(estimateTab)
                    1 -> replaceView(trackingTab)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun replaceView(tab: Fragment) {
        var selectedFragment: Fragment? = null

        selectedFragment = tab
        selectedFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_history_frameLayout, it).commit()
        }
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