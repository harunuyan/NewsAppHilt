package com.volie.newsapphilt.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.volie.newsapphilt.model.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}