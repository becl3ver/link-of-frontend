package com.example.logisticsestimate.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
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
import com.example.logisticsestimate.view.activity.SignInActivity
import com.example.logisticsestimate.base.App
import com.example.logisticsestimate.base.MainActivity
import com.example.logisticsestimate.databinding.FragmentMyPageBinding

/**
 * 로그인, 로그아웃, 앱 설정, 유저 정보를 조회
 */
class MyPageFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: FragmentMyPageBinding? = null
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
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        if(App.prefs.getAccessToken() == "") {
            setBtnSignedOut()
        } else {
            setBtnSignedIn()
        }

        binding.fragmentMyPageBtnHistory.setOnClickListener {

        }

        binding.fragmentMyPageBtnUpdate.setOnClickListener {

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
        binding.fragmentMyPageBtn.text = getString(R.string.sign_out)
        binding.fragmentMyPageContainer.visibility = View.VISIBLE

        binding.fragmentMyPageBtn.setOnClickListener {
            AlertDialog.Builder(requireActivity()).let {
                it.setMessage("로그아웃 하시겠습니까?")
                it.setPositiveButton("확인") { _, _ ->
                    setBtnSignedOut()
                    App.prefs.removeAccessToken()
                    App.prefs.removeUid()
                    App.prefs.removeNickname()
                    App.prefs.removeString("id")
                    App.prefs.removeString("password")
                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
                }
                it.setNegativeButton("취소")  { _, _ -> }
                it.create()
                it.show()
            }
        }
    }

    private fun setBtnSignedOut() {
        binding.fragmentMyPageBtn.text = getString(R.string.sign_in)
        binding.fragmentMyPageContainer.visibility = View.GONE

        binding.fragmentMyPageBtn.setOnClickListener {
            val intent = Intent(context, SignInActivity::class.java)
            getResult.launch(intent)
        }
    }
}