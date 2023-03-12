package com.volie.newsapphilt.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.volie.newsapphilt.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles")
    fun deleteAllSaved()

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>
}