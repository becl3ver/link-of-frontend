package com.example.logisticsestimate.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.logisticsestimate.R
import com.example.logisticsestimate.base.App
import com.example.logisticsestimate.data.remote.api.AccountRetrofitClient
import com.example.logisticsestimate.data.remote.model.login.AccountRequestDto
import com.example.logisticsestimate.data.remote.model.login.AccountResponseDto
import com.example.logisticsestimate.data.remote.model.login.AccountSignInDto
import com.example.logisticsestimate.data.remote.model.login.TokenDto
import com.example.logisticsestimate.databinding.ActivityMyInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 로그인한 사용자의 정보를 표시하고, 수정할 수 있게 한다.
 */
class MyInfoActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = getString(R.string.change_info)

        binding.activityMyInfoEtName.setText(App.prefs.getName())
        binding.activityMyInfoEtNickname.setText(App.prefs.getNickname())

        binding.activityMyInfoBtnNicknameCheck.setOnClickListener(this)
        binding.activityMyInfoBtnSubmit.setOnClickListener(this)
        binding.activityMyInfoBtnCurrentPassword.setOnClickListener(this)
        binding.activityMyInfoTvBtnSubmitPassword.setOnClickListener(this)

        binding.activityMyInfoEtPassword.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val length = p0.toString().length

                binding.activityMyInfoTvPassword.text =
                    if (length < 8 || length > 20) "비밀번호는 8 ~ 20자로 이루어져야 합니다."
                    else if (isSafe(p0.toString())) ""
                    else "영문, 숫자, 특수문자를 하나씩 포함해야 합니다."
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.activityMyInfoEtPasswordCheck.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.activityMyInfoEtPassword.text.toString() == p0.toString()) {
                    binding.activityMyInfoTvPasswordCheck.text = "비밀번호가 일치합니다."
                    binding.activityMyInfoTvPasswordCheck.setTextColor(getColor(R.color.green))
                } else {
                    binding.activityMyInfoTvPasswordCheck.text = "비밀번호가 일치하지 않습니다."
                    binding.activityMyInfoTvPasswordCheck.setTextColor(getColor(R.color.red))
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.activityMyInfoBtnCurrentPassword.id -> {
                val id = App.prefs.getLoginId()
                val password = binding.activityMyInfoEtCurrentPassword.text.toString()

                val call = AccountRetrofitClient.getInstance().signIn(AccountSignInDto(id, password))

                call.enqueue(object: Callback<TokenDto> {
                    override fun onResponse(call: Call<TokenDto>, response: Response<TokenDto>) {
                        if (!response.isSuccessful) {
                            if(response.code() == 401) {
                                Toast.makeText(
                                    this@MyInfoActivity,
                                    "비밀번호를 다시 확인해주세요.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                Toast.makeText(
                                    this@MyInfoActivity,
                                    "오류가 발생했습니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                this@MyInfoActivity,
                                "확인이 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            App.prefs.removeLoginData()
                            App.prefs.setLoginData(response.body()!!)
                            binding.activityMyInfoEtCurrentPassword.isEnabled = false

                            setButtonStateInverse(binding.activityMyInfoBtnCurrentPassword)
                            setButtonStateInverse(binding.activityMyInfoTvBtnSubmitPassword)
                        }
                    }

                    override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                        Toast.makeText(this@MyInfoActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("http", "onFailure : " + t.message)
                    }
                })
            }

            binding.activityMyInfoBtnNicknameCheck.id -> {
                val nickname = binding.activityMyInfoEtNickname.text.toString()
                if(nickname != App.prefs.getNickname()) {
                    val accountRequestDto = AccountRequestDto("", "", "", nickname)
                    val call =
                        AccountRetrofitClient.getInstance().checkDuplication(accountRequestDto)

                    call.enqueue(object : Callback<AccountResponseDto> {
                        override fun onResponse(
                            call: Call<AccountResponseDto>,
                            response: Response<AccountResponseDto>
                        ) {
                            if (!response.isSuccessful) {
                                Toast.makeText(
                                    this@MyInfoActivity,
                                    "오류가 발생했습니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                val str: String = when (response.body()?.result) {
                                    "nickname" -> "이미 사용 중인 닉네임입니다."
                                    else -> {
                                        binding.activityMyInfoEtNickname.isEnabled = false
                                        setButtonStateInverse(binding.activityMyInfoBtnNicknameCheck)
                                        setButtonStateInverse( binding.activityMyInfoBtnSubmit)
                                        "사용 가능한 닉네임입니다."
                                    }
                                }

                                Toast.makeText(this@MyInfoActivity, str, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<AccountResponseDto>, t: Throwable) {
                            Toast.makeText(this@MyInfoActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("http", "onFailure : " + t.message)
                        }
                    })
                } else {
                    binding.activityMyInfoEtNickname.isEnabled = false
                    setButtonStateInverse(binding.activityMyInfoBtnNicknameCheck)
                    setButtonStateInverse(binding.activityMyInfoBtnSubmit)
                }
            }

            binding.activityMyInfoBtnSubmit.id -> {
                val token = App.prefs.getAccessToken()

                val id = App.prefs.getLoginId()
                val name = binding.activityMyInfoEtName.text.toString()
                val password = binding.activityMyInfoEtCurrentPassword.text.toString()
                val nickname = binding.activityMyInfoEtNickname.text.toString()
                val accountRequestDto = AccountRequestDto(id, name, password, nickname)

                val call = AccountRetrofitClient.getInstance().updateAccount(token, accountRequestDto)

                call.enqueue(object: Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        if (!response.isSuccessful) {
                            Toast.makeText(this@MyInfoActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this@MyInfoActivity, "변경이 완료되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                            App.prefs.setLoginId(id)
                            App.prefs.setName(name)
                            App.prefs.setNickname(nickname)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@MyInfoActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("http", "onFailure : " + t.message)
                    }
                })
            }

            binding.activityMyInfoTvBtnSubmitPassword.id -> {
                val token = App.prefs.getAccessToken()

                val id = App.prefs.getLoginId()
                val name = App.prefs.getName()
                val nickname = App.prefs.getNickname()
                val password = binding.activityMyInfoEtPassword.text.toString()
                val accountRequestDto = AccountRequestDto(id, name, password, nickname)

                val call = AccountRetrofitClient.getInstance().updateAccount(token, accountRequestDto)
                call.enqueue(object: Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        if(!response.isSuccessful) {
                            Toast.makeText(
                                this@MyInfoActivity,
                                "오류가 발생했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MyInfoActivity,
                                "변경이 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@MyInfoActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("http", "onFailure : " + t.message)
                    }
                })
            }
        }
    }

    private fun isSafe(str: String): Boolean {
        val regex =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\$@\$!%*#?&])[A-Za-z\\d\$@\$!%*#?&]{8,16}\$".toRegex()
        return regex.matchEntire(str) != null
    }

    private fun setButtonStateInverse(button: Button) {
        if(button.isEnabled) {
            button.isEnabled = false
            button.background = ContextCompat.getDrawable(
                this@MyInfoActivity,
                R.drawable.all_btn_round_edge_disabled
            )
        } else {
            button.isEnabled = true
            button.background = ContextCompat.getDrawable(
                this@MyInfoActivity,
                R.drawable.all_btn_round_edge
            )
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