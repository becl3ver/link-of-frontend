package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.logisticsestimate.databinding.ActivityMainBinding
import com.example.logisticsestimate.fragment.*

/**
 * BottomNavigationView에서 아이템을 누르면, 그에 따라서 프래그먼트를 전환한다.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mainScreenFragment: EstimateFragment = EstimateFragment()
    private var trackingFragment: TrackingFragment = TrackingFragment()
    private var communityFragment: CommunityFragment = CommunityFragment()
    private var myPageFragment: MyPageFragment = MyPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startService(Intent(this, TerminationService::class.java))
        startActivity(Intent(this, LoadingActivity::class.java))

        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_fl, mainScreenFragment).commit()
        supportActionBar!!.title = FRAGMENTS_NAME[0]

        binding.activityMainMnuSelect.setOnItemSelectedListener { item ->
            supportActionBar!!.title = FRAGMENTS_NAME[when(item.itemId) {
                R.id.select_main_screen -> 0
                R.id.select_tracking -> 1
                R.id.select_community -> 2
                else -> 3
            }]

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_fl, when(item.itemId) {
                    R.id.select_main_screen -> mainScreenFragment
                    R.id.select_tracking -> trackingFragment
                    R.id.select_community -> communityFragment
                    else -> myPageFragment
                })
                .commit()
            true
        }
    }

    companion object {
        val FRAGMENTS_NAME = arrayOf("견적", "화물 위치 확인", "커뮤니티", "마이 페이지")
    }
}