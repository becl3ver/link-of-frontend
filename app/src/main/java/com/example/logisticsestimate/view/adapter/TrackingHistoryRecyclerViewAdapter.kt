package com.example.logisticsestimate.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.TrackingHistoryEntity
import com.example.logisticsestimate.databinding.ItemTrackingHistoryBinding
import com.example.logisticsestimate.view.activity.ShipTrackingActivity
import kotlin.collections.ArrayList

/**
 * 선박 위치 조회 기록을 표시
 */
class TrackingHistoryRecyclerViewAdapter(private var entities : ArrayList<TrackingHistoryEntity>, val context: Context) : RecyclerView.Adapter<TrackingHistoryRecyclerViewAdapter.TrackingHistoryViewHolder>() {
    inner class TrackingHistoryViewHolder(val binding: ItemTrackingHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    val db = AppDatabase.getInstance(context)!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackingHistoryViewHolder {

        val binding : ItemTrackingHistoryBinding = ItemTrackingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrackingHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackingHistoryViewHolder, position: Int) {
        val curData = entities[position]

        holder.binding.itemTrackingHistoryTvCategory.text = when(curData.category) {
            0 -> "선박 ID"
            1 -> "IMO"
            else -> "MMSI"
        }


        holder.binding.itemTrackingHistoryTvContent.text = curData.shipId

        holder.binding.itemTrackingHistoryContainer.setOnClickListener {view ->
            view.context.startActivity(Intent(context, ShipTrackingActivity::class.java).let {
                it.putExtra("history_category", entities[holder.adapterPosition].category)
                it.putExtra("history_shipId", entities[holder.adapterPosition].shipId)
            })
        }

        holder.binding.itemTrackingHistoryContainer.setOnLongClickListener {

            AlertDialog.Builder(context).let {
                it.setMessage("기록을삭제하시겠습니까?")
                it.setPositiveButton("네") { _, _ ->
                    val entity = entities[holder.adapterPosition]

                    Thread {
                        db.getTrackingHistoryDao().delete(entity)
                    }.start()

                    entities.removeAt(holder.adapterPosition)
                    notifyDataSetChanged()
                }
                it.setNegativeButton("아니오") { _, _ -> }
                it.create()
                it.show()
            }

            return@setOnLongClickListener true
        }
    }



    override fun getItemCount() = entities.size
}