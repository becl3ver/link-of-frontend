package com.example.logisticsestimate.data.remote.api.service

import com.example.logisticsestimate.data.remote.model.board.*
import com.example.logisticsestimate.data.remote.model.comment.CommentDeleteDto
import com.example.logisticsestimate.data.remote.model.comment.CommentDto
import com.example.logisticsestimate.data.remote.model.comment.CommentRequestDto
import com.example.logisticsestimate.data.remote.model.comment.CommentResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 게시글 및 댓글 통신
 */

interface BoardService {
    @POST("/auth/board/new")
    fun putNewBoard(@Header("Authorization") token: String, @Body boardDto: BoardDto): Call<String>
    
    @POST("/board/search")
    fun getBoards(@Body boardRequestDto: BoardRequestDto): Call<BoardResponseDto>

    @POST("/auth/comment/new")
    fun addNewComment(@Header("Authorization") token: String, @Body commentDto: CommentDto): Call<String>

    @POST("/comment/search")
    fun getComments(@Body commentRequestDto: CommentRequestDto): Call<CommentResponseDto>

    @POST("/auth/board/update")
    fun updateBoard(@Header("Authorization") token: String, @Body boardUpdateDto: BoardUpdateDto): Call<String>

    @POST("/auth/board/delete")
    fun deleteBoard(@Header("Authorization") token: String, @Body boardDeleteDto: BoardDeleteDto): Call<String>

    @POST("/auth/board/recommend")
    fun putRecommendBoard(@Header("Authorization") token: String, @Body boardRecommendDto: BoardRecommendDto): Call<String>

    @POST("/auth/comment/delete")
    fun deleteComment(@Header("Authorization") token: String, @Body commentDeleteDto: CommentDeleteDto): Call<String>

    @POST("/board/image")
    fun getImage(@Body boardImageRequestDto: BoardImageRequestDto): Call<String>
}