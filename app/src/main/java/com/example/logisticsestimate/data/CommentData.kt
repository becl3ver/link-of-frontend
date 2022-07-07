package com.example.logisticsestimate.data

import android.provider.ContactsContract

data class CommentData(
    val isNested : Boolean,
    val parentId : Long,
    val nickname: String,
    val uid : Long,
    val date : String,
    val id : Long
)
