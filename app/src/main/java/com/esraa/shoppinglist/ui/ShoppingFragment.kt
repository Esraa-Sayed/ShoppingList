package com.esraa.shoppinglist.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esraa.shoppinglist.R
import com.esraa.shoppinglist.adapters.ShoppingItemAdapter
import com.esraa.shoppinglist.databinding.ConfirmDialogBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject

class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter,
    var viewModel: ShoppingViewModel? = null
) : Fragment(R.layout.fragment_shopping) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        subscribeToObservers()
        setupRecyclerView()
        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            confirmDialog("Are you sure you want to delete this item ?").observe(viewLifecycleOwner) {
            if (it == true) {
                val pos = viewHolder.layoutPosition
                val item = shoppingItemAdapter.shoppingItems[pos]
                viewModel?.deleteShoppingItem(item)

                Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel?.insertShoppingItemIntoDb(item)
                        }
                        show()
                    }
            }
        }

    }
}

private fun setupRecyclerView() {
    rvShoppingItems.apply {
        adapter = shoppingItemAdapter
        layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
    }
}

private fun subscribeToObservers() {
    viewModel?.shoppingItems?.observe(viewLifecycleOwner, Observer {
        shoppingItemAdapter.shoppingItems = it
    })
    viewModel?.totalPrice?.observe(viewLifecycleOwner, Observer {
        val price = it ?: 0f
        val priceText = "Total Price: $price€"
        tvShoppingItemPrice.text = priceText
    })
}

private fun confirmDialog(title: String): LiveData<Boolean> {
    val isOk: MutableLiveData<Boolean> = MutableLiveData()
    val inflater = requireActivity().layoutInflater
    val dialog = Dialog(requireActivity())
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val bind: ConfirmDialogBinding = ConfirmDialogBinding.inflate(inflater)
    dialog.setContentView(bind.root)
    dialog.setTitle("Confirmation")
    bind.confirmText.text = title
    bind.okBtn.setOnClickListener {
        with(isOk) { postValue(true) }
        Log.i("Confirm", "in ok button")
        dialog.dismiss()
    }
    bind.cancelBtn.setOnClickListener {
        with(isOk) { postValue(false) }
        Log.i("Confirm", "in cancel button")
        dialog.dismiss()
    }

    dialog.setCanceledOnTouchOutside(true)
    dialog.show()

    return isOk
}
}