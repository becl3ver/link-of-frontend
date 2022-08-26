package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

/**
 * 게시글을 조회
 * 1. 게시판 분류에 따라 검색하는 경우, keyword, boardIdList는 null이 담겨서 전송
 * 2. 키워드에 의해 검색하는 경우, boardIdList는 null이 답겨서 전송
 * 3. 북마크한 글을 검색하는 경우, boardIdList를 제외한 나머지는 null이 담겨서 전송
 */
data class BoardRequestDto(
    @SerializedName("keyword") var keyword: String?,
    @SerializedName("range") var boardIdUpperLimit: Long?,

    @SerializedName("category") var category: Int?,
    @SerializedName("size") var responseSize: Int?,
    @SerializedName("list") var boardIdList: ArrayList<Long>?
    )