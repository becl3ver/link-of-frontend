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
import com.example.logisticsestimate.base.App
import com.example.logisticsestimate.base.MainActivity
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.EstimateHistoryEntity
import com.example.logisticsestimate.data.db.entity.TrackingHistoryEntity
import com.example.logisticsestimate.data.remote.api.EstimateRetrofitClient
import com.example.logisticsestimate.data.remote.model.estimate.EstimateRequestDto
import com.example.logisticsestimate.data.remote.model.estimate.EstimateResponseDto
import com.example.logisticsestimate.databinding.FragmentEstimateBinding
import com.example.logisticsestimate.view.activity.EstimateActivity
import com.example.logisticsestimate.view.activity.HistoryActivity
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

    private lateinit var binding : FragmentEstimateBinding
    private lateinit var db: AppDatabase
    private var tmpTransshipmentYn : String? = null

    private var annNumber: String? = null
    private var annRuteName: String? = null
    private var shippingPortName: String? = null
    private var landingPortName: String? = null
    private var codPortName: String? = null
    private var containerOwnSeName: String? = null
    private var containerCndName: String? = null
    private var containerStdStndrdName: String? = null
    private var freightName: String? = null
    private var tnSpotSeName: String? = null
    private var transshipmentYn: String? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEstimateBinding.inflate(inflater, container, false)
        db = AppDatabase.getInstance(requireContext())!!

        setAdapter()

        binding.fragmentEstimateRgTransshipmentYn.setOnCheckedChangeListener { group, checkId ->
            when(checkId) {
                R.id.fragment_estimate_rg_yes -> tmpTransshipmentYn = "Y"
                R.id.fragment_estimate_rg_no -> tmpTransshipmentYn = "N"
            }
        }

        binding.fragmentEstimateBtnSumit.setOnClickListener{

            if(!tmpTransshipmentYn.isNullOrEmpty()) {
                annNumber = binding.fragmentEstimateEtAnnNumber.text.toString()
                annRuteName = binding.fragmentEstimateMtvAnnRuteName.text.toString()
                shippingPortName = binding.fragmentEstimateMtvShippingPortName.text.toString()
                landingPortName = binding.fragmentEstimateMtvLandingPortName.text.toString()
                codPortName = binding.fragmentEstimateMtvCodPortName.text.toString()
                containerOwnSeName = binding.fragmentEstimateSpnContainerOwnSeName.selectedItem.toString()
                containerCndName = binding.fragmentEstimateSpnContainerCndName.selectedItem.toString()
                containerStdStndrdName = binding.fragmentEstimateEtContainerStdStndrdName.text.toString()
                freightName = binding.fragmentEstimateSpnFreightName.selectedItem.toString()
                tnSpotSeName = binding.fragmentEstimateSpnTnSpotSeName.selectedItem.toString()
                transshipmentYn = tmpTransshipmentYn

                val estimateRequestDto = EstimateRequestDto(annNumber!!, annRuteName!!, shippingPortName!!, landingPortName!!, codPortName!!,
                    transshipmentYn!!, containerOwnSeName!!, containerCndName!!, containerStdStndrdName + " feet", freightName!!, tnSpotSeName!!)

                getEstimateInfo(estimateRequestDto)


            }
            else {
                Toast.makeText(requireContext(), "입력값이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun getEstimateInfo(estimateRequestDto: EstimateRequestDto) {
        val call =
            EstimateRetrofitClient.getInstance().requestEstimateResult(estimateRequestDto)

        call.enqueue(object : Callback<EstimateResponseDto> {
            override fun onResponse(
                call: Call<EstimateResponseDto>,
                response: Response<EstimateResponseDto>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(context, "입력값이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "조회에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                    val result = response.body()!!
                    val intent = Intent(context, EstimateActivity::class.java)



                    insertHistory(
                        EstimateHistoryEntity
                            (
                            0,
                            estimateRequestDto.annNo,
                            estimateRequestDto.annRuteNm,
                            estimateRequestDto.shippingPrtNm,
                            estimateRequestDto.landingPrtNm,
                            estimateRequestDto.codPrtNm,
                            estimateRequestDto.tsYn,
                            estimateRequestDto.contnOwnSeNm,
                            estimateRequestDto.contnCndNm,
                            estimateRequestDto.contnStdStndrdNm,
                            estimateRequestDto.frghtNm,
                            estimateRequestDto.tnspotSeNm
                        )
                    )

                    val primaryKey =
                        estimateRequestDto.annNo.toInt()


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

    private fun insertHistory(e: EstimateHistoryEntity) {
        Thread {
            if (db.getEstimateHistoryDao().isExist(
                e.annNumber,
                e.annRuteName,
                e.shippingPortName,
                e.landingPortName,
                e.codPortName,
                e.transshipmentYn,
                e.containerOwnSeName,
                e.containerCndName,
                e.containerStdStndrdName,
                e.freightName,
                e.tnSpotSeName
            ).isEmpty()) {
                db.getEstimateHistoryDao().insert(e)
            }
        }.start()
    }

    private fun setAdapter() {

        var annRuteNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.annRuteNameList, R.layout.ship_search_option_spinner)
        var shippingPortNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.shippingPortNameList, R.layout.ship_search_option_spinner)
        var landingOrCodPortNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.landingOrCodPortNameList, R.layout.ship_search_option_spinner)

        var contnOwnSeNameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.contnOwnSeNameList, R.layout.ship_search_option_spinner)
        var contnCndNameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.contnCndNameList, R.layout.ship_search_option_spinner)
        var freightNameAdapter  = ArrayAdapter.createFromResource(requireContext(), R.array.freightNameList, R.layout.ship_search_option_spinner)
        var tnSpotSeNameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.tnSpotSeNameList, R.layout.ship_search_option_spinner)

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