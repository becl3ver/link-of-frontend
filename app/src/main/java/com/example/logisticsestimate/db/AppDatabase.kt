package com.example.logisticsestimate.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TemporaryEntity::class, TermEntity::class, BookmarkEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTemporaryPostDao(): TemporaryDao
    abstract fun getTermDao() : TermDao
    abstract fun getBookmarkDao() : BookmarkDao

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