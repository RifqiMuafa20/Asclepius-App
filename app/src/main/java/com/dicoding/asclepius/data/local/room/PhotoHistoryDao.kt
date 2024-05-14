package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.PhotoHistory

@Dao
interface PhotoHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photoHistory: PhotoHistory)

    @Query("SELECT * FROM photo_visited")
    fun getPhotoHistory(): LiveData<List<PhotoHistory>>

}