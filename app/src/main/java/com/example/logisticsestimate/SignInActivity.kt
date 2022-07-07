package com.example.logisticsestimate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.logisticsestimate.data.*
import com.example.logisticsestimate.databinding.ActivitySignInBinding
import com.example.logisticsestimate.repository.AccountRetrofitBuilder
import retrofit2.*

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activitySignInBtnSubmit.setOnClickListener {
            val id = binding.activitySignInEtId.text.toString()
            val password = binding.activitySignInEtPw.text.toString()

            if(!SignUpActivity.isSafe(1, id) || !SignUpActivity.isSafe(2, password)) {
                printToast("ID 또는 PW를 확인해주세요")
                return@setOnClickListener
            }

            val account = AccountSignInDto(id, password)
            val call = AccountRetrofitBuilder.getInstance().getSignIn(account)

            call.enqueue(object: Callback<TokenDto> {
                override fun onResponse(
                    call: Call<TokenDto>,
                    response: Response<TokenDto>
                ) {
                    if(!response.isSuccessful) {
                        printToast("연결이 비정상적")
                        return;
                    } else {
                        App.prefs.setAccessToken(response.body()?.token)
                        printToast("응답값 : " + App.prefs.getAccessToken(""))
                        Log.d(SignInActivity::class.toString(), "토큰 응답 : " + App.prefs.getAccessToken(""))

                        setResult(RESULT_OK)
                        finish()
                    }
                }

                override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                    printToast("연결 실패")
                }
            })
        }

        binding.activitySignInBtnFind.setOnClickListener {
            val intent = Intent(applicationContext, PasswordResetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun printToast(str : String) {
        Toast.makeText(this@SignInActivity, str, Toast.LENGTH_SHORT).show()
    }
}