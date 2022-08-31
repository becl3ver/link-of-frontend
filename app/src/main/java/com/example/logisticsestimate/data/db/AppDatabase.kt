package com.example.logisticsestimate.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.logisticsestimate.data.db.dao.*
import com.example.logisticsestimate.data.db.entity.*

/**
 * 룸 지속성 라이브러리 상속 및 정의
 */
@Database(entities = [TemporaryEntity::class, TermEntity::class, BookmarkEntity::class, TrackingHistoryEntity::class, EstimateHistoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTemporaryPostDao(): TemporaryDao
    abstract fun getTermDao() : TermDao
    abstract fun getBookmarkDao() : BookmarkDao
    abstract fun getEstimateHistoryDao() : EstimateHistoryDao
    abstract fun getTrackingHistoryDao() : TrackingHistoryDao

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