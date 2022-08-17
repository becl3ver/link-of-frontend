package com.example.logisticsestimate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM BookmarkEntity")
    fun getAll() : List<BookmarkEntity>

    @Query("SELECT * FROM BookmarkEntity WHERE boardId = :query")
    fun selectEntity(query : Long) : List<BookmarkEntity>

    @Query("DELETE FROM BookmarkEntity")
    fun truncateEntity()

    @Insert
    fun insertEntity(entity : BookmarkEntity)

    @Delete
    fun deleteEntity(vararg entity : BookmarkEntity)
}