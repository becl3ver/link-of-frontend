package com.example.logisticsestimate

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.logisticsestimate.databinding.ActivityMainBinding
import com.example.logisticsestimate.fragment.*

/**
 * BottomNavigationView를 통해서 프래그먼트 선택 시 화면 전환
 *
 * @author 최재훈
 * @version 1.0 기본 프래그먼트 화면 전환 구현
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mainScreenFragment : EstimateFragment? = EstimateFragment()
    private var termsFragment : TrackingFragment? = TrackingFragment()
    private var communityFragment : CommunityFragment? = CommunityFragment()
    private var myPageFragment : MyPageFragment? = MyPageFragment()
    private var testFragment : TestingFragment? = TestingFragment()

    private lateinit var bar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        supportFragmentManager.beginTransaction().replace(R.id.activity_main_fl, mainScreenFragment!!).commit()

        binding.activityMainMnuSelect.setOnItemSelectedListener { item->
            var nowFragment = when(item.itemId) {
                R.id.select_main_screen -> mainScreenFragment
                R.id.select_terms -> termsFragment
                R.id.select_community -> testFragment //communityFragment
                R.id.select_account -> myPageFragment
                else -> mainScreenFragment
            }

            supportFragmentManager.beginTransaction().replace(R.id.activity_main_fl, nowFragment!!).commit()
            true
        }
    }
}