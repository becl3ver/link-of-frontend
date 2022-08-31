package com.example.logisticsestimate.data.remote.model.estimate

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 견적 조회 응답
 */
data class EstimateResponseDto (
    @SerializedName("cychgOf") var Of : String,                 // 운임 OF
    @SerializedName("cychgOfCurCd") var ofCode : String,
    @SerializedName("cychgBaf") var Baf : String,               // 운임 BAF
    @SerializedName("cychgBafCurCd") var bafCode : String,
    @SerializedName("cychgCaf") var Caf : String,               // 운임 CAF
    @SerializedName("cychgCafCurCd") var cafCode : String,
    @SerializedName("cychgLss") var Lss : String,               // 운임 LSS
    @SerializedName("cychgLssCurCd") var lssCode : String,
    @SerializedName("cychgEbs") var Ebs : String,               // 운임 EBS
    @SerializedName("cychgEbsCurCd") var ebsCode : String,
    @SerializedName("cychgOthc") var Othc : String,             // 운임 OTHC
    @SerializedName("cychgOthcCurCd") var othcCode : String,
    @SerializedName("cychgDthc") var Dthc : String,             // 운임 DTHC
    @SerializedName("cychgDthcCurCd") var dthcCode : String,
    @SerializedName("cychgDf") var Df : String,                 // 서류 발급비
    @SerializedName("cychgDfCurCd") var dfCode : String,
    @SerializedName("cychgDo") var Do : String,                 // 화물인도 지시서
    @SerializedName("cychgDoCurCd") var doCode : String,
    @SerializedName("cychgWaf") var Waf : String,               // 부두 사용료
    @SerializedName("cychgWafCurCd") var wafCode : String,
    @SerializedName("cychgSc") var Sc : String,                 // 컨테이너 봉인료
    @SerializedName("cychgScCurCd") var scCode : String,
    @SerializedName("cychgEtc") var Etc : String,               // 운임 기타
    @SerializedName("cychgEtcCurCd") var etcCode : String
) :Serializable
