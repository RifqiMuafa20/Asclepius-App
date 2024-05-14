package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.PhotoHistory

@Database(entities = [PhotoHistory::class], version = 1, exportSchema = false)
abstract class PhotoHistoryRoomDatabase : RoomDatabase() {
    abstract fun photoHistoryDao(): PhotoHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: PhotoHistoryRoomDatabase? = null

        fun getDatabase(context: Context): PhotoHistoryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoHistoryRoomDatabase::class.java, "photo_history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}