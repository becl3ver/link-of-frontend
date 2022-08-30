package com.example.logisticsestimate.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.logisticsestimate.base.App
import com.example.logisticsestimate.data.remote.model.login.AccountSignInDto
import com.example.logisticsestimate.data.remote.model.login.TokenDto
import com.example.logisticsestimate.databinding.ActivityLoadingBinding
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.TermEntity
import com.example.logisticsestimate.data.remote.api.AccountRetrofitClient
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 로딩 화면 표시, 자동 로그인, 용어 사전 초기화
 */
class LoadingActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        startLoadingScreen()
        getJsonData()

        val id = App.prefs.getString("id", "")
        val password = App.prefs.getString("password", "")

        val account = AccountSignInDto(id, password)
        val call = AccountRetrofitClient.getInstance().signIn(account)

        if(id != "" && password != "") {
            call.enqueue(object: Callback<TokenDto> {
                override fun onResponse(
                    call: Call<TokenDto>,
                    response: Response<TokenDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@LoadingActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        App.prefs.removeLoginData()
                        App.prefs.setLoginData(response.body()!!)

                        isLoggedIn = true
                        setResult(RESULT_OK)
                    }
                }

                override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                    Toast.makeText(this@LoadingActivity, "연결이 비정상적입니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun startLoadingScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
            if (isLoggedIn) {
                Toast.makeText(this@LoadingActivity, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }, 2000)
    }

    // json 파일로부터 데이터 불러오기
    private fun getJsonData() {
        val jsonString = assets.open("TermData.json").reader().readText()
        val jsonArray = JSONArray(jsonString)
        val db = AppDatabase.getInstance(this)

        Thread {
            for(index in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(index)

                val name = jsonObject.getString("name")
                val content = jsonObject.getString("content")
                db!!.getTermDao().insert(TermEntity(index, name, content))
            }
        }.start()
    }
}