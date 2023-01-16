package com.esraa.shoppinglist.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.esraa.shoppinglist.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var shoppingItemDB: ShoppingItemDB

    private lateinit var dao: ShoppingDao

    @Before
    fun setup(){
        hiltRule.inject()
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