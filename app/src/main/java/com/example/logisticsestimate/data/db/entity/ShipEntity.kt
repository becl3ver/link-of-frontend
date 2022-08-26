package com.example.logisticsestimate.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShipEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "mmsi") var mmsi: String,
    @ColumnInfo(name = "lat") var lat: Double,
    @ColumnInfo(name = "lon") var lon: Double,
    @ColumnInfo(name = "speed") var speed: Double,
    @ColumnInfo(name = "course") var course: Double,
    @ColumnInfo(name = "timestamp") var timestamp: String
)
