package com.esraa.shoppinglist.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.esraa.shoppinglist.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var shoppingItemDB: ShoppingItemDB
    private lateinit var dao: ShoppingDao

    @Before
    fun setup(){
        shoppingItemDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDB::class.java
        ).allowMainThreadQueries().build()
        dao = shoppingItemDB.shoppingDao()
    }

    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("Bag",1,100f,"url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).contains(shoppingItem)
    }
    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("Bag",1,100f,"url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }
    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem = ShoppingItem("Bag",1,100f,"url", id = 1)
        val shoppingItem2 = ShoppingItem("Bag",1,200f,"url", id = 2)
        val shoppingItem3 = ShoppingItem("Bag",1,300f,"url", id = 3)
        dao.insertShoppingItem(shoppingItem)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val allShoppingItems = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(allShoppingItems).isEqualTo(600)
    }
    @After
    fun tearDown(){
        shoppingItemDB.close()
    }

}