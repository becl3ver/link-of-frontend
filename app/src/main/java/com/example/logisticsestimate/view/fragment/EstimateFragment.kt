package com.example.logisticsestimate.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.logisticsestimate.R
import com.example.logisticsestimate.data.remote.api.EstimateRetrofitClient
import com.example.logisticsestimate.data.remote.model.estimate.EstimateRequestDto
import com.example.logisticsestimate.data.remote.model.estimate.EstimateResponseDto
import com.example.logisticsestimate.databinding.FragmentEstimateBinding
import com.example.logisticsestimate.view.activity.EstimateActivity
import kotlinx.android.synthetic.main.fragment_estimate.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import kotlin.math.floor
import kotlin.math.round

/**
 * 메인 화면, 견적 계산 기능을 수행한다.
 */
class EstimateFragment : Fragment() {
    private var _binding : FragmentEstimateBinding? = null
    private val binding get() = _binding!!
    private var tmpTransshipmentYn : String? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEstimateBinding.inflate(inflater, container, false)

        setAdapter()

        binding.fragmentEstimateRgTransshipmentYn.setOnCheckedChangeListener { _, checkId ->
            when(checkId) {
                R.id.fragment_estimate_rg_yes -> tmpTransshipmentYn = "Y"
                R.id.fragment_estimate_rg_no -> tmpTransshipmentYn = "N"
            }
        }

        binding.fragmentEstimateBtnSumit.setOnClickListener{

            val annNumber = binding.fragmentEstimateEtAnnNumber.text.toString()
            val annRuteName = binding.fragmentEstimateMtvAnnRuteName.text.toString()
            val shippingPortName = binding.fragmentEstimateMtvShippingPortName.text.toString()
            val landingPortName = binding.fragmentEstimateMtvLandingPortName.text.toString()
            val codPortName = binding.fragmentEstimateMtvCodPortName.text.toString()
            val containerOwnSeName = binding.fragmentEstimateSpnContainerOwnSeName.selectedItem.toString()
            val containerCndName = binding.fragmentEstimateSpnContainerCndName.selectedItem.toString()
            val containerStdStndrdName = binding.fragmentEstimateEtContainerStdStndrdName.text.toString() + " feet"
            val freightName = binding.fragmentEstimateSpnFreightName.selectedItem.toString()
            val tnSpotSeName = binding.fragmentEstimateSpnTnSpotSeName.selectedItem.toString()

            if(!tmpTransshipmentYn.isNullOrEmpty()) {
                val transshipmentYn = tmpTransshipmentYn

                val call = EstimateRetrofitClient.getInstance().requestEstimateResult(
                    EstimateRequestDto(
                        annNumber, annRuteName, shippingPortName, landingPortName, codPortName, transshipmentYn!!,
                        containerOwnSeName, containerCndName, containerStdStndrdName, freightName, tnSpotSeName)
                )

                call.enqueue(object : Callback<EstimateResponseDto> {
                    override fun onResponse(
                        call: Call<EstimateResponseDto>,
                        response: Response<EstimateResponseDto>
                    ) {
                        if(!response.isSuccessful) {
                            Toast.makeText(requireContext(), "입력값이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(requireContext(), "조회에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                            val result = response.body()!!

                            val intent = Intent(requireContext(), EstimateActivity::class.java)

                            intent.putExtra("estimateResult", result)

                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<EstimateResponseDto>, t: Throwable) {
                        Log.d("HTTP", "onFailure: " + t.message!!)
                        Toast.makeText(activity, "연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else {
                Toast.makeText(requireContext(), "입력값이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }


        }

        return binding.root
    }

    private fun setAdapter() {
        val annRuteNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.annRuteNameList, R.layout.ship_search_option_spinner)
        val shippingPortNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.shippingPortNameList, R.layout.ship_search_option_spinner)
        val landingOrCodPortNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.landingOrCodPortNameList, R.layout.ship_search_option_spinner)

        val contnOwnSeNameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.contnOwnSeNameList, R.layout.ship_search_option_spinner)
        val contnCndNameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.contnCndNameList, R.layout.ship_search_option_spinner)
        val freightNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.freightNameList, R.layout.ship_search_option_spinner)
        val tnSpotSeNameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.tnSpotSeNameList, R.layout.ship_search_option_spinner)

        binding.fragmentEstimateMtvAnnRuteName.setAdapter(annRuteNameAdapter)
        binding.fragmentEstimateMtvShippingPortName.setAdapter(shippingPortNameAdapter)
        binding.fragmentEstimateMtvLandingPortName.setAdapter(landingOrCodPortNameAdapter)
        binding.fragmentEstimateMtvCodPortName.setAdapter(landingOrCodPortNameAdapter)

        contnOwnSeNameAdapter.setDropDownViewResource(R.layout.ship_search_option_spinner_dropdown)
        contnCndNameAdapter.setDropDownViewResource(R.layout.ship_search_option_spinner_dropdown)
        freightNameAdapter.setDropDownViewResource(R.layout.ship_search_option_spinner_dropdown)
        tnSpotSeNameAdapter.setDropDownViewResource(R.layout.ship_search_option_spinner_dropdown)

        binding.fragmentEstimateSpnContainerOwnSeName.adapter = contnOwnSeNameAdapter
        binding.fragmentEstimateSpnContainerCndName.adapter = contnCndNameAdapter
        binding.fragmentEstimateSpnFreightName.adapter = freightNameAdapter
        binding.fragmentEstimateSpnTnSpotSeName.adapter = tnSpotSeNameAdapter

    }
}