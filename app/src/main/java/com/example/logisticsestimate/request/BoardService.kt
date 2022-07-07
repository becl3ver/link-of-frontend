package com.example.logisticsestimate.request

import com.example.logisticsestimate.data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface BoardService {
    @POST("/auth/board/new")
    fun putNewBoard(@Header("Authorization") token: String, @Body boardDto: BoardDto) : Call<String>
    
    @POST("/board/search")
    fun getBoards(@Body boardRequestDto: BoardRequestDto) : Call<BoardResponseDto>

    @POST("/auth/board/comment/new")
    fun putNewComment(@Header("token") token: String, @Body commentDto: CommentDto) : Call<String>

    @POST("/board/comment/search")
    fun getComments(@Body commentRequestDto: CommentRequestDto) : Call<CommentResponseDto>
}