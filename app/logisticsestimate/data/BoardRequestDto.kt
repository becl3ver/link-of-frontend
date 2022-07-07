package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class BoardRequestDto( // 토큰 필요
    @SerializedName("nickname") var nickname: String?, // 이름으로 검색
    @SerializedName("keyword") var keyword: String?, // 키워드로 검색
    @SerializedName("range") var range: Long?, // 특정한 글 번호보다 번호가 작은 글들만 검색

    @SerializedName("category") var category: Int?, // 글 카테고리 지정
    @SerializedName("size") var size: Int? // 응답 사이즈 명시
    )