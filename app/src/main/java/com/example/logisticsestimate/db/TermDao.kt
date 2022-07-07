package com.example.logisticsestimate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TermDao {
    @Query("SELECT * FROM TermEntity WHERE category LIKE :string")
    fun loadAllMatchedTerms(string: String) : Array<TermEntity>

    @Insert
    fun insertTerms(termEntity: TermEntity)

    @Delete
    fun deleteTerms(termEntity: TermEntity)
}