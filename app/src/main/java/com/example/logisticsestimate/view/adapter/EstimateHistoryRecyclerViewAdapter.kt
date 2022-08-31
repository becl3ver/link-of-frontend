package com.example.logisticsestimate.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.EstimateHistoryEntity
import com.example.logisticsestimate.data.remote.api.EstimateRetrofitClient
import com.example.logisticsestimate.data.remote.model.estimate.EstimateRequestDto
import com.example.logisticsestimate.data.remote.model.estimate.EstimateResponseDto
import com.example.logisticsestimate.databinding.ItemEstimateHistoryBinding
import com.example.logisticsestimate.view.activity.EstimateActivity
import com.example.logisticsestimate.view.activity.HistoryActivity
import com.example.logisticsestimate.view.activity.ShipTrackingActivity
import com.example.logisticsestimate.view.fragment.EstimateFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * 견적 조회 기록들을 읽어와서 표시
 */
class EstimateHistoryRecyclerViewAdapter (private var entities : ArrayList<EstimateHistoryEntity>, val context: Context): RecyclerView.Adapter<EstimateHistoryRecyclerViewAdapter.EstimateHistoryViewHolder>() {
    inner class EstimateHistoryViewHolder(val binding: ItemEstimateHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    val db = AppDatabase.getInstance(context)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstimateHistoryViewHolder {
        val binding: ItemEstimateHistoryBinding = ItemEstimateHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return EstimateHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EstimateHistoryViewHolder, position: Int) {
        val curData = entities[position]

        holder.binding.itemEstimateHistoryTvAnnNum.text = "공표 번호: " + curData.annNumber
        holder.binding.itemEstimateHistoryTvAnnRuteName.text = "공표 항구명: " + curData.annRuteName
        holder.binding.itemEstimateHistoryTvShippingPortName.text = "선적지 항구명: " + curData.shippingPortName
        holder.binding.itemEstimateHistoryTvLandingPortName.text = "양하지 항구명: " + curData.landingPortName
        holder.binding.itemEstimateHistoryTvCodPortName.text = "양륙지 항구명: " + curData.codPortName

        holder.binding.itemEstimateHistoryContainer.setOnClickListener {
            val holderPosition = entities[holder.adapterPosition]

            val call =
                EstimateRetrofitClient.getInstance().requestEstimateResult(EstimateRequestDto(
                    holderPosition.annNumber,
                    holderPosition.annRuteName,
                    holderPosition.shippingPortName,
                    holderPosition.landingPortName,
                    holderPosition.codPortName,
                    holderPosition.transshipmentYn,
                    holderPosition.containerOwnSeName,
                    holderPosition.containerCndName,
                    holderPosition.containerStdStndrdName,
                    holderPosition.freightName,
                    holderPosition.tnSpotSeName
                ))

            call.enqueue(object : Callback<EstimateResponseDto> {
                override fun onResponse(
                    call: Call<EstimateResponseDto>,
                    response: Response<EstimateResponseDto>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(context, "입력값이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "조회에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                        val result = response.body()!!
                        val intent = Intent(context, EstimateActivity::class.java)


                        intent.putExtra("estimateResult", result)

                        context.startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<EstimateResponseDto>, t: Throwable) {
                    Log.d("HTTP", "onFailure: " + t.message!!)
                    Toast.makeText(context, "연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        holder.binding.itemEstimateHistoryContainer.setOnLongClickListener {

            AlertDialog.Builder(context).let {
                it.setMessage("기록을삭제하시겠습니까?")
                it.setPositiveButton("네") { _, _ ->
                    val entity = entities[holder.adapterPosition]

                    Thread {
                        db.getEstimateHistoryDao().delete(entity)
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