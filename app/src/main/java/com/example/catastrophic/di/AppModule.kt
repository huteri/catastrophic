package com.example.catastrophic.di

import android.content.Context
import androidx.room.Room
import com.example.catastrophic.data.api.APIServices
import com.example.catastrophic.data.dao.AppDatabase
import com.example.catastrophic.data.repository.ImageRepository
import com.example.catastrophic.data.repository.ImageRepositoryImpl
import com.example.catastrophic.util.schedulers.DefaultSchedulerProvider
import com.example.catastrophic.util.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Platform.get().log(Platform.INFO, message, null)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY

        val builder =  OkHttpClient.Builder()
            .addInterceptor(logging)

        return builder.build()
    }


    @Singleton
    @Provides
    fun provideApiService(client: OkHttpClient): APIServices {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(APIServices::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app-database").build()
    }

    @Singleton
    @Provides
    fun provideImageRepository(apiService: APIServices, appDatabase: AppDatabase): ImageRepository {
        return ImageRepositoryImpl(apiService, appDatabase)
    }

    @Singleton
    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return DefaultSchedulerProvider()
    }

}