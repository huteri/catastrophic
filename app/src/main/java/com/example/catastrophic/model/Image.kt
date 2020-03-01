package com.example.catastrophic.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Image(
    @PrimaryKey @NonNull val id: String,
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
        return Image(id, url)
    }
}

data class ImageModel(
    var showOverlay: Boolean = false,
    val imageId: String,
    val url: String
)
