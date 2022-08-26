package com.example.logisticsestimate.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.view.activity.NewBoardActivity
import com.example.logisticsestimate.databinding.ItemTemporaryBinding
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.TemporaryEntity
import com.example.logisticsestimate.view.fragment.CommunityFragment

/**
 * 임시 글 목록 표시, onClick 처리
 */
class TemporaryRecyclerViewAdapter(
    private val entities: ArrayList<TemporaryEntity>,
    private val context: Context
    ): RecyclerView.Adapter<TemporaryRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemTemporaryBinding): RecyclerView.ViewHolder(binding.root) {
        private var category = -1
        private var title = ""
        private var content = ""

        fun setData(entity: TemporaryEntity) {
            category = entity.category
            title = entity.title
            content = entity.content

            binding.itemTemporaryTvTitle.text = title
            binding.itemTemporaryTvContent.text = content
            binding.itemTemporaryTvCategory.text = CommunityFragment.BOARD_NAME[category]

            binding.itemTemporaryTvDate.text = entity.date

            binding.itemTemporaryCl.setOnClickListener { view ->
                view.context.startActivity(Intent(context, NewBoardActivity::class.java).let {
                    it.putExtra("title", title)
                    it.putExtra("content", content)
                    it.putExtra("category", category)
                })
            }

            binding.itemTemporaryCl.setOnLongClickListener {
                AlertDialog.Builder(context).let {
                    it.setMessage("삭제하시겠습니까?")
                    it.setPositiveButton("네") { _, _ ->
                        val db = AppDatabase.getInstance(context)!!
                        val temporaryPostDao = db.getTemporaryPostDao()
                        val entity = entities[adapterPosition]

                        Thread {
                            temporaryPostDao.delete(entity)
                        }.start()

                        Log.d(TemporaryRecyclerViewAdapter::class.java.name, adapterPosition.toString())
                        entities.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }
                    it.setNegativeButton("아니오") { _, _ -> }
                    it.create()
                    it.show()
                }

                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entity = entities[position]
        holder.setData(entity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTemporaryBinding = ItemTemporaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}