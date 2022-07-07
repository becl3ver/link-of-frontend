package com.example.logisticsestimate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TemporaryPostDao {
    @Query("SELECT * FROM TemporaryPostEntity")
    fun getAll() : List<TemporaryPostEntity>

    @Insert
    fun insertTemporaryPost(temporaryPost : TemporaryPostEntity)

    @Delete
    fun deleteTemporaryPost(temporaryPost: TemporaryPostEntity)
}