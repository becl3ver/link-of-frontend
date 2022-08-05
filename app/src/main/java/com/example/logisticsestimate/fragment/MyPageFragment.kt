package com.example.logisticsestimate.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.logisticsestimate.*
import com.example.logisticsestimate.databinding.FragmentMyPageBinding
import kotlin.concurrent.thread

/**
 * 로그인, 로그아웃, 앱 설정, 유저 정보를 조회한다.
 */
class MyPageFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding : FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        when(result.resultCode) {
            RESULT_OK -> {
                mainActivity.runOnUiThread {
                    setBtnSignedIn()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        /* TODO(토큰 관리 메모리에서 하도록 수정?) */
        if(App.prefs.getAccessToken("empty") == "empty") {
            setBtnSignedOut()
        } else {
            setBtnSignedIn()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private fun setBtnSignedIn() {
        binding.fragmentMyPageBtn1.text = getString(R.string.my_info)
        binding.fragmentMyPageBtn2.text = getString(R.string.sign_out)

        binding.fragmentMyPageBtn1.setOnClickListener {
            startActivity(Intent(context, MyInfoActivity::class.java))
        }

        binding.fragmentMyPageBtn2.setOnClickListener {
            AlertDialog.Builder(requireActivity()).let {
                it.setMessage("로그아웃 하시겠습니까?")
                it.setPositiveButton("네") { _, _ ->
                    setBtnSignedOut()
                    App.prefs.removeAccessToken()
                    App.prefs.removeString("id")
                    App.prefs.removeString("password")
                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
                }
                it.setNegativeButton("아니오", DialogInterface.OnClickListener { _, _ ->
                    return@OnClickListener
                })
                it.create()
                it.show()
            }
        }
    }

    private fun setBtnSignedOut() {
        binding.fragmentMyPageBtn1.text = getString(R.string.sign_in)
        binding.fragmentMyPageBtn2.text = getString(R.string.sign_up)

        binding.fragmentMyPageBtn1.setOnClickListener {
            val intent = Intent(context, SignInActivity::class.java)
            getResult.launch(intent)
        }

        binding.fragmentMyPageBtn2.setOnClickListener {
            startActivity(Intent(context, SignUpActivity::class.java))
        }
    }
}