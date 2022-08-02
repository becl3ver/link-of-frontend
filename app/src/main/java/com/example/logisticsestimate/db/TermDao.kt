package com.example.logisticsestimate.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface TermDao {

    @Query("SELECT * FROM TermEntity WHERE name LIKE '%' || :query || '%'")
    fun getQueryTerms(query: String) : List<TermEntity>

    @Query("SELECT * FROM TermEntity")
    fun getAllTerms() : List<TermEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTerm(term : TermEntity)
}