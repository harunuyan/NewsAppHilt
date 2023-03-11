package com.volie.newsapphilt.api

import com.volie.newsapphilt.model.News
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApi {

    @GET("harunuyan/NewsData/main/newsheadlines.json")
    suspend fun getNews(): Response<News>

}