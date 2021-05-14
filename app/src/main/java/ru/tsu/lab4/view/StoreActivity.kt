package ru.tsu.lab4.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ru.tsu.lab4.Item
import ru.tsu.lab4.ItemAdapter
import ru.tsu.lab4.R
import ru.tsu.lab4.databinding.ActivityStoreBinding
import ru.tsu.lab4.viewmodel.StoreViewModel

class StoreActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStoreBinding
    private val playerViewModel by lazy {
        ViewModelProvider(this).get(StoreViewModel::class.java)
    }

    private val itemAdapter by lazy {
        ItemAdapter { position: Int, item: Item ->
            playerViewModel.currentSelectedShip.value = position + 1
            binding.itemList.smoothScrollToPosition(position) //сглаживание анимации
        }
    }
    private val possibleItems = listOf( //список возможных иконок
        Item(
            "100",
            R.drawable.first_ship
        ),
        Item(
            "150",
            R.drawable.base_ship
        ),
        Item(
            "200",
            R.drawable.third_ship
        ),
        Item(
            "250",
            R.drawable.second_ship
        ),
        Item(
            "300",
            R.drawable.fourth_ship
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_store)
        binding.lifecycleOwner = this
        binding.storeViewModel = playerViewModel

        binding.itemList.initialize(itemAdapter)
        binding.itemList.setViewsToChangeColor(listOf(R.id.listItemText))
        itemAdapter.setItems(getLargeListOfItems())
    }

    override fun onStart() {
        super.onStart()
        binding.stars.onStart()
    }

    override fun onStop() {
        binding.stars.onStop()
        super.onStop()
    }

    private fun getLargeListOfItems(): List<Item> {
        val items = mutableListOf<Item>()
        for (element in possibleItems) {
            items.add(element)
        }
        return items
    }
}