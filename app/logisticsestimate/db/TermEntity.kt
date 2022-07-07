package com.example.logisticsestimate.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TermEntity(
    @PrimaryKey(autoGenerate = true) var id : Int? = null,
    @ColumnInfo(name="category") var category: String,
    @ColumnInfo(name="name") var name: String,
    @ColumnInfo(name="content") var content : String
    )