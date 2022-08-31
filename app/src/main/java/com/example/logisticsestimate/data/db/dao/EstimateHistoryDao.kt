package com.example.logisticsestimate.data.db.dao

import androidx.room.*
import com.example.logisticsestimate.data.db.entity.EstimateHistoryEntity

/**
 * 견적 검색 기록을 저장
 */
@Dao
interface EstimateHistoryDao {
    @Query("SELECT * FROM EstimateHistoryEntity")
    fun getAll() : List<EstimateHistoryEntity>

    @Query(
        "SELECT * FROM EstimateHistoryEntity WHERE" +
                ":annNumber = annNumber and" +
                ":annRuteName = annRuteName and" +
                ":shippingPortName = shippingPortName and" +
                ":landingPortName = landingPortName and" +
                ":codPortName = codPortName and" +
                ":transshipmentYn = transshipmentYn and" +
                ":containerOwnSeName = containerOwnSeName and" +
                ":containerCndName = containerCndName and" +
                ":containerStdStndrdName = containerStdStndrdName and" +
                ":freightName = freightName and" +
                ":tnSpotSeName = tnSpotSeName"
    )
    fun isExist(
        annNumber: String,
        annRuteName: String,
        shippingPortName: String,
        landingPortName: String,
        codPortName: String,
        transshipmentYn: String,
        containerOwnSeName: String,
        containerCndName: String,
        containerStdStndrdName: String,
        freightName: String,
        tnSpotSeName: String
    ): List<EstimateHistoryEntity>

    @Insert
    fun insert(estimateHistoryEntity: EstimateHistoryEntity)

    @Delete
    fun delete(estimateHistoryEntity: EstimateHistoryEntity)
}