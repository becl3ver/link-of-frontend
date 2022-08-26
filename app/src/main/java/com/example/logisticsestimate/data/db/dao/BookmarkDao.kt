package com.example.logisticsestimate.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.logisticsestimate.data.db.entity.BookmarkEntity

/**
 * 북마크 게시글 ID 관리
 */
@Dao
interface BookmarkDao {
    @Query("SELECT * FROM BookmarkEntity")
    fun getAll(): List<BookmarkEntity>

    @Query("SELECT * FROM BookmarkEntity WHERE boardId = :query")
    fun loadAllById(query : Long): List<BookmarkEntity>

    @Query("DELETE FROM BookmarkEntity")
    fun deleteAll()

    @Insert
    fun insert(entity: BookmarkEntity)

    @Delete
    fun delete(vararg entity: BookmarkEntity)
}