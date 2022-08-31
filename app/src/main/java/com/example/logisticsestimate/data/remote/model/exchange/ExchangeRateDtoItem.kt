package com.example.logisticsestimate.data.remote.model.exchange

/**
 * 환율 정보 조회 결과
 */
data class ExchangeRateDtoItem(
    val bkpr: String,
    val cur_nm: String,
    val cur_unit: String,
    val deal_bas_r: String,
    val kftc_bkpr: String,
    val kftc_deal_bas_r: String,
    val result: Int,
    val ten_dd_efee_r: String,
    val ttb: String,
    val tts: String,
    val yy_efee_r: String
)