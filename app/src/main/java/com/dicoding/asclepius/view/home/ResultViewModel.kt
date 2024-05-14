package com.dicoding.asclepius.view.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.dicoding.asclepius.data.local.entity.PhotoHistory
import com.dicoding.asclepius.data.local.room.PhotoHistoryDao
import com.dicoding.asclepius.data.local.room.PhotoHistoryRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    private var photoHistoryDao: PhotoHistoryDao?
    private var photoHistoryDb: PhotoHistoryRoomDatabase? =
        PhotoHistoryRoomDatabase.getDatabase(application)

    init {
        photoHistoryDao = photoHistoryDb?.photoHistoryDao()
    }

    fun addVisited(photoName: String, result: String, inference: String, date: String, photo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val photoHistory = PhotoHistory(
                photoName = photoName, result = result, inference = inference, date = date, photo = photo
            )
            photoHistoryDao?.insert(photoHistory)
        }
    }

    companion object {
        private const val TAG = "ResultViewModel"
    }
}