package com.example.logisticsestimate

import android.app.ActionBar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.logisticsestimate.databinding.ActivityMainBinding
import com.example.logisticsestimate.fragment.*

/**
 * BottomNavigationView에서 아이템을 누르면, 그에 따라서 프래그먼트를 전환한다.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mainScreenFragment: EstimateFragment? = EstimateFragment()
    private var termsFragment: TrackingFragment? = TrackingFragment()
    private var communityFragment: CommunityFragment? = CommunityFragment()
    private var myPageFragment: MyPageFragment? = MyPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_fl, mainScreenFragment!!).commit()
        supportActionBar!!.title = FRAGMENTS_NAME[0]

        binding.activityMainMnuSelect.setOnItemSelectedListener { item ->
            var nowFragment : Fragment?
            var position : Int?

            when (item.itemId) {
                R.id.select_main_screen -> {
                    nowFragment = mainScreenFragment; position = 0
                }
                R.id.select_terms -> {
                    nowFragment = termsFragment; position = 1
                }
                R.id.select_community -> {
                    nowFragment = communityFragment; position = 2
                }
                R.id.select_account -> {
                    nowFragment = myPageFragment; position = 3
                }
                else -> {
                    nowFragment = mainScreenFragment; position = 0
                }
            }

            supportActionBar!!.title = FRAGMENTS_NAME[position ?: 0]
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_fl, nowFragment!!)
                .commit()
            true
        }
    }

    companion object {
        val FRAGMENTS_NAME = arrayOf("견적", "화물 위치 확인", "커뮤니티", "마이 페이지")
    }
}