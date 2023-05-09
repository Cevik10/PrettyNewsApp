package com.hakancevik.newsappbihaber.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hakancevik.newsappbihaber.model.Article
import com.hakancevik.newsappbihaber.util.Converters


@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}