package com.example.logisticsestimate.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 견적 조회 정보 객체
 */
@Entity
data class EstimateHistoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "annNumber") var annNumber: String,
    @ColumnInfo(name = "annRuteName") var annRuteName: String,
    @ColumnInfo(name = "shippingPortName") var shippingPortName: String,
    @ColumnInfo(name = "landingPortName") var landingPortName: String,
    @ColumnInfo(name = "codPortName") var codPortName: String,
    @ColumnInfo(name = "transshipmentYn") var transshipmentYn: String,
    @ColumnInfo(name = "containerOwnSeName") var containerOwnSeName: String,
    @ColumnInfo(name = "containerCndName") var containerCndName: String,
    @ColumnInfo(name = "containerStdStndrdName") var containerStdStndrdName: String,
    @ColumnInfo(name = "freightName") var freightName: String,
    @ColumnInfo(name = "tnSpotSeName") var tnSpotSeName: String
)
