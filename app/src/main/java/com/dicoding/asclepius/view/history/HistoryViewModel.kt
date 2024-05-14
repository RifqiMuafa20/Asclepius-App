package com.dicoding.asclepius.view.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.PhotoHistory
import com.dicoding.asclepius.data.local.room.PhotoHistoryDao
import com.dicoding.asclepius.data.local.room.PhotoHistoryRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private var photoVisitedDao : PhotoHistoryDao? = null
    private var photoVisitedDb : PhotoHistoryRoomDatabase? =
        PhotoHistoryRoomDatabase.getDatabase(application)

    init {
        photoVisitedDao = photoVisitedDb?.photoHistoryDao()
    }

    fun getAllPhotoVisited(): LiveData<List<PhotoHistory>>? {
        return photoVisitedDao?.getPhotoHistory()
    }

    fun deleteAllPhotoVisited() {
        CoroutineScope(Dispatchers.IO).launch {
            photoVisitedDao?.deleteAll()
        }
    }
}