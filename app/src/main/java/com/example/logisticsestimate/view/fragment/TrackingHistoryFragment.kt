package com.example.logisticsestimate.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.TrackingHistoryEntity
import com.example.logisticsestimate.databinding.FragmentTrackingHistoryBinding
import com.example.logisticsestimate.view.activity.HistoryActivity
import com.example.logisticsestimate.view.adapter.TrackingHistoryRecyclerViewAdapter

/**
 * 상단 탭을 통해서 선택된 경우에 선박 위치 조회 기록을 표시하고, 특정 기록을 선택하면 조회
 */
class TrackingHistoryFragment : Fragment() {
    private lateinit var binding : FragmentTrackingHistoryBinding
    private lateinit var db: AppDatabase

    private var trackingHistory = ArrayList<TrackingHistoryEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackingHistoryBinding.inflate(layoutInflater)

        db = AppDatabase.getInstance(requireContext())!!


        val context = context as HistoryActivity

        Thread {
            trackingHistory = ArrayList(db.getTrackingHistoryDao().getAll())

            if (trackingHistory.size != 0) {
                context.runOnUiThread {
                    val adapter =
                        TrackingHistoryRecyclerViewAdapter(trackingHistory, requireContext())

                    binding.fragmentTrackingHistoryRv.adapter = adapter
                    binding.fragmentTrackingHistoryRv.layoutManager =
                        LinearLayoutManager(requireContext())
                }
            } else {
                context.runOnUiThread {Toast.makeText(requireContext(), "검색 기록이 없습니다.", Toast.LENGTH_SHORT).show()}
            }
        }.start()


        return binding.root
    }
}