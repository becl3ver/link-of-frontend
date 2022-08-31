package com.example.logisticsestimate.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.logisticsestimate.BuildConfig
import com.example.logisticsestimate.BuildConfig.EXCHANGE_API_KEY
import com.example.logisticsestimate.R
import com.example.logisticsestimate.base.App.Companion.context
import com.example.logisticsestimate.data.remote.api.service.ExchangeService
import com.example.logisticsestimate.data.remote.model.estimate.EstimateResponseDto
import com.example.logisticsestimate.data.remote.model.exchange.ExchangeRateDto
import com.example.logisticsestimate.databinding.ActivityEstimateBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDate
import java.util.*
import kotlin.math.floor
import kotlin.math.round

/**
 * 사용자가 입력한 정보를 바탕으로 화물 견적 계산
 */
class EstimateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEstimateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstimateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)
        supportActionBar!!.title = "견적"

        val resultEstimate = intent.getSerializableExtra("estimateResult") as EstimateResponseDto

        isCodeNull(resultEstimate)

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        val client = builder.addInterceptor(interceptor).build()
        val service = Retrofit.Builder()
            .baseUrl(BuildConfig.EXCHANGE_API_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
            .create(ExchangeService::class.java)

        val call = service.getExchangeRate(EXCHANGE_API_KEY, dateCheck(), "AP01")

        call.enqueue(object : Callback<ExchangeRateDto> {
            override fun onResponse(
                call: Call<ExchangeRateDto>,
                response: Response<ExchangeRateDto>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(context, "환율 API 로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val resultExchange = response.body()!!

                    print("body() : ")
                    println(response.body())

                    binding.activityEstimateTvResultTotalCost.text =
                        calTotal(resultEstimate, resultExchange!!) + " KRW"
                }
            }

            override fun onFailure(call: Call<ExchangeRateDto>, t: Throwable) {
                Toast.makeText(context, "환율 API 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isCodeNull(result: EstimateResponseDto) {
        if (result.Of == "0") binding.activityEstimateTvResultCychgOf.text = "0"
        else binding.activityEstimateTvResultCychgOf.text = result.Of + " " + result.ofCode
        if (result.Baf == "0") binding.activityEstimateTvResultCychgBaf.text = "0"
        else binding.activityEstimateTvResultCychgBaf.text = result.Baf + " " + result.bafCode
        if (result.Caf == "0") binding.activityEstimateTvResultCychgCaf.text = "0"
        else binding.activityEstimateTvResultCychgCaf.text = result.Caf + " " + result.cafCode
        if (result.Lss == "0") binding.activityEstimateTvResultCychgLss.text = "0"
        else binding.activityEstimateTvResultCychgLss.text = result.Lss + " " + result.lssCode
        if (result.Ebs == "0") binding.activityEstimateTvResultCychgEbs.text = "0"
        else binding.activityEstimateTvResultCychgEbs.text = result.Ebs + " " + result.ebsCode
        if (result.Othc == "0") binding.activityEstimateTvResultCychgOthc.text = "0"
        else binding.activityEstimateTvResultCychgOthc.text = result.Othc + " " + result.othcCode
        if (result.Dthc == "0") binding.activityEstimateTvResultCychgDthc.text = "0"
        else binding.activityEstimateTvResultCychgDthc.text = result.Dthc + " " + result.dthcCode
        if (result.Df == "0") binding.activityEstimateTvResultCychgDf.text = "0"
        else binding.activityEstimateTvResultCychgDf.text = result.Df + " " + result.dfCode
        if (result.Do == "0") binding.activityEstimateTvResultCychgDo.text = "0"
        else binding.activityEstimateTvResultCychgDo.text = result.Do + " " + result.doCode
        if (result.Waf == "0") binding.activityEstimateTvResultCychgWaf.text = "0"
        else binding.activityEstimateTvResultCychgWaf.text = result.Waf + " " + result.wafCode
        if (result.Sc == "0") binding.activityEstimateTvResultCychgSc.text = "0"
        else binding.activityEstimateTvResultCychgSc.text = result.Sc + " " + result.scCode
        if (result.Etc == "0") binding.activityEstimateTvResultCychgEtc.text = "0"
        else binding.activityEstimateTvResultCychgEtc.text = result.Etc + " " + result.etcCode
    }

    private fun floorSc(str: String): Double {
        val scToken = str.split(".")

        var valResult: Double =
            floor(((scToken[0].toDouble() + scToken[1].toDouble() / 10) * 100) / 100)

        90
        return valResult
    }

    private fun dateCheck(): String {

        val cal = Calendar.getInstance()
        var varYear = cal.get(Calendar.YEAR)
        var varMonth = cal.get(Calendar.MONTH) + 1
        var varDate = cal.get(Calendar.DATE) - 1

        var wod = cal.get(Calendar.DAY_OF_WEEK)

        when(wod) {
            1 -> if(varDate <= 2) {
                if(varMonth == 1) {
                    varYear -= 1
                    varMonth = 12
                    varDate = 29 - varDate
                }
                else {
                    varDate -= 2
                }
            }

            2 -> if(varDate <= 1) {
                if(varMonth == 1) {
                    varYear -= 1
                    varMonth = 12
                    varDate = 29 - varDate
                }
                else {
                    varDate -= 1
                }
            }
        }

        binding.activityEstimateTvExchangeNotice.text =
            "현재 적용 환율은 한국은행 " + varMonth.toString() + "월 " + varDate.toString() + "일 기준입니다."

        if(varMonth < 10) {
            return varYear.toString() + "0" + varMonth.toString() + varDate.toString()
        }

        return varYear.toString() + varMonth.toString() + varDate.toString()

    }

    private fun calTotal(rEs: EstimateResponseDto, rEx: ExchangeRateDto): String {

        val total = rEs.Of.toInt() * checkCode(rEs.Of, rEs.ofCode, rEx) +
                rEs.Baf.toInt() * checkCode(rEs.Baf, rEs.bafCode, rEx) +
                rEs.Caf.toInt() * checkCode(rEs.Caf, rEs.cafCode, rEx) +
                rEs.Lss.toInt() * checkCode(rEs.Lss, rEs.lssCode, rEx) +
                rEs.Ebs.toInt() * checkCode(rEs.Ebs, rEs.ebsCode, rEx) +
                rEs.Othc.toInt() * checkCode(rEs.Othc, rEs.othcCode, rEx) +
                rEs.Dthc.toInt() * checkCode(rEs.Dthc, rEs.dthcCode, rEx) +
                rEs.Df.toInt() * checkCode(rEs.Df, rEs.dfCode, rEx) +
                rEs.Do.toInt() * checkCode(rEs.Do, rEs.doCode, rEx) +
                rEs.Waf.toInt() * checkCode(rEs.Waf, rEs.wafCode, rEx) +
                floorSc(rEs.Sc) * checkCode(rEs.Sc, rEs.scCode, rEx) +
                rEs.Etc.toInt() * checkCode(rEs.Etc, rEs.etcCode, rEx)

        return (round(total * 100 ) / 100).toString()
    }

    private fun checkCode(cost: String, code: String, rEx: ExchangeRateDto): Double {
        if (cost != "0") {

            var idx = 0

            if (code == "KRW") {
                return 1.0
            }

            if (code == "CNY") {
                return rEx[6].tts.toDouble()
            }

            while (idx < 23) {
                print(code)
                print(" ? ")
                println(rEx[idx].cur_unit)
                if (code == rEx[idx].cur_unit) {
                    val comaToken = rEx[idx].tts.split(',')

                    if (comaToken[0] != rEx[idx].tts)
                        return (comaToken[0] + comaToken[1]).toDouble()
                    else
                        return rEx[idx].tts.toDouble()
                }
                idx++
            }
        }


        return -1.0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}