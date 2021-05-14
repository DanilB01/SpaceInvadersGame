package ru.tsu.lab4.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tsu.lab4.model.PlayerModel

class StoreViewModel(private val app: Application): AndroidViewModel(app) {

    private val playerModel = PlayerModel(getApplication())

    var currentMoney: MutableLiveData<Int> = MutableLiveData()
    var currentSelectedShip: MutableLiveData<Int> = MutableLiveData()

    init {
        getCoins()
        currentSelectedShip.value = 1
    }

    private fun getCoins() {
        GlobalScope.launch(Dispatchers.Main) {
            val res = playerModel.getCoins()
            currentMoney.value = res
        }
    }

    fun buyNewShip() {
        val shipNum = currentSelectedShip.value
        val shipPrice = playerModel.getShipPrice(shipNum!!)
        GlobalScope.launch(Dispatchers.Main) {
                if (shipPrice <= currentMoney.value!!) {
                    playerModel.setCurrentShips(shipNum)
                    val newMoneyNum = currentMoney.value!! - shipPrice
                    currentMoney.value = newMoneyNum
                    playerModel.setCoins(newMoneyNum)
                    Toast.makeText(app, "Got it!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(app, "Not enough money to buy!", Toast.LENGTH_SHORT).show()
                }
            //}
        }
    }

}