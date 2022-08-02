package com.example.logisticsestimate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.databinding.ActivityTermBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TermEntity
import org.json.JSONArray

class TermActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermBinding
    private lateinit var termDB: AppDatabase
    private lateinit var adapter: TermRecyclerViewAdapter

    // Recycler View에 사용될 용어 배열
    var termList = ArrayList<TermEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = "용어 사전"

        // DB 인스턴스
        termDB = AppDatabase.getInstance(this)!!

        /* TODO("초기에 용어가 두개만 로딩되는 문제 해결해야 함") */
        // TermData.json으로부터 데이터를 불러와 RecyclerView에 반영
        getAllTerms()

        // 용어 검색 EditText의 문자열 리스너
        binding.termSearchEt.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getQueryTerms(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        // EditText의 액션 리스너
        binding.termSearchEt.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                var handled = false

                // 검색 버튼을 눌렀을 경우
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handled = true

                    // 활성화된 키보드 내리기
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.termSearchEt.windowToken, 0)
                }

                return handled
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // menuInflater.inflate(R.menu.new_post_menu, menu)
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
    private fun getQueryTerms(query : String) {
        Thread {
            if(query == "") {
                getAllTerms()
            }
            else {
                termList = ArrayList(termDB.getTermDao().getQueryTerms(query))
            }
            setRecyclerView()

        }.start()
    }

    // 모든 용어 데이터 가져오기
    private fun getAllTerms() {
        Thread {
            termList = ArrayList(termDB.getTermDao().getAllTerms())
            setRecyclerView()
        }.start()
    }

    // 데이터베이스에 용어 추가
    private fun addTerm(term : TermEntity) {

    }

    // RecyclerView Refresh
    private fun setRecyclerView() {
        runOnUiThread {
            adapter = TermRecyclerViewAdapter(termList, this)
            binding.rcView.adapter = adapter
            binding.rcView.layoutManager = LinearLayoutManager(this)
        }
    }
}