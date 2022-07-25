package com.example.logisticsestimate.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.logisticsestimate.BoardListActivity
import com.example.logisticsestimate.TemporaryPostActivity
import com.example.logisticsestimate.TermsActivity
import com.example.logisticsestimate.databinding.FragmentCommunityBinding

/**
 * 유저가 작성한 글, 북마크, 임시 저장 글 리스트 등에 접근
 * 게시판 접근
 * @author 최재훈
 * @version
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
            binding.fragmentCommunityBtnDictionary.id -> startActivity(Intent(context, TermsActivity::class.java))

            binding.fragmentCommunityBtnTemporary.id -> startActivity(Intent(context, TemporaryPostActivity::class.java))

            binding.fragmentCommunityBtnNotice.id -> loadBoardList(0)
            binding.fragmentCommunityBtnQna.id -> loadBoardList(1)
            binding.fragmentCommunityBtnFree.id -> loadBoardList(2)

            else -> Log.d(TAG, "Failed to find onClick()")
        }
    }

    private fun loadBoardList(idx : Int) {
        startActivity(Intent(context, BoardListActivity::class.java).let { it.putExtra("category", idx) })
    }

    companion object {
        val BOARD_NAME = arrayOf("공지사항", "Q&A", "자유")
    }
}