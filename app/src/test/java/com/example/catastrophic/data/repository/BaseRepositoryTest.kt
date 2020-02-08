package com.example.catastrophic.data.repository

import com.example.catastrophic.data.api.APIServices
import com.example.catastrophic.data.dao.AppDatabase
import com.example.catastrophic.data.dao.ImageDao
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

open class BaseRepositoryTest {

    @Mock
    lateinit var apiServies: APIServices

    @Mock
    lateinit var appDatabase: AppDatabase

    @Mock
    lateinit var imageDao: ImageDao

    @Before
    open fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(appDatabase.imageDao()).thenReturn(imageDao)
    }


}

