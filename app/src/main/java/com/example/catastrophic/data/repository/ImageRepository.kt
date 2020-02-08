package com.example.catastrophic.data.repository

import com.example.catastrophic.data.APIServices
import com.example.catastrophic.model.ImageModel
import io.reactivex.Observable


interface ImageRepository {
    fun getImages(page: Int = 1): Observable<List<ImageModel>>
}

class ImageRepositoryImpl constructor(val apiServices: APIServices): ImageRepository {

    override fun getImages(page: Int): Observable<List<ImageModel>> {
        return apiServices.getImages(page = page).toObservable().flatMapIterable<ImageModel> { it }.toList().toObservable()
    }

}