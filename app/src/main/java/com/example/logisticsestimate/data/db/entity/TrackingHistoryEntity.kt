package com.example.logisticsestimate.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 선박 위치 조회 정보 객체
 */
@Entity
data class TrackingHistoryEntity(
    @ColumnInfo(name = "category") var category: Int,
    @PrimaryKey(autoGenerate = false) var shipId: String
)