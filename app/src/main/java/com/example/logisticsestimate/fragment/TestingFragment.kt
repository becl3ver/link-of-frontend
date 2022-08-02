package com.example.logisticsestimate.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.logisticsestimate.App
import com.example.logisticsestimate.data.BoardDto
import com.example.logisticsestimate.data.BoardRequestDto
import com.example.logisticsestimate.data.BoardResponseDto
import com.example.logisticsestimate.databinding.FragmentTestingBinding
import com.example.logisticsestimate.repository.BoardRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestingFragment : Fragment() {
    private var _binding : FragmentTestingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestingBinding.inflate(inflater, container, false)

        binding.fragmentTestingBtnSubmit.setOnClickListener {
            val title = binding.fragmentTestingEtTitle.text.toString()
            val content = binding.fragmentTestingEtContent.text.toString()
            val category = 1

            val board = BoardDto(title, content, category)

            val token = App.prefs.getAccessToken("")

            if(token == "") {
                Toast.makeText(context, "token not exist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val call = BoardRetrofitBuilder.getInstance().putNewBoard("Bearer $token", board)

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(!response.isSuccessful) {
                        Toast.makeText(context, "communication failed", Toast.LENGTH_SHORT).show()
                    } else {
                        if(response.body()!!.toString() == "false") {
                            Toast.makeText(context, "push failed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, response.body()!!.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context, "communication failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.fragmentTestingBtnTest.setOnClickListener {
            val boardRequest = BoardRequestDto("", "", null, 1, 20, null)

            val call = BoardRetrofitBuilder.getInstance().getBoards(boardRequest)
            call.enqueue(object : Callback<BoardResponseDto> {
                override fun onResponse(
                    call: Call<BoardResponseDto>,
                    response: Response<BoardResponseDto>
                ) {
                    if(!response.isSuccessful) {
                        Toast.makeText(context, "communication failed", Toast.LENGTH_SHORT).show()
                    } else {
                        if(response.body()!!.boards.size == 0) {
                            Toast.makeText(context, "content empty", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, response.body()!!.boards.get(0).boardTitle
                            + response.body()!!.boards.get(0).boardContent,
                            Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<BoardResponseDto>, t: Throwable) {
                    Toast.makeText(context, "communication failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}