package ru.tsu.lab4.game

import android.graphics.RectF

class Bullet(screenY: Int) {

    private val heightModifier: Float = 50f
    private val speed: Float = 500f
    val position = RectF()

    // Which way is it shooting
    val up = 0
    val down = 1

    // Going nowhere
    private var heading = -1

    private val width = 2
    private var height = screenY / heightModifier

    var isActive = false

    fun shoot(startX: Float, startY: Float, direction: Int): Boolean {
        if (!isActive) {
            position.left = startX
            position.top = startY
            position.right = position.left + width
            position.bottom = position.top + height
            heading = direction
            isActive = true
            return true
        }

        // Bullet already active
        return false
    }

    fun updateObject() {

        // Just move up or down
        if (heading == up) {
            position.top -= speed
        } else {
            position.top += speed
        }

        // Update the bottom position
        position.bottom = position.top + height
    }
}