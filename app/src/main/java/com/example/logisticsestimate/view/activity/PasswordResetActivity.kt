package com.example.logisticsestimate.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.logisticsestimate.R
import com.example.logisticsestimate.data.remote.api.AccountRetrofitClient
import com.example.logisticsestimate.data.remote.model.login.CodeDto
import com.example.logisticsestimate.data.remote.model.login.EmailDto
import com.example.logisticsestimate.data.remote.model.login.EmailTokenDto
import com.example.logisticsestimate.data.remote.model.login.PasswordDto
import com.example.logisticsestimate.databinding.ActivityPasswordResetBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer

class PasswordResetActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPasswordResetBinding

    private var isRunning = false

    private var token: String? = null
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityPasswordResetBtnSend.setOnClickListener(this)
        binding.activityPasswordResetBtnCheck.setOnClickListener(this)
        binding.activityPasswordResetBtnSubmit.setOnClickListener(this)

        binding.activityPasswordResetEtPassword.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s.toString().length

                binding.activityPasswordResetTvPassword.text =
                    if (length < 8 || length > 20) "비밀번호는 8 ~ 20자로 이루어져야 합니다."
                    else if (isSafe(s.toString())) ""
                    else "영문, 숫자, 특수문자를 하나씩 포함해야 합니다."
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        binding.activityPasswordResetEtPasswordCheck.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.activityPasswordResetEtPassword.text.toString() == s.toString()) {
                    binding.activityPasswordResetTvPasswordCheck.text = "비밀번호가 일치합니다."
                    binding.activityPasswordResetTvPasswordCheck.setTextColor(getColor(R.color.green))
                } else {
                    binding.activityPasswordResetTvPasswordCheck.text = "비밀번호가 일치하지 않습니다."
                    binding.activityPasswordResetTvPasswordCheck.setTextColor(getColor(R.color.red))
                }
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            binding.activityPasswordResetBtnSend.id -> {
                startTimer()
                val email = binding.activityPasswordResetEtMail.text.toString()
                if (email == "") {
                    Toast.makeText(this@PasswordResetActivity, "메일 주소를 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                val call = AccountRetrofitClient.getInstance().identifyByEmail(EmailDto(email))

                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (!response.isSuccessful) {
                            Toast.makeText(
                                this@PasswordResetActivity,
                                "오류가 발생했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            binding.activityPasswordResetBtnCheck.isEnabled = true
                            binding.activityPasswordResetBtnCheck.background =
                                ContextCompat.getDrawable(
                                    this@PasswordResetActivity,
                                    R.drawable.all_btn_round_edge
                                )
                            startTimer()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(
                            this@PasswordResetActivity,
                            "연결에 실패했습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.d("http", t.message ?: "onFailure")
                    }
                })
            }

            binding.activityPasswordResetBtnCheck.id -> {
                val code = binding.activityPasswordResetEtCode.text.toString()

                val call = AccountRetrofitClient.getInstance().identificationCode(CodeDto(code))
                call.enqueue(object : Callback<EmailTokenDto> {
                    override fun onResponse(
                        call: Call<EmailTokenDto>,
                        response: Response<EmailTokenDto>
                    ) {
                        if (!response.isSuccessful) {
                            Toast.makeText(
                                this@PasswordResetActivity,
                                "오류가 발생했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            token = response.body()!!.token

                            binding.activityPasswordResetBtnSubmit.isEnabled = true
                            binding.activityPasswordResetBtnSubmit.background =
                                ContextCompat.getDrawable(
                                    this@PasswordResetActivity,
                                    R.drawable.all_btn_round_edge
                                )
                        }
                    }

                    override fun onFailure(call: Call<EmailTokenDto>, t: Throwable) {
                        Toast.makeText(
                            this@PasswordResetActivity,
                            "연결에 실패했습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.d("http", t.message ?: "onFailure")
                    }
                })
            }

            binding.activityPasswordResetBtnSubmit.id -> {
                val password = binding.activityPasswordResetEtPassword.text.toString()

                if (!isSafe(password)) {
                    Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

                if (password != binding.activityPasswordResetEtPasswordCheck.text.toString()) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    return
                }

                val call = AccountRetrofitClient.getInstance()
                    .passwordReset(token!!, PasswordDto(password))
                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (!response.isSuccessful) {
                            Toast.makeText(
                                this@PasswordResetActivity,
                                "오류가 발생했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@PasswordResetActivity,
                                "비밀번호가 변경되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(
                            this@PasswordResetActivity,
                            "연결에 실패했습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Log.d("http", t.message ?: "onFailure")
                    }
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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

    private fun startTimer() {
        if (isRunning) {
            return
        }

        binding.activityPasswordResetBtnSend.isEnabled = false
        binding.activityPasswordResetBtnSend.background =
            ContextCompat.getDrawable(this, R.drawable.all_btn_round_edge_disabled)
        binding.activityPasswordResetBtnCheck.isEnabled = true
        binding.activityPasswordResetBtnCheck.background =
            ContextCompat.getDrawable(this, R.drawable.all_btn_round_edge)
        isRunning = true

        var time = 180
        timer = timer(period = 1000) {
            val minutes = time / 60
            val seconds = time % 60

            runOnUiThread {
                binding.activityPasswordResetTvTimerMinute.text = if(minutes > 0) "$minutes" else "0"
                binding.activityPasswordResetTvTimerSecond.text = if(seconds < 10) ":0$seconds" else "$seconds"
            }

            if(time == 0) {
                runOnUiThread {
                    binding.activityPasswordResetTvTimerMinute.text = ""
                    binding.activityPasswordResetTvTimerSecond.text = "시간이 만료되었습니다. 인증번호를 다시 발송해주세요."
                    binding.activityPasswordResetBtnSend.isEnabled = true
                    binding.activityPasswordResetBtnSend.background =
                        ContextCompat.getDrawable(this@PasswordResetActivity, R.drawable.all_btn_round_edge)
                    binding.activityPasswordResetBtnCheck.isEnabled = false
                    binding.activityPasswordResetBtnCheck.background = ContextCompat.getDrawable(
                        this@PasswordResetActivity,
                        R.drawable.all_btn_round_edge_disabled
                    )
                }
                isRunning = false
                this.cancel()
            }

            time--
        }
    }

    private fun isSafe(str: String): Boolean {
        val regex =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\$@\$!%*#?&])[A-Za-z\\d\$@\$!%*#?&]{8,16}\$".toRegex()
        return regex.matchEntire(str) != null
    }
}