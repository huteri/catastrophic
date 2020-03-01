package com.example.catastrophic.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catastrophic.model.Image


@Database(entities = [Image::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}