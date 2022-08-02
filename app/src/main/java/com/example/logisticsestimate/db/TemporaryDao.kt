package com.example.logisticsestimate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TemporaryDao {
    @Query("SELECT * FROM TemporaryEntity")
    fun getAll() : List<TemporaryEntity>

    @Insert
    fun insertEntity(temporaryPost : TemporaryEntity)

    @Delete
    fun deleteEntity(vararg temporaryEntity: TemporaryEntity)
}