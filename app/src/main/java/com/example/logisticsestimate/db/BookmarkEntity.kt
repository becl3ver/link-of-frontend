package com.example.logisticsestimate.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookmarkEntity(
    @PrimaryKey val boardId : Long
)