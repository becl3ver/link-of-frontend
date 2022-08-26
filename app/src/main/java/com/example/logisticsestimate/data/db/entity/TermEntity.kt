package com.example.logisticsestimate.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 용어 사전 개체
 */
@Entity
data class TermEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "content") var content: String
)