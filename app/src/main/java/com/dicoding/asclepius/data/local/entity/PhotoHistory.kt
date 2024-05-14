package com.dicoding.asclepius.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "photo_visited")
data class PhotoHistory(
    @PrimaryKey @ColumnInfo(name = "photoName") val photoName: String,
    @ColumnInfo(name = "result") val result: String,
    @ColumnInfo(name = "inference") val inference: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "photo") val photo: String
) : Serializable