package com.example.logisticsestimate.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.R
import com.example.logisticsestimate.view.adapter.TermRecyclerViewAdapter
import com.example.logisticsestimate.databinding.ActivityTermBinding
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.TermEntity

/**
 * 용어 검색 기능
 */
class TermActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTermBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: TermRecyclerViewAdapter

    // Recycler View에 사용될 용어 배열
    var entities = ArrayList<TermEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = "용어 사전"

        // DB 인스턴스
        db = AppDatabase.getInstance(this)!!

        // TermData.json으로부터 데이터를 불러와 RecyclerView에 반영
        getAllTerms()

        // 용어 검색 EditText의 문자열 리스너
        binding.activityTermEtSearch.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getQueryTerms(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        // EditText의 액션 리스너
        binding.activityTermEtSearch.setOnEditorActionListener { _, actionId, _ ->
            var handled = false

            // 검색 버튼을 눌렀을 경우
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handled = true

                // 활성화된 키보드 내리기
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.activityTermEtSearch.windowToken, 0)
            }

            handled
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // 요청한 키워드를 포함한 용어 검색
    private fun getQueryTerms(query: String) {
        Thread {
            if(query == "") {
                getAllTerms()
            }
            else {
                entities = ArrayList(db.getTermDao().getAllByTitle(query))
            }
            setRecyclerView()

        }.start()
    }

    // 모든 용어 데이터 가져오기
    private fun getAllTerms() {
        Thread {
            entities = ArrayList(db.getTermDao().getAll())
            setRecyclerView()
        }.start()
    }

    // RecyclerView Refresh
    private fun setRecyclerView() {
        runOnUiThread {
            adapter = TermRecyclerViewAdapter(entities, this)
            binding.activityTermRv.adapter = adapter
            binding.activityTermRv.layoutManager = LinearLayoutManager(this)
        }
    }
}