package com.example.logisticsestimate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.databinding.ItemTemporaryBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TemporaryEntity
import com.example.logisticsestimate.fragment.CommunityFragment

class TemporaryRecyclerViewAdapter(private val entities : ArrayList<TemporaryEntity>, private val context: Context)
    :RecyclerView.Adapter<TemporaryRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemTemporaryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val container = binding.itemTemporaryCl
        private val title = binding.itemTemporaryTvTitle
        private val content = binding.itemTemporaryTvContent
        private val date = binding.itemTemporaryTvDate
        private val category = binding.itemTemporaryTvCategory

        private var _category = -1
        private var _title = ""
        private var _content = ""

        fun setData(entity: TemporaryEntity) {
            _category = entity.category
            _title = entity.title
            _content = entity.content

            title.text = _title
            content.text = _content
            category.text = CommunityFragment.BOARD_NAME[_category]

            date.text = entity.date

            container.setOnClickListener { view ->
                view.context.startActivity(Intent(context, NewPostActivity::class.java).let {
                    it.putExtra("title", _title)
                    it.putExtra("content", _content)
                    it.putExtra("category", _category)
                })
            }

            container.setOnLongClickListener {
                AlertDialog.Builder(context).let {
                    it.setTitle("알림")
                    it.setMessage("삭제하시겠습니까?")
                    it.setPositiveButton("네") { _, _ ->
                        val db = AppDatabase.getInstance(context)!!
                        val temporaryPostDao = db.getTemporaryPostDao()
                        val entity = entities[adapterPosition]

                        Thread {
                            temporaryPostDao.deleteEntity(entity)
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