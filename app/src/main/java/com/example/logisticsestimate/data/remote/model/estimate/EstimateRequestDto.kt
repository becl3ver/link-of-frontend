package com.example.logisticsestimate.data.remote.model.estimate

import com.google.gson.annotations.SerializedName

/**
 * 견적 요청 정보
 */
data class EstimateRequestDto(
    @SerializedName("annNo") var annNo : String,
    @SerializedName("annRuteNm") var annRuteNm : String,
    @SerializedName("shipngPrtNm") var shippingPrtNm : String,
    @SerializedName("landngPrtNm") var landingPrtNm : String,
    @SerializedName("codPrtNm") var codPrtNm : String,
    @SerializedName("tsYn") var tsYn : String,
    @SerializedName("contnOwnSeNm") var contnOwnSeNm : String,
    @SerializedName("contnCndNm") var contnCndNm : String,
    @SerializedName("contnStdStndrdNm") var contnStdStndrdNm : String,
    @SerializedName("frghtNm") var frghtNm : String,
    @SerializedName("tnspotSeNm") var tnspotSeNm : String
)
