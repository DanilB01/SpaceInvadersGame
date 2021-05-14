package ru.tsu.lab4.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import ru.tsu.lab4.R
import java.util.*

class BossInvader (context: Context, row: Int, column: Int, screenX: Int, screenY: Int) {
    // How wide, high and spaced out are the invader will be
    var width = screenX / 15f
    private var height = screenY / 15f
    private val padding = screenX / 45

    var position = RectF(
        column * (width + padding),
        100 + row * (width + padding / 4),
        column * (width + padding) + width,
        100 + row * (width + padding / 4) + height
    )

    var healt = 5

    // This will hold the pixels per second speed that the invader will move
    private var speed = 40f

    private val left = 1
    private val right = 2

    // Is the ship moving and in which direction
    private var shipMoving = right

    var isVisible = true

    companion object {
        // The alien ship will be represented by a Bitmap
        lateinit var bitmap1: Bitmap
        lateinit var bitmap2: Bitmap

        // keep track of the number of instances
        // that are active
        var numberOfInvaders = 0
    }

    init {
        // Initialize the bitmaps
        bitmap1 = BitmapFactory.decodeResource(
            context.resources,
            setInvaderDrawable())

        bitmap2 = BitmapFactory.decodeResource(
            context.resources,
            setInvaderDrawable())

        // stretch the first bitmap to a size
        // appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(
            bitmap1,
            (width.toInt()),
            (height.toInt()),
            false)

        // stretch the second bitmap as well
        bitmap2 = Bitmap.createScaledBitmap(
            bitmap2,
            (width.toInt()),
            (height.toInt()),
            false)

        numberOfInvaders ++
    }

    private fun setInvaderDrawable(): Int {
        return when(CurrentShip.curLvlNum) {
            4 -> R.drawable.enemy_ship_4
            5 -> R.drawable.boss_ship
            else -> R.drawable.invader1
        }
    }

    fun updateObject() {
        if (shipMoving == left) {
            position.left -= speed
        }

        if (shipMoving == right) {
            position.left += speed
        }

        position.right = position.left + width
    }

    fun dropDownAndReverse(waveNumber: Int) {
        shipMoving = if (shipMoving == left) {
            right
        } else {
            left
        }

        position.top += height
        position.bottom += height

        // The later the wave, the more the invader speeds up
        speed *=  (1.1f + (waveNumber.toFloat() / 360))
    }

    fun takeAim(playerShipX: Float,
                playerShipLength: Float,
                waves: Int)
            : Boolean {

        val generator = Random()
        var randomNumber: Int

        // If near the player consider taking a shot
        if (playerShipX + playerShipLength > position.left &&
            playerShipX + playerShipLength < position.left + width ||
            playerShipX > position.left && playerShipX < position.left + width) {

            // The fewer invaders the more each invader shoots
            // The higher the wave the more the invader shoots
            randomNumber = generator.nextInt((100 * numberOfInvaders) / waves)
            if (randomNumber == 0) {
                return true
            }

        }

        // If firing randomly (not near the player)
        randomNumber = generator.nextInt(150 * numberOfInvaders)
        return randomNumber == 0

    }
}