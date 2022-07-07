package com.example.logisticsestimate.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.logisticsestimate.databinding.FragmentTrackingBinding

/**
 * @author 최재훈
 * @version
 */
class TrackingFragment : Fragment() {
    private var _binding : FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)

        return binding.root
    }
}