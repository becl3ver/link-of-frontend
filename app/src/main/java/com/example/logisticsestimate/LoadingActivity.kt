package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.logisticsestimate.data.AccountSignInDto
import com.example.logisticsestimate.data.TokenDto
import com.example.logisticsestimate.databinding.ActivityLoadingBinding
import com.example.logisticsestimate.db.AppDatabase
import com.example.logisticsestimate.db.TermEntity
import com.example.logisticsestimate.repository.AccountRetrofitBuilder
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 앱 시작 시 로딩 화면을 2초간 표시한다.
 */
class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        startLoadingScreen()
        getJsonData("TermData.json")

        val id = App.prefs.getString("id", "")
        val password = App.prefs.getString("password", "")

        val account = AccountSignInDto(id, password)
        val call = AccountRetrofitBuilder.getInstance().getSignIn(account)

        if(id != "" && password != "") {
            call.enqueue(object: Callback<TokenDto> {
                override fun onResponse(
                    call: Call<TokenDto>,
                    response: Response<TokenDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@LoadingActivity, "연결이 비정상적입니다.", Toast.LENGTH_SHORT).show()
                        return;
                    } else {
                        App.prefs.setAccessToken(response.body()?.token)
                        Toast.makeText(this@LoadingActivity, "응답값 : " + App.prefs.getAccessToken(""), Toast.LENGTH_SHORT).show()

                        setResult(RESULT_OK)
                        finish()
                    }
                }

                override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                    Toast.makeText(this@LoadingActivity, "연결이 비정상적입니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun startLoadingScreen() {
        Handler(Looper.getMainLooper()).postDelayed({finish()}, 2000)
    }

    // json 파일로부터 데이터 불러오기
    private fun getJsonData(filename: String) {

        val jsonString = assets.open(filename).reader().readText()

        val jsonArray = JSONArray(jsonString)

        val database = AppDatabase.getInstance(this)
        val termDao = database!!.getTermDao()

        Thread {
            for(index in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(index)

                val name = jsonObject.getString("name")
                val content = jsonObject.getString("content")
                database.getTermDao().addTerm(TermEntity(index, name, content))
            }
        }.start()
    }
}