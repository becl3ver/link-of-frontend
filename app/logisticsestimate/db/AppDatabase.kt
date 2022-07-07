package com.example.logisticsestimate.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TemporaryPostEntity::class, TermEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTemporaryPostDado(): TemporaryPostDao
    abstract fun getTermDao() : TermDao

    companion object {
        private const val databaseName = "db_logistics_estimate"
        var appDatabase: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (appDatabase == null) {
                appDatabase =
                    Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
            }
            return appDatabase
        }
    }
}