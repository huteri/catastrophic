package com.example.catastrophic.data.api

import com.example.catastrophic.data.repository.ImageRepository
import com.example.catastrophic.model.ImageResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface APIServices {

    @GET("v1/images/search?mime_types=png&&order=Desc")
    fun getImages(@Query("limit") limit: Int = ImageRepository.PER_PAGE_LIMIT, @Query("page") page: Int = 1) : Single<List<ImageResponse>>
}