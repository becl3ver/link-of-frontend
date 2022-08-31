package com.example.logisticsestimate.data.db.dao

import androidx.room.*
import com.example.logisticsestimate.data.db.entity.TermEntity
import com.example.logisticsestimate.data.db.entity.TrackingHistoryEntity

/**
 * 선박 위치 조회 기록을 저장
 */
@Dao
interface TrackingHistoryDao {
    @Query("SELECT * FROM TrackingHistoryEntity")
    fun getAll() : List<TrackingHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(trackingHistoryEntity: TrackingHistoryEntity)

    @Delete
    fun delete(trackingHistoryEntity: TrackingHistoryEntity)
}