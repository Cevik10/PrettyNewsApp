package com.hakancevik.newsappbihaber.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hakancevik.newsappbihaber.R
import com.hakancevik.newsappbihaber.api.NewsAPI
import com.hakancevik.newsappbihaber.repo.NewsRepository
import com.hakancevik.newsappbihaber.repo.NewsRepositoryImpl
import com.hakancevik.newsappbihaber.roomdb.NewsDao
import com.hakancevik.newsappbihaber.roomdb.NewsDatabase
import com.hakancevik.newsappbihaber.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideNewsAPI(): NewsAPI {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()


        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(NewsAPI::class.java)
    }


    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, NewsDatabase::class.java, "NewsDB"
        ).build()

    @Singleton
    @Provides
    fun provideDao(database: NewsDatabase) = database.newsDao()


    @Singleton
    @Provides
    fun provideNormalRepo(dao: NewsDao, api: NewsAPI) = NewsRepositoryImpl(dao, api) as NewsRepository


    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.news_placeholder)
                .error(R.drawable.news_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )

    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


}