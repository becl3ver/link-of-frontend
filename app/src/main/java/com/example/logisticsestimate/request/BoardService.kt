package com.example.logisticsestimate.request

import com.example.logisticsestimate.data.board.*
import com.example.logisticsestimate.data.comment.CommentDeleteDto
import com.example.logisticsestimate.data.comment.CommentDto
import com.example.logisticsestimate.data.comment.CommentRequestDto
import com.example.logisticsestimate.data.comment.CommentResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BoardService {
    @POST("/auth/board/new")
    fun putNewBoard(@Header("Authorization") token: String, @Body boardDto: BoardDto) : Call<String>
    
    @POST("/board/search")
    fun getBoards(@Body boardRequestDto: BoardRequestDto) : Call<BoardResponseDto>

    @POST("/auth/comment/new")
    fun putNewComment(@Header("Authorization") token: String, @Body commentDto: CommentDto) : Call<String>

    @POST("/comment/search")
    fun getComments(@Body commentRequestDto: CommentRequestDto) : Call<CommentResponseDto>

    @POST("/auth/board/update")
    fun updateBoard(@Header("Authorization") token : String, @Body boardUpdateDto: BoardUpdateDto) : Call<String>

    @POST("/auth/board/delete")
    fun deleteBoard(@Header("Authorization") token : String, @Body boardDeleteDto: BoardDeleteDto) : Call<String>

    @POST("/auth/comment/delete")
    fun deleteComment(@Header("Authorization") token : String, @Body commentDeleteDto: CommentDeleteDto) : Call<String>
}