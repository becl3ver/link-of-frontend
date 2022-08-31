package com.example.logisticsestimate.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.EstimateHistoryEntity
import com.example.logisticsestimate.data.db.entity.TrackingHistoryEntity
import com.example.logisticsestimate.databinding.FragmentEstimateHistoryBinding
import com.example.logisticsestimate.view.activity.HistoryActivity
import com.example.logisticsestimate.view.adapter.EstimateHistoryRecyclerViewAdapter
import com.example.logisticsestimate.view.adapter.TrackingHistoryRecyclerViewAdapter

/**
 * 상단탭을 통해서 선택된 경우에 견적 조회 기록을 표시하고, 특정 기록을 선택하면 조회
 */
class EstimateHistoryFragment : Fragment() {
    private lateinit var binding : FragmentEstimateHistoryBinding
    private lateinit var db: AppDatabase

    private var estimateHistory = ArrayList<EstimateHistoryEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstimateHistoryBinding.inflate(layoutInflater)

        db = AppDatabase.getInstance(requireContext())!!

        val context = context as HistoryActivity

        Thread {
            estimateHistory = ArrayList(db.getEstimateHistoryDao().getAll())

            if (estimateHistory.size != 0) {
                context.runOnUiThread {
                    val adapter =
                        EstimateHistoryRecyclerViewAdapter(estimateHistory, requireContext())

                    binding.fragmentEstimateHistoryRv.adapter = adapter
                    binding.fragmentEstimateHistoryRv.layoutManager =
                        LinearLayoutManager(requireContext())
                }
            } else {
                context.runOnUiThread {Toast.makeText(requireContext(), "검색 기록이 없습니다.", Toast.LENGTH_SHORT).show()}
            }
        }.start()

        return binding.root
    }
}