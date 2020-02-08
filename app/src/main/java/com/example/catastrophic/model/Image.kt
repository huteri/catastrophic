package com.example.catastrophic.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Image(
    @PrimaryKey @NonNull val id: String,
    @ColumnInfo(name = "url") private val url: String
) : ImageModel {
    override fun getImageId(): String {
        return id
    }

    override fun getUrl(): String {
        return url
    }
}

data class ImageResponse(
    private val id: String,
    private val url: String
) : ImageModel {
    override fun getUrl(): String {
        return url
    }

    override fun getImageId(): String = id

    fun toImage(): Image {
        return Image(id, url)
    }
}


interface ImageModel {
    fun getImageId(): String
    fun getUrl(): String
}