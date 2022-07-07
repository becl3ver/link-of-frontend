package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.databinding.ActivityTemporaryPostBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TemporaryPostDao
import com.example.logisticsestimate.db.TemporaryPostEntity

class TemporaryPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTemporaryPostBinding

    private lateinit var db : AppDatabase
    private lateinit var postDao: TemporaryPostDao
    private lateinit var postDatas : ArrayList<TemporaryPostEntity>
    private lateinit var adapter: TemporaryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTemporaryPostBinding.inflate(layoutInflater)

        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        postDao = db.getTemporaryPostDado()

        getAllTemporaryPost()
    }

    private fun getAllTemporaryPost() {
        Thread {
            postDatas = ArrayList(postDao.getAll())
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        runOnUiThread {
            adapter = TemporaryRecyclerViewAdapter(postDatas, this)
            binding.activityTemporaryRvList.adapter = adapter
            binding.activityTemporaryRvList.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        getAllTemporaryPost()
    }

    companion object {
        val TAG = TemporaryPostActivity::class.java.simpleName
    }
}