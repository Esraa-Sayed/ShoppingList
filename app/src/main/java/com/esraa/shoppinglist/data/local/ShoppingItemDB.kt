package com.esraa.shoppinglist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItem::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingItemDB :RoomDatabase(){
    abstract fun shoppingDao():ShoppingDao
}