package com.esraa.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.esraa.shoppinglist.R
import com.esraa.shoppinglist.data.local.ShoppingDao
import com.esraa.shoppinglist.data.local.ShoppingItemDB
import com.esraa.shoppinglist.data.remote.PixabayAPI
import com.esraa.shoppinglist.other.Constant.BASE_URL
import com.esraa.shoppinglist.other.Constant.DATABASE_NAME
import com.esraa.shoppinglist.repo.DefaultShoppingRepo
import com.esraa.shoppinglist.repo.ShoppingRepoInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideShoppingDB(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,ShoppingItemDB::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        shoppingItemDB: ShoppingItemDB
    ) = shoppingItemDB.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayAPI():PixabayAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }
    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayAPI
    ) = DefaultShoppingRepo(dao, api) as ShoppingRepoInterface

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )
}