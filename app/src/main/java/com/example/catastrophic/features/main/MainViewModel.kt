package com.example.catastrophic.features.main

import androidx.lifecycle.MutableLiveData
import com.example.catastrophic.features.base.BaseViewModel
import com.example.catastrophic.data.repository.ImageRepository
import com.example.catastrophic.model.ImageModel
import com.example.catastrophic.util.schedulers.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class MainViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider, private val imageRepository: ImageRepository
) : BaseViewModel(schedulerProvider) {
    private var currentPage = 1
    private var currentList = mutableListOf<ImageModel>()

    private var isZoomIn = false

    val imageListLiveData = MutableLiveData<List<ImageModel>>()
    val needtoZoomIn = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun loadImageList() {
        compositeDisposable.add(
            imageRepository.getImages(currentPage)
                .compose(applySchedulers())
                .subscribe({
                    currentList.addAll(it)

                    imageListLiveData.value = currentList
                }, { it.printStackTrace() })
        )
    }

    fun loadMore() {
        currentPage++
        loadImageList()
    }

    fun pinchList(scaleFactor: Float) {

        if (scaleFactor > 1 && !isZoomIn) {
            isZoomIn = true
            needtoZoomIn.value = true
        } else if (scaleFactor < 1 && isZoomIn) {
            isZoomIn = false
            needtoZoomIn.value = false
        }

    }

    fun updatOverlay(showOverlay: MutableList<Int>) {
        currentList.forEachIndexed { index, imageModel -> imageModel.showOverlay = showOverlay.contains(index) }
        imageListLiveData.value = currentList
    }

}