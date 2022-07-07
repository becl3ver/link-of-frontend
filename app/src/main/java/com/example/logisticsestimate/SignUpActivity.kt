package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.logisticsestimate.data.AccountRequestDto
import com.example.logisticsestimate.data.AccountResponseDto
import com.example.logisticsestimate.databinding.ActivitySignUpBinding
import com.example.logisticsestimate.repository.AccountRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.activitySignUpBtnIdCheck.setOnClickListener {
            val id = binding.activitySignInEtId.text.toString()
            val nickname = binding.activitySignInEtNickname.text.toString()

            if(!isSafe(1, id)) {
                Toast.makeText(this, "ID를 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val account = AccountRequestDto(id, "", "", nickname)
            val call = AccountRetrofitBuilder.getInstance().getWithdraw(account)

            call.enqueue(object : Callback<AccountResponseDto> {
                override fun onResponse(
                    call: Call<AccountResponseDto>,
                    response: Response<AccountResponseDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        var string : String = when(response.body()?.result) {
                            "id" -> "이미 사용 중인 ID입니다."
                            "nickname" -> "이미 사용 중인 닉네임입니다."
                            else -> "사용 가능한 ID와 닉네임입니다."
                        }
                        Toast.makeText(this@SignUpActivity, string, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AccountResponseDto>, t: Throwable) {
                    Toast.makeText(this@SignUpActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.activitySignUpBtnSubmit.setOnClickListener {
            val id = binding.activitySignInEtId.text.toString()
            val nickname = binding.activitySignInEtNickname.text.toString()
            val password = binding.activitySignInEtPassword.text.toString()

            if(!isSafe(1, id) || !isSafe(2, password)) {
                Toast.makeText(this, "ID 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val account = AccountRequestDto(id, password, "", nickname)
            val call = AccountRetrofitBuilder.getInstance().getSignUp(account)

            call.enqueue(object : Callback<AccountResponseDto> {
                override fun onResponse(
                    call: Call<AccountResponseDto>,
                    response: Response<AccountResponseDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
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
                }
            })
        }

        binding.activitySignInEtId.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(isSafe(1, p0.toString())) {
                    binding.activitySignInTvId.text = ""
                } else {
                    binding.activitySignInTvId.text = "ID는 5 ~ 20자의 영소문자, 숫자로 이루어져야 합니다. "
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.activitySignInEtNickname.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.activitySignInEtPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val length = p0.toString().length

                if(length < 8 || length > 20) {
                    binding.activitySignInTvPassword.text = "비밀번호는 8 ~ 20자로 이루어져야 합니다."
                }
                else if(isSafe(2, p0.toString())) {
                    binding.activitySignInTvPassword.text = ""
                } else {
                    binding.activitySignInTvPassword.text = "영문, 숫자, 특수문자를 하나씩 포함해야 합니다."
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.activitySignInEtPasswordCheck.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.activitySignInEtPassword.text.toString() == p0.toString()) {
                    binding.activitySignInTvPasswordCheck.text = "비밀번호가 일치합니다."
                    binding.activitySignInTvPasswordCheck.setTextColor(getColor(R.color.green))
                } else {
                    binding.activitySignInTvPasswordCheck.text = "비밀번호가 일치하지 않습니다."
                    binding.activitySignInTvPasswordCheck.setTextColor(getColor(R.color.red))
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    companion object {
        fun isSafe(option: Int, string: String): Boolean {
            lateinit var regex: Regex

            when (option) {
                1 -> regex = "^[a-z][a-z0-9]{4,19}$".toRegex() // 영어 소문자로 시작, 소문자, 숫자 허용, 5 ~ 20글자
                2 -> regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\$@\$!%*#?&])[A-Za-z\\d\$@\$!%*#?&]{8,16}\$".toRegex() // 영어, 숫자, 특수문자 하나씩은 조합 8 ~ 16
            }

            return regex.matchEntire(string) != null
        }
    }
}