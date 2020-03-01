package com.example.catastrophic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Image(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name="imageId") val imageId: String,
    @ColumnInfo(name = "url") val url: String
)  {
    fun toImageModel(): ImageModel {
        return ImageModel(imageId = id, url = url)
    }
}

data class ImageResponse(
    private val id: String,
    private val url: String
)  {

    fun toImage(): Image {
        return Image(imageId = id, url = url)
    }
}

data class ImageModel(
    var showOverlay: Boolean = false,
    val imageId: Int,
    val url: String
)
