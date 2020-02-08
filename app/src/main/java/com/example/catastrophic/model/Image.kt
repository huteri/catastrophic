package com.example.catastrophic.model

data class ImageResponse(
    private val id: String,
    private val url: String
) : ImageModel {
    override fun getUrl(): String {
        return url
    }

    override fun getId(): String = id
}


interface ImageModel {
    fun getId(): String
    fun getUrl(): String
}