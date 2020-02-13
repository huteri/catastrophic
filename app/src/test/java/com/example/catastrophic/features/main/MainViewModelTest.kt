package com.example.catastrophic.features.main

import com.example.catastrophic.features.BaseViewModelTest
import com.example.catastrophic.model.ImageModel
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class MainViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: MainViewModel

    override fun setUp() {
        super.setUp()
        viewModel = MainViewModel(scheduler, imageRepository)

        initLiveData()
    }

    @Test
    fun `loadImageList will return with all list`() {

        val image = mock(ImageModel::class.java)
        val imageList = listOf<ImageModel>(image, image, image)

        `when`(imageRepository.getImages(1)).thenReturn(Observable.just(imageList))

        viewModel.loadImageList()

        assertEquals(viewModel.imageListLiveData.value?.first(), image)
        assertEquals(viewModel.imageListLiveData.value?.size, 3)

    }

    @Test
    fun `loadMore will load second page of image list`() {
        val image = mock(ImageModel::class.java)
        val imageListPage1 = listOf<ImageModel>(image, image, image)
        val imageListPage2 = listOf<ImageModel>(image, image, image)

        `when`(imageRepository.getImages(1)).thenReturn(Observable.just(imageListPage1))
        `when`(imageRepository.getImages(2)).thenReturn(Observable.just(imageListPage2))

        viewModel.loadImageList()
        viewModel.loadMore()

        assertEquals(viewModel.imageListLiveData.value?.size, 6)
    }

    @Test
    fun `pinch list with scale factory more than 1 and isZoomIn false will need to zoom in`() {

        viewModel.pinchList(1.1f)
        assertEquals(viewModel.needtoZoomIn.value, true)

    }

    @Test
    fun `pinch list with scale factor less than 1 and isZoomIn true will need to zoom out`() {

        //set isZoomIn to true
        viewModel.pinchList(1.1f)

        viewModel.pinchList(0.9f)
        assertEquals(viewModel.needtoZoomIn.value, false)

    }


    private fun initLiveData() {
        viewModel.imageListLiveData.observeForever { }
    }
}
