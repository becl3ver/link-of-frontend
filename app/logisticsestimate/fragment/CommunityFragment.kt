package com.example.logisticsestimate.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.logisticsestimate.databinding.FragmentCommunityBinding

/**
 * 유저가 작성한 글, 북마크, 임시 저장 글 리스트 등에 접근
 * 게시판 접근
 * @author 최재훈
 * @version 1.0, 버튼을 통해 글 목록을 출력하는 엑티비티 시작
 */
class CommunityFragment : Fragment() {
    private var _binding : FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}