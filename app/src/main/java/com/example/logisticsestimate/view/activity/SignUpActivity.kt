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
import com.example.logisticsestimate.data.remote.model.login.*
import com.example.logisticsestimate.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer

/**
 * 회원가입 기능 수행
 */
class SignUpActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySignUpBinding
    private var isDuplicationChecked = false
    private var authToken = ""
    private var submitToken: String? = null
    private var isRunning = false
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "회원 가입"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        binding.activitySignInEtId.text.isEmpty()

        binding.activitySignUpBtnIdCheck.setOnClickListener(this)
        binding.activitySignUpBtnMailCheck.setOnClickListener(this)
        binding.activitySignUpBtnMailCodeCheck.setOnClickListener(this)
        binding.activitySignUpBtnSubmit.setOnClickListener(this)

        binding.activitySignInEtId.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(isSafe(1, s.toString())) {
                    binding.activitySignInTvId.text = ""
                } else {
                    binding.activitySignInTvId.text = "ID는 5 ~ 20자의 영소문자, 숫자로 이루어져야 합니다. "
                }
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.activitySignInEtNickname.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.activitySignInEtPassword.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val length = s.toString().length

                binding.activitySignInTvPassword.text =
                    if (length < 8 || length > 20) "비밀번호는 8 ~ 20자로 이루어져야 합니다."
                    else if (isSafe(2, s.toString())) ""
                    else "영문, 숫자, 특수문자를 하나씩 포함해야 합니다."
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.activitySignInEtPasswordCheck.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.activitySignInEtPassword.text.toString() == s.toString()) {
                    binding.activitySignInTvPasswordCheck.text = "비밀번호가 일치합니다."
                    binding.activitySignInTvPasswordCheck.setTextColor(getColor(R.color.green))
                } else {
                    binding.activitySignInTvPasswordCheck.text = "비밀번호가 일치하지 않습니다."
                    binding.activitySignInTvPasswordCheck.setTextColor(getColor(R.color.red))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            binding.activitySignUpBtnIdCheck.id -> {
                val id = binding.activitySignInEtId.text.toString()
                val nickname = binding.activitySignInEtNickname.text.toString()

                if(!isSafe(1, id)) {
                    Toast.makeText(this, "ID를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

                val request = AccountRequestDto(id, "", "", nickname)
                val call = AccountRetrofitClient.getInstance().checkDuplication(request)

                call.enqueue(object: Callback<AccountResponseDto> {
                    override fun onResponse(
                        call: Call<AccountResponseDto>,
                        response: Response<AccountResponseDto>
                    ) {
                        if(!response.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            val str: String = when(response.body()?.result) {
                                "id" -> "이미 사용 중인 ID입니다."
                                "nickname" -> "이미 사용 중인 닉네임입니다."
                                else -> {
                                    isDuplicationChecked = true
                                    binding.activitySignUpBtnIdCheck.isEnabled = true
                                    binding.activitySignUpBtnIdCheck.background =
                                        ContextCompat.getDrawable(
                                            this@SignUpActivity,
                                            R.drawable.all_btn_round_edge_disabled
                                        )
                                    binding.activitySignInEtId.isEnabled = false
                                    binding.activitySignInEtNickname.isEnabled = false
                                    "사용 가능한 ID와 닉네임입니다."
                                }
                            }

                            Toast.makeText(this@SignUpActivity, str, Toast.LENGTH_SHORT).show()
                            checkSubmitButton()
                        }
                    }

                    override fun onFailure(call: Call<AccountResponseDto>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("http", t.message ?: "onFailure")
                    }
                })
            }

            binding.activitySignUpBtnMailCheck.id -> {
                val email = binding.activitySignUpEtMail.text.toString()
                if(email == "") {
                    Toast.makeText(this@SignUpActivity, "메일 주소를 입력해주세요", Toast.LENGTH_SHORT).show()
                    return
                }

                startTimer()
                val call = AccountRetrofitClient.getInstance().checkEmail(EmailDto(email))

                call.enqueue(object: Callback<EmailTokenDto> {
                    override fun onResponse(call: Call<EmailTokenDto>, response: Response<EmailTokenDto>) {
                        if(!response.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            authToken = "Bearer " + response.body()!!.token
                            binding.activitySignUpBtnMailCheck.background = ContextCompat.getDrawable(
                                this@SignUpActivity,
                                R.drawable.all_btn_round_edge
                            )
                            binding.activitySignUpBtnMailCheck.isEnabled = true
                        }
                    }

                    override fun onFailure(call: Call<EmailTokenDto>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "연결에 실패했습니다", Toast.LENGTH_SHORT).show()
                        Log.d("http", t.message ?: "onFailure")
                    }
                })
            }

            binding.activitySignUpBtnMailCodeCheck.id -> {
                val code = binding.activitySignUpEtMailCode.text.toString()
                val call = AccountRetrofitClient.getInstance().checkEmailCode(authToken, CodeDto(code))

                call.enqueue(object: Callback<EmailTokenDto> {
                    override fun onResponse(call: Call<EmailTokenDto>, response: Response<EmailTokenDto>) {
                        if(!response.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            submitToken = "Bearer " + response.body()!!.token
                            binding.activitySignUpTvTimerMinute.text = ""
                            binding.activitySignUpTvTimerSecond.text = "인증이 완료되었습니다."
                            timer?.cancel()
                            checkSubmitButton()
                        }
                    }

                    override fun onFailure(call: Call<EmailTokenDto>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("http", t.message ?: "onFailure")
                    }
                })
            }

            binding.activitySignUpBtnSubmit.id -> {
                val id = binding.activitySignInEtId.text.toString()
                val name = binding.activitySignInEtName.text.toString()
                val password = binding.activitySignInEtPassword.text.toString()
                val nickname = binding.activitySignInEtNickname.text.toString()

                if(!isSafe(2, password)) {
                    Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

                if(password != binding.activitySignInTvPasswordCheck.toString()) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    return
                }
                
                if(nickname.length > 10 || name.length > 10) {
                    Toast.makeText(this, "이름과 별명은 10자 이내로 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

                val request = AccountRequestDto(id, "", password, nickname)
                val call = AccountRetrofitClient.getInstance().signUp(submitToken!!, request)

                call.enqueue(object: Callback<AccountResponseDto> {
                    override fun onResponse(
                        call: Call<AccountResponseDto>,
                        response: Response<AccountResponseDto>
                    ) {
                        if(!response.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            if(response.body()?.result == "failed") {
                                Toast.makeText(this@SignUpActivity, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@SignUpActivity, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<AccountResponseDto>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("http", t.message ?: "onFailure")
                    }
                })
            }
        }
    }

    private fun startTimer() {
        if (isRunning) {
            return
        }

        binding.activitySignUpBtnMailCheck.isEnabled = false
        binding.activitySignUpBtnMailCheck.background =
            ContextCompat.getDrawable(this, R.drawable.all_btn_round_edge_disabled)
        binding.activitySignUpBtnMailCodeCheck.isEnabled = true
        binding.activitySignUpBtnMailCodeCheck.background =
            ContextCompat.getDrawable(this, R.drawable.all_btn_round_edge)
        isRunning = true

        var time = 180
        timer = timer(period = 1000) {
            val minutes = time / 60
            val seconds = time % 60

            runOnUiThread {
                binding.activitySignUpTvTimerMinute.text = if(minutes > 0) "$minutes" else "0"
                binding.activitySignUpTvTimerSecond.text = if(seconds < 10) ":0$seconds" else ":$seconds"
            }

            if(time == 0) {
                runOnUiThread {
                    binding.activitySignUpTvTimerMinute.text = ""
                    binding.activitySignUpTvTimerSecond.text = "시간이 만료되었습니다. 인증번호를 다시 발송해주세요."
                    binding.activitySignUpBtnMailCheck.isEnabled = true
                    binding.activitySignUpBtnMailCheck.background = ContextCompat.getDrawable(
                        this@SignUpActivity,
                        R.drawable.all_btn_round_edge
                    )
                    binding.activitySignUpBtnMailCodeCheck.isEnabled = false
                    binding.activitySignUpBtnMailCodeCheck.background = ContextCompat.getDrawable(
                        this@SignUpActivity,
                        R.drawable.all_btn_round_edge_disabled
                    )
                }
                isRunning = false
                this.cancel()
            }

            time--
        }
    }

    private fun checkSubmitButton() {
        if(isDuplicationChecked && submitToken != null) {
            binding.activitySignUpBtnSubmit.background = ContextCompat.getDrawable(
                this@SignUpActivity,
                R.drawable.all_btn_round_edge
            )
            binding.activitySignUpBtnSubmit.isEnabled = true
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

    private fun isSafe(option: Int, str: String): Boolean {
        val regex = when (option) {
            // 영어 소문자로 시작, 소문자, 숫자 허용, 5 ~ 20글자
            1 -> "^[a-z][a-z0-9]{4,19}$".toRegex()
            // 영어, 숫자, 특수문자 하나씩은 조합 8 ~ 16
            else -> "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\$@\$!%*#?&])[A-Za-z\\d\$@\$!%*#?&]{8,16}\$".toRegex()
        }

        return regex.matchEntire(str) != null
    }
}