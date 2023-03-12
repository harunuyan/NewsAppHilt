package com.volie.newsapphilt.repo

import com.volie.newsapphilt.api.RetrofitApi
import com.volie.newsapphilt.database.ArticleDao
import com.volie.newsapphilt.model.Article
import com.volie.newsapphilt.model.News
import com.volie.newsapphilt.util.Resource
import javax.inject.Inject

class Repository
@Inject constructor(
    private val articleDao: ArticleDao,
    private val retrofitApi: RetrofitApi
) {

    suspend fun insertArticle(article: Article) {
        articleDao.upsert(article)
    }

    suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }

    fun getNewsFromLocal() = articleDao.getAllArticles()


    suspend fun getBrekingNewsFromRemote(): Resource<News> {
        return try {
            val response = retrofitApi.getNews()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error!", null)
            } else {
                Resource.error("Error!", null)
            }
        } catch (e: Exception) {
            Resource.error("No data!", null)
        }
    }


}