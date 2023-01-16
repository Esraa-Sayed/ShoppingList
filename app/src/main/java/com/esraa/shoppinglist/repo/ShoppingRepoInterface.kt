package com.esraa.shoppinglist.repo

import androidx.lifecycle.LiveData
import com.esraa.shoppinglist.data.local.ShoppingItem
import com.esraa.shoppinglist.data.remote.responses.ImageResponse
import com.esraa.shoppinglist.other.Resource

interface ShoppingRepoInterface {
    suspend fun  insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItems():LiveData<List<ShoppingItem>>
    fun observeAllPrices():LiveData<Float>

    suspend fun searchForImage(imageQuery:String):Resource<ImageResponse>
}