package com.example.catastrophic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catastrophic.model.Image
import com.example.catastrophic.model.ImageModel
import io.reactivex.Single

@Dao
interface ImageDao {

    @Query("SELECT * FROM Image LIMIT :limit OFFSET :offset")
    fun getImages(limit: Int, offset: Int) : Single<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(images: List<Image>)

}