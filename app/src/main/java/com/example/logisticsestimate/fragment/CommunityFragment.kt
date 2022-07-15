package com.example.logisticsestimate.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.logisticsestimate.BoardListActivity
import com.example.logisticsestimate.TermActivity
import com.example.logisticsestimate.databinding.FragmentCommunityBinding

/**
 * 유저가 작성한 글, 북마크, 임시 저장 글 리스트 등에 접근
 * 게시판 접근
 * @author 최재훈
 * @version
 */
class CommunityFragment : Fragment(), View.OnClickListener {
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

    override fun onClick(v: View?) {
        when(v) {
            binding.fragmentCommunityBtnDictionary -> {
                val intent = Intent(context, TermActivity::class.java)
                startActivity(intent)
            }

            binding.fragmentCommunityBtnNotice -> loadBoardList(0)
            binding.fragmentCommunityBtnQna -> loadBoardList(1)
            binding.fragmentCommunityBtnFree -> loadBoardList(2)
        }
    }

    private fun loadBoardList(idx : Int) {
        val intent = Intent(context, BoardListActivity::class.java)
        intent.putExtra("category", idx)
        startActivity(intent)
    }

    companion object {
        val BOARD_NAME = arrayOf("공지사항", "Q&A", "자유")
    }
}