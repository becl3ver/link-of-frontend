package com.example.logisticsestimate.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.logisticsestimate.data.db.entity.ShipEntity

@Dao
interface ShipDao {
    @Query("SELECT * FROM ShipEntity")
    fun getAll(): List<ShipEntity>

    @Insert
    fun insert(shipEntity: ShipEntity)

    @Query("DELETE FROM ShipEntity")
    fun deleteAll()
}