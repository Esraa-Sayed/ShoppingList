package com.esraa.shoppinglist.ui

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.ui.ShoppingViewModel
import com.esraa.shoppinglist.getOrAwaitValueTest
import com.esraa.shoppinglist.other.Constant
import com.esraa.shoppinglist.other.Status
import com.esraa.shoppinglist.repo.FakeShoppingRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel
    @Before
    fun init() {
        viewModel = ShoppingViewModel(FakeShoppingRepo())
    }

    @Test
    fun insertShoppintItemWithEmptyFieldReturnErrorTest(){
        viewModel.insertShoppingItem("Bag","","30")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun insertShoppintItemWithTooLongNameFieldReturnErrorTest(){
        val string = buildString {
            for (i in 1..Constant.MAX_NAME_LENGTH +1){
                append(1)
            }
        }
        viewModel.insertShoppingItem(string,"5","30")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun insertShoppintItemWithTooLongPricaFieldReturnErrorTest(){
        val string = buildString {
            for (i in 1..Constant.MAX_PRICE_LENGTH +1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("name","5",string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun insertShoppintItemWithTooLongAmountFieldReturnErrorTest(){
        val string = buildString {
            for (i in 1..Constant.MAX_NAME_LENGTH +1){
                append(1)
            }
        }
        viewModel.insertShoppingItem(string,"9999999999999999999","30")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun insertShoppintItemWithValidItemReturnSuccessTest(){
        viewModel.insertShoppingItem("name","5","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun insertShoppintItemWithValidItemImageURlWillBeEmptyTest(){
        viewModel.insertShoppingItem("Bag","5","3.0")
        val value = viewModel.curImageUrl.getOrAwaitValueTest()
        assertThat(value).isEmpty()
    }


}