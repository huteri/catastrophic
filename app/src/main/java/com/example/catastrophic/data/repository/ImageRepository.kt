package com.example.catastrophic.data.repository

import com.example.catastrophic.data.api.APIServices
import com.example.catastrophic.data.dao.AppDatabase
import com.example.catastrophic.model.ImageModel
import io.reactivex.Observable
import io.reactivex.Single


interface ImageRepository {
    fun getImages(page: Int = 1): Observable<List<ImageModel>>

    companion object {
        const val PER_PAGE_LIMIT = 20
    }
}

class ImageRepositoryImpl(
    val apiServices: APIServices,
    val appDatabase: AppDatabase
) : ImageRepository {

    override fun getImages(page: Int): Observable<List<ImageModel>> {

        val disk = appDatabase.imageDao()
            .getImages(ImageRepository.PER_PAGE_LIMIT, ImageRepository.PER_PAGE_LIMIT * (page-1))
        var api = apiServices.getImages().doOnSuccess { list ->
            appDatabase.imageDao().insertOrUpdate(list.map { it.toImage() })
        }

        return disk.flatMap {
            if(it.isEmpty()) {
                api
            } else {
                Single.just(it)
            }
        }.toObservable()
    }

}