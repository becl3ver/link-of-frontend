package com.example.logisticsestimate.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.logisticsestimate.data.db.entity.TemporaryEntity

/**
 * 임시 저장 글 관리
 */
@Dao
interface TemporaryDao {
    @Query("SELECT * FROM TemporaryEntity")
    fun getAll(): List<TemporaryEntity>

    @Insert
    fun insert(temporaryEntity: TemporaryEntity)

    @Delete
    fun delete(vararg temporaryEntity: TemporaryEntity)
}