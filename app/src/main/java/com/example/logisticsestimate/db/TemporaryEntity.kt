package com.example.logisticsestimate.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TemporaryEntity (
    @PrimaryKey(autoGenerate = true) var id : Long,
    @ColumnInfo(name="category") var category : Int,
    @ColumnInfo(name="title") var title : String,
    @ColumnInfo(name="content") var content : String,
    @ColumnInfo(name="date") var date : String
    )