package com.esraa.shoppinglist.repo

import androidx.lifecycle.LiveData
import com.esraa.shoppinglist.data.local.ShoppingDao
import com.esraa.shoppinglist.data.local.ShoppingItem
import com.esraa.shoppinglist.data.remote.PixabayAPI
import com.esraa.shoppinglist.data.remote.responses.ImageResponse
import com.esraa.shoppinglist.other.Resource
import retrofit2.Response
import javax.inject.Inject

class DefaultShoppingRepo @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
):ShoppingRepoInterface {
    override fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeAllPrices(): LiveData<Float> {
       return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
       return try {
           val response = pixabayAPI.searchForImage(imageQuery)
           if (response.isSuccessful){
               response.body()?.let {
                   return@let Resource.success(it)
               } ?: Resource.error("Error",null)
           }else{
               Resource.error("Error",null)
           }
       }catch (e:java.lang.Exception){
           Resource.error("Error couldn't reach the server",null)
       }
    }
}