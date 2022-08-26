package com.example.logisticsestimate.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 북마크 글 ID 개체
 */
@Entity
data class BookmarkEntity(
    @PrimaryKey val boardId: Long
)