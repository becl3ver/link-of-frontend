package com.example.logisticsestimate.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.logisticsestimate.utils.FreightCBMCalculator
import com.example.logisticsestimate.R
import com.example.logisticsestimate.databinding.ActivityCbmBinding

/**
 * 사용자의 입력을 바탕으로 CBM 계산기 기능 수행
 */
class CBMActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCbmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCbmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "CBM 계산기"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        binding.activityCbmBtnCalculate.setOnClickListener {
            val length = binding.activityCbmEtLength.text.toString().toDouble()
            val width = binding.activityCbmEtWidth.text.toString().toDouble()
            val height = binding.activityCbmEtHeight.text.toString().toDouble()
            val weight = binding.activityCbmEtWeight.text.toString().toDouble()
            val num = binding.activityCbmEtNum.text.toString().toInt()
            val unit = when(binding.activityCbmRgUnit.checkedRadioButtonId) {
                R.id.activity_cbm_rb_unit_1 -> 0
                R.id.activity_cbm_rb_unit_2 -> 1
                else -> 3
            }

            val calculator = FreightCBMCalculator(length, width, height, weight, num, unit)

            if(!calculator.checkAllRightInput()) {
                Toast.makeText(this, "입력을 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.activityCbmTvCbmResult.text = calculator.cbmCalculator().toString()

            val resultTier1 = calculator.containerPerFreightOneFun()
            val resultTier2 = calculator.containerPerFreightTwoFun()

            binding.activityCbmTv1.text = resultTier1[0].toString()
            binding.activityCbmTv2.text = resultTier1[1].toString()
            binding.activityCbmTv3.text = resultTier1[2].toString()

            binding.activityCbmTv4.text = resultTier2[0].toString()
            binding.activityCbmTv5.text = resultTier2[1].toString()
            binding.activityCbmTv6.text = resultTier2[2].toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}