package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.databinding.ActivityTemporaryPostBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TemporaryDao
import com.example.logisticsestimate.db.TemporaryEntity

/**
 * 작성을 중단한 임시글 목록을 Room 지속성 라이브러리에서 읽어와서 보여준다.
 */
class TemporaryBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTemporaryPostBinding

    private lateinit var db : AppDatabase
    private lateinit var temporaryDao: TemporaryDao
    private lateinit var entities : ArrayList<TemporaryEntity>
    private lateinit var adapter: TemporaryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTemporaryPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = "임시 저장 글"

        db = AppDatabase.getInstance(this)!!
        temporaryDao = db.getTemporaryPostDao()

        getAllTemporaryPost()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getAllTemporaryPost() {
        Thread {
            entities = ArrayList(temporaryDao.getAll())
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        runOnUiThread {
            adapter = TemporaryRecyclerViewAdapter(entities, this)

            binding.activityTemporaryRvList.adapter = adapter
            binding.activityTemporaryRvList.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        getAllTemporaryPost()
    }
}