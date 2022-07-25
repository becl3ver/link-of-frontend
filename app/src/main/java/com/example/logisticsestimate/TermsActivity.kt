package com.example.logisticsestimate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logisticsestimate.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTermsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setData()

        val mAdapter = TermsAdapter(TermsList, this)

        binding.rcView.adapter = mAdapter
        binding.rcView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.termsSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                binding.termsSearchView.clearFocus()
                mAdapter!!.filter.filter(query)

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                mAdapter!!.filter.filter(query)

                return false
            }

        })
    }
}

val TermsList = ArrayList<TermsData>()

fun setData() {
    TermsList.add(TermsData("인코텀즈 (incoterms)",
        "인코텀즈(incoterms)란 국가 간의 무역활동 시 통용되는 규칙으로,\n" +
                "판매자로부터 구매자까지, 상품이 거래되는 과정에서 발생할 수 있는\n" +
                "위험과 책임에 대한 내용을 담고 있다.\n" +
                "국제 상업 회의소(ICC)가 제정한 무역 조건의 해석에 관한 국제 규칙으로 10년 단위로 개정되고 있다."),)
    TermsList.add(TermsData("HS코드 (HS CODE)",
        "HS CODE는 국제 통일 상품 분류 체계에 따라 대외 무역 거래 상품을 총괄적으로 분류한 품목 분류 코드를 말한다.\n" +
                "이 HS CODE는 총 6 자리로 구성되며, 우리나라는 물건의 세부 분류를 위해 4 자리를 추가해 사용하고 있다. (이를 HSK 코드라고 한다.)\n\n\n" +
                "(국제) HS CODE   ⇒ XXXXXX\n" +
                "(한국) HSK CODE ⇒ XXXXXX-XXXX"))
    TermsList.add(TermsData("유류할증료 BAF (Bunker Adjustment Factor)",
        "유류할증료 BAF (Bunker Adjustment Factor)는 운임 할증의 하나로, 선박 주 연료인 벙커유 가격 변동에 따른 선사의 손실 보전 목적으로 부과되는 비용이다.\n" +
                "유가는 시시각각 변하고 있으며, 이는 연료유 소비가 많은 선사는 유가가 급등할 경우 수익에 직접적으로 영향을 받게 된다.\n" +
                "따라서 선사는 이를 보전하기 위해 BAF를 부과하고 있다."))
    TermsList.add(TermsData("통화할증료 CAF(Currency Adjustment Factor)",
        "국가 대 국가 간 거래인 수출, 수입을 진행할 때는 일반적으로 미 달러로 계산을 하게 된다.\n" +
                "이럴 경우 미국 이외의 국가에서 환율 리스크가 발생하게 되어 외화 환차손으로 인한 위험에 노출되게 된다.\n" +
                "CAF (Currency Adjustment Factor)는 이와 같은 손실에 대응하기 위해 부과하는 금액을 말한다.\n" +
                "대부분의 경우 선사가 포워더에게 직접 청구하며, 포워더는 화주에게 다시 청구한다."))
    TermsList.add(TermsData("CFS Charge (Container Freight Station Charge)",
        "컨테이너를 꽉 채울 수 없는 LCL (소량 화물)의 경우 또는 별도 컨테이너 작업이 필요한 경우 CFS에서 다른 화물들과 혼재 또는 분류 등의 작업을 하게 된다.\n" +
                "CFS Charge는 이때 발생하는 비용을 말한다.\n" +
                "CFS Charge는 R.ton(R/T) 당 부과가 되며, 이 비용에는 CFS에서 발생되는 상하차비, 적출입료 등을 포함하고, 일반적으로 포워더를 통해 화주에게 청구된다."))
    TermsList.add(TermsData("CCC (Container Cleaning Charge)",
        "컨테이너 청소비를 의미하는 것으로 컨테이너의 상태에 따라 컨테이너 청소를 요구하게 되는 경우 부과되는 비용을 말한다.\n"+
                "다양한 화물을 적재하는 컨테이너의 경우 오염 등의 위험에 노출될 가능성이 있다.\n" +
                "이럴 때 소요되는 비용을 보전하는 차원에서 부과하는 비용이 CCC (Container Cleaning Charge) 또는 CCF (Container Cleaning Fee)이다."))
    TermsList.add(TermsData("CIC (Container Imbalance Charge)",
        "CIC (Container Imbalance Charge)는 선사에서 부과하는 비용으로 컨테이너 수급 불균형에 따른 " +
                "공 컨테이너의 부족 현상을 해결하고 원활한 서비스 제공을 목적으로 컨테이너 임대나 포지션을 진행하기 위해 사용되는 비용을 말한다. 수출, 수입은 화물을 컨테이너에 적재하여 운송이 된다.\n" +
                "이때 지역 별, 국가 별로 수요와 공급의 차이(Imbalance)가 발생하기 때문에 컨테이너가 남는 지역, 혹은 부족한 지역이 발생하게 된다.\n" +
                "컨테이너가 부족한 지역에서 출발하여 남는 지역으로 운송이 이뤄질 경우 다시 부족한 지역으로 컨테이너를 가져와야 하는 비용과 노력이 발생하게 되고, " +
                "이를 보전하기 위해 부과되는 비용이 CIC (Container Imbalance Charge)이다."))
    TermsList.add(TermsData("터미널 화물 처리비 THC (Terminal Handling Charge)",
        "THC (Terminal Handling Charge)는 해상 운임 중 정기선의 부대 운임 중 하나로" +
                "수출의 경우 컨테이너가 CY에 입고된 순간부터 본선의 선 측까지, 반대로 수입 시는 본선의 선 측에서 CY 게이트를 통과하기까지 화물의 이동에 따르는 비용을 말한다.\n" +
                "수출항(출발지)에서의 터미널 비용은 THC in Loading Port라고 하며," +
                "수입항(도착지)에서의 터미널 비용은 THC in Discharging Port라고 표기한다.\n" +
                "인코텀즈에 따라 THC를 내는 주체가 달라질 수 있으니 수출입 업무 진행 시 사전에 확인하셔야 한다."))
    TermsList.add(TermsData("서류발급비 (Document Fee)",
        "Document Fee는 서류 발급에 대한 비용으로 선사나 포워더가 B/L을 발급해 줄 때 발생한다. " +
                "Document Fee는 다른 부대비용과는 달리 B/L에 따라 발생하게 된다.\n" +
                "즉, 컨테이너 수에 상관없이 B/L 1건당 1개의 Document Fee가 발생한다."))
    TermsList.add(TermsData("선하증권 B/L (Bill of Lading)",
        "B/L (Bill of Lading, 선하증권)은 해상 수출-수입 시 화물을 판매, 구매할 때 사용하는 서류를 말한다.\n" +
                "수출, 수입 업무 진행 시 B/L 은 권리증(유가증권)으로 사용되어 수출자가 " +
                "바이어에게 화물을 판매할 때, 수입자가 제조사로부터 화물을 구매할 때 화물과 교환하게 된다.\n" +
                "원본 B/L(Original B/L)은 물류사에서 원본 3부, 은행 보관용 1부, COPY 3~5부를 1set로 " +
                "발행하게되며 발행 형태에 따라서 WAYBILL, 혹은 SURRENDERED B/L 로도 발행된다.\n" +
                "이때 B/L의 발행 주체는 선사 또는 물류사이며 " +
                "화물을 인도받으면서 발행하게 되고, 이후 화주 측에 전달하게 된다.\n" +
                "B/L은 발행 주체에 따라 실무에선 여러 이름으로 불리고 있다. " +
                "선 사에서 발행한 B/L 은 선사 B/L, MASTER B/L, MASTER DIRECT B/L과 같은 형태로 불리며 " +
                "포워더에서 발행한 B/L 은 포워더 B/L, HOUSE B/L으로 불린다."))
}