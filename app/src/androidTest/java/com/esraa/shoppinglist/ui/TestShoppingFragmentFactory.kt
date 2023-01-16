package com.androiddevs.shoppinglisttestingyt.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.esraa.shoppinglist.adapters.ImageAdapter
import com.esraa.shoppinglist.adapters.ShoppingItemAdapter
import com.esraa.shoppinglist.repositories.FakeShoppingRepositoryAndroidTest
import com.esraa.shoppinglist.ui.AddShoppingItemFragment
import com.esraa.shoppinglist.ui.ImagePickFragment
import com.esraa.shoppinglist.ui.ShoppingFragment
import com.esraa.shoppinglist.ui.ShoppingViewModel
import javax.inject.Inject

class TestShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemAdapter: ShoppingItemAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemAdapter,
                ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
            )
            else -> super.instantiate(classLoader, className)
        }
    }
}