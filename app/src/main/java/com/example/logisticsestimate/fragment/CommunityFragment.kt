package com.example.logisticsestimate.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.logisticsestimate.BoardListActivity
import com.example.logisticsestimate.TemporaryBoardActivity
import com.example.logisticsestimate.TermActivity
import com.example.logisticsestimate.databinding.FragmentCommunityBinding

/**
 * 각 게시판으로 진입하는 액티비티를 시작할 수 있게 한다.
 */
class CommunityFragment : Fragment(), View.OnClickListener {
    private val TAG = CommunityFragment::class.java.name
    private var _binding : FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.fragmentCommunityBtnCbm.setOnClickListener(this)
        binding.fragmentCommunityBtnDictionary.setOnClickListener(this)

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
            binding.fragmentCommunityBtnCbm.id -> {}
            binding.fragmentCommunityBtnDictionary.id -> startActivity(Intent(context, TermActivity::class.java))

            binding.fragmentCommunityBtnBookmark.id -> loadBoardList(0)
            binding.fragmentCommunityBtnTemporary.id -> startActivity(Intent(context, TemporaryBoardActivity::class.java))

            binding.fragmentCommunityBtnNotice.id -> loadBoardList(1)
            binding.fragmentCommunityBtnQna.id -> loadBoardList(2)
            binding.fragmentCommunityBtnFree.id -> loadBoardList(3)
        }
    }

    private fun loadBoardList(idx : Int) {
        startActivity(Intent(context, BoardListActivity::class.java).putExtra("category", idx))
    }

    companion object {
        val BOARD_NAME = arrayOf("즐겨찾기", "공지사항", "Q&A", "자유")
    }
}