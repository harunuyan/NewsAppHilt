package com.volie.newsapphilt.di

import android.content.Context
import androidx.room.Room
import com.volie.newsapphilt.api.RetrofitApi
import com.volie.newsapphilt.database.ArticleDatabase
import com.volie.newsapphilt.util.Constant.BASE_URL
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun injectRetrofitApi(): RetrofitApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApi::class.java)
    }

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_db"
        ).build()

    @Singleton
    @Provides
    fun injectDao(database: ArticleDatabase) = database.articleDao()
}