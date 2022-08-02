package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.logisticsestimate.data.*
import com.example.logisticsestimate.databinding.ActivitySignInBinding
import com.example.logisticsestimate.repository.AccountRetrofitBuilder
import retrofit2.*

/**
 * 로그인 처리를 수행한다.
 */
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "로그인"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        binding.activitySignInBtnSubmit.setOnClickListener {
            val id = binding.activitySignInEtId.text.toString()
            val password = binding.activitySignInEtPw.text.toString()

            val account = AccountSignInDto(id, password)
            val call = AccountRetrofitBuilder.getInstance().getSignIn(account)

            call.enqueue(object: Callback<TokenDto> {
                override fun onResponse(
                    call: Call<TokenDto>,
                    response: Response<TokenDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@SignInActivity, "연결이 비정상적입니다.", Toast.LENGTH_SHORT).show()
                        return;
                    } else {
                        App.prefs.removeAccessToken()
                        App.prefs.setAccessToken(response.body()?.token)
                        Toast.makeText(this@SignInActivity, "응답값 : " + App.prefs.getAccessToken(""), Toast.LENGTH_SHORT).show()
                        Log.d(SignInActivity::class.toString(), "토큰 응답 : " + response.body()?.token)

                        if(binding.activitySignInCbRemember.isChecked) {
                            App.prefs.setString("id", id)
                            App.prefs.setString("password", password)
                        }

                        setResult(RESULT_OK)
                        finish()
                    }
                }

                override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                    Toast.makeText(this@SignInActivity, "연결이 비정상적입니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 비밀번호 찾기를 위한 액티비티를 시작한다.
        binding.activitySignInBtnFind.setOnClickListener {
            val intent = Intent(applicationContext, PasswordResetActivity::class.java)
            startActivity(intent)
        }
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
}