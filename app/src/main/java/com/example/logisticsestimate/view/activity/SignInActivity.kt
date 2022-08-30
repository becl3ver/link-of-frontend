package com.example.logisticsestimate.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.logisticsestimate.R
import com.example.logisticsestimate.base.App
import com.example.logisticsestimate.data.remote.model.login.AccountSignInDto
import com.example.logisticsestimate.data.remote.model.login.TokenDto
import com.example.logisticsestimate.databinding.ActivitySignInBinding
import com.example.logisticsestimate.data.remote.api.AccountRetrofitClient
import retrofit2.*

/**
 * 로그인 처리
 */
class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "로그인"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        binding.activitySignInEtId.setText(App.prefs.getString("id", ""))
        binding.activitySignInEtPw.setText(App.prefs.getString("password", ""))


        binding.activitySignInBtnSubmit.setOnClickListener {
            val id = binding.activitySignInEtId.text.toString()
            val password = binding.activitySignInEtPw.text.toString()

            val account = AccountSignInDto(id, password)
            val call = AccountRetrofitClient.getInstance().signIn(account)

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
                        App.prefs.removeUid()
                        App.prefs.removeNickname()

                        App.prefs.setAccessToken(response.body()!!.token)
                        App.prefs.setUid(response.body()!!.uid)
                        App.prefs.setNickname(response.body()!!.nickname)

                        if(binding.activitySignInCbKeep.isChecked) {
                            App.prefs.setString("id", id)
                            App.prefs.setString("password", password)
                        } else {
                            App.prefs.removeString("id")
                            App.prefs.removeString("password")
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

        binding.activitySignInBtnFind.setOnClickListener {
            startActivity(Intent(this, PasswordResetActivity::class.java))
        }

        binding.activitySignInBtnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
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
}