package ru.tsu.lab4.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tsu.lab4.R
import ru.tsu.lab4.model.PlayerModel

class PlayerShip(context: Context,
                 private val screenX: Int,
                 screenY: Int) {

    private fun getShipDrawable(): Int {
        return when(CurrentShip.curShipNumber) {
            1 -> R.drawable.first_ship
            2 -> R.drawable.base_ship
            3 -> R.drawable.second_ship
            4 -> R.drawable.third_ship
            5 -> R.drawable.fourth_ship
            else -> R.drawable.playership
        }
    }

    // The player ship will be represented by a Bitmap
    var bitmap: Bitmap = BitmapFactory.decodeResource(
            context.resources,
            getShipDrawable())

    // How wide and high our ship will be
    val width = screenX / 20f
    val height = screenY / 20f

    // This keeps track of where the ship is
    val position = RectF(
            screenX /2f,
            screenY-height,
            screenX/2 + width,
            screenY.toFloat())

    // This will hold the pixels per second speed that the ship will move
    private val speed  = 100f

    // This data is accessible using ClassName.propertyName
    companion object {
        // Which ways can the ship move
        const val stopped = 0
        const val left = 1
        const val right = 2
    }

    // Is the ship moving and in which direction
    // Start off stopped
    var moving = stopped

    init{
        // stretch the bitmap to a size
        // appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                width.toInt() ,
                height.toInt() ,
                false)
    }

    // This update method will be called from update in
    // KotlinInvadersView It determines if the player's
    // ship needs to move and changes the coordinates
    fun updateObject() {
        // Move as long as it doesn't try and leave the screen
        if (moving == left && position.left > 0) {
            position.left -= speed
        }

        else if (moving == right && position.left < screenX - width) {
            position.left += speed
        }

        position.right = position.left + width
    }

}