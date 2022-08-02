package com.example.logisticsestimate.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id : Long,
    @ColumnInfo(name="boardId") val boardId : Long
)