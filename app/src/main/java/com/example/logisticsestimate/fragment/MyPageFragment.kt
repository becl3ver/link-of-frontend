package com.example.logisticsestimate.fragment

import android.app.Activity.RESULT_OK
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

/**
 * @author 최재훈
 * @version
 *
 * 앱 종료 시 로그아웃 처리 필요
 */
class MyPageFragment : Fragment() {
    private var _binding : FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        when(result.resultCode) {
            RESULT_OK -> {
                setBtnSignedIn()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

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

    private fun setBtnSignedIn() {
        binding.fragmentMyPageBtn1.text = getString(R.string.my_info)
        binding.fragmentMyPageBtn2.text = getString(R.string.sign_out)

        binding.fragmentMyPageBtn1.setOnClickListener {
            startActivity(Intent(context, MyInfoActivity::class.java))
        }

        binding.fragmentMyPageBtn2.setOnClickListener {
            AlertDialog.Builder(requireActivity()).let {
                it.setMessage("로그아웃 하시겠습니까?")
                it.setPositiveButton("네", DialogInterface.OnClickListener { _, _ ->
                    setBtnSignedOut()
                    App.prefs.removeAccessToken()
                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
                })
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