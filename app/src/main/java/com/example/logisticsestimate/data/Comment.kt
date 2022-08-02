package com.example.logisticsestimate.data

data class Comment(
    val type : Int,
    val content : String?,
    val date : String?,
    val nickname : String?,
    val id : Long?,
    val uid : Long?,
    val parentUid : Long?,
    val parentId : Long?
)