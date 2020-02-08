package com.example.catastrophic.data.repository

import com.example.catastrophic.model.Image
import com.example.catastrophic.model.ImageModel
import com.example.catastrophic.model.ImageResponse
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.mockito.Mockito.`when`

class ImageRepositoryImplTest : BaseRepositoryTest() {

    private lateinit var imageRepository: ImageRepositoryImpl

    override fun setUp() {
        super.setUp()

        imageRepository = ImageRepositoryImpl(apiServies, appDatabase)
    }


    @Test
    fun `getImages will return from db if there is data`() {

        val image = Image("id", "url")
        val image2 = ImageResponse("id2", "url2")

        val diskImageList = listOf(image)
        val apiImageList = listOf(image2)


        `when`(imageDao.getImages(20, 0)).thenReturn(Single.just(diskImageList))
        `when`(apiServies.getImages()).thenReturn(Single.just(apiImageList))

        val testObserver = TestObserver<List<ImageModel>>()

        imageRepository.getImages(1).subscribe(testObserver)

        testObserver.assertValue { it.size == 1}
        testObserver.assertValue { it == diskImageList}

    }

    @Test
    fun `getImages will call api if there is no data in db`() {

        val image2 = ImageResponse("id2", "url2")

        val apiImageList = listOf(image2)

        `when`(imageDao.getImages(20, 0)).thenReturn(Single.just(emptyList()))
        `when`(apiServies.getImages()).thenReturn(Single.just(apiImageList))

        val testObserver = TestObserver<List<ImageModel>>()

        imageRepository.getImages(1).subscribe(testObserver)

        testObserver.assertValue { it.size == 1}
        testObserver.assertValue { it == apiImageList}
    }
}