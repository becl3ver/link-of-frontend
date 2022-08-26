package com.example.logisticsestimate.data.db.dao

import androidx.room.*
import com.example.logisticsestimate.data.db.entity.TermEntity

/**
 * 용어 관리
 */
@Dao
interface TermDao {
    @Query("SELECT * FROM TermEntity WHERE title LIKE '%' || :query || '%'")
    fun getAllByTitle(query: String): List<TermEntity>

    @Query("SELECT * FROM TermEntity")
    fun getAll(): List<TermEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(term: TermEntity)
}