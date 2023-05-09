package com.hakancevik.newsappbihaber.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hakancevik.newsappbihaber.model.Article

@Dao
interface NewsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long


    @Delete
    suspend fun deleteArticle(article: Article)


    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>


}