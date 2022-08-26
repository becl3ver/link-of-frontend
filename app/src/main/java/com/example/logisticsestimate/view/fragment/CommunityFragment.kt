package com.example.logisticsestimate.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.logisticsestimate.view.activity.BoardListActivity
import com.example.logisticsestimate.view.activity.TemporaryBoardActivity
import com.example.logisticsestimate.view.activity.TermActivity
import com.example.logisticsestimate.databinding.FragmentCommunityBinding
import com.example.logisticsestimate.view.activity.CBMActivity

/**
 * 게시판 프래그먼트
 */
class CommunityFragment: Fragment(), View.OnClickListener {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.fragmentCommunityBtnCbm.setOnClickListener(this)
        binding.fragmentCommunityBtnDictionary.setOnClickListener(this)
        binding.fragmentCommunityBtnPopular.setOnClickListener(this)

        binding.fragmentCommunityBtnBookmark.setOnClickListener(this)
        binding.fragmentCommunityBtnTemporary.setOnClickListener(this)
        binding.fragmentCommunityBtnNotice.setOnClickListener(this)
        binding.fragmentCommunityBtnFree.setOnClickListener(this)
        binding.fragmentCommunityBtnQna.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.fragmentCommunityBtnCbm.id -> startActivity(Intent(context, CBMActivity::class.java))
            binding.fragmentCommunityBtnDictionary.id -> startActivity(Intent(context, TermActivity::class.java))

            binding.fragmentCommunityBtnPopular.id -> loadBoardListByCategory(10)
            binding.fragmentCommunityBtnBookmark.id -> loadBoardListByCategory(0)
            binding.fragmentCommunityBtnTemporary.id -> startActivity(Intent(context, TemporaryBoardActivity::class.java))

            binding.fragmentCommunityBtnNotice.id -> loadBoardListByCategory(1)
            binding.fragmentCommunityBtnQna.id -> loadBoardListByCategory(2)
            binding.fragmentCommunityBtnFree.id -> loadBoardListByCategory(3)
        }
    }

    private fun loadBoardListByCategory(category: Int) {
        startActivity(Intent(context, BoardListActivity::class.java).putExtra("category", category))
    }

    companion object {
        val BOARD_NAME = arrayOf("즐겨찾기", "공지사항", "Q&A", "자유")
    }
}