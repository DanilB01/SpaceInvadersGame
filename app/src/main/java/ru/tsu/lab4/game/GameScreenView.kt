package ru.tsu.lab4.game

import android.annotation.SuppressLint
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.*
import ru.tsu.lab4.SharedPreference
import ru.tsu.lab4.model.db.AppDatabase


@SuppressLint("ViewConstructor")
class GameScreenView(
    private val activity: GameActivity,
    private val size: Point
    //private val lvlNumber: Int
) : SurfaceView(activity), SurfaceHolder.Callback {

    private val prefs = SharedPreference(activity)
    private val playerName = prefs.getString("playerName")

    private var highScore = 0
    private var currentScore: Int = 0

    private var playing = false
    private var paused = true

    private val soundPlayer = SoundPlayer(context)

    private var playerShip: PlayerShip = PlayerShip(activity, size.x, size.y)
    private val invaders = ArrayList<Invader>()
    private var numInvaders = 0
    private val bricks = ArrayList<DefenceBrick>()
    private var numBricks: Int = 0
    private var playerBullet = Bullet(size.y)
    private val invadersBullets = ArrayList<Bullet>()
    private var nextBullet = 0
    private val maxInvaderBullets = 50
    private var score = 0
    private var waves = 1
    private var lives = 3

    private var bossInvader = BossInvader(context,
        size.y / 2,
        size.x / 4,
        size.x,
        size.y)

    private var menaceInterval: Long = 1000
    private var uhOrOh: Boolean = false
    private var lastMenaceTime = System.currentTimeMillis()

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        GlobalScope.launch {
            highScore = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(activity.applicationContext).playerWithScoreDao().getPlayerHighScore(
                    playerName!!
                )
            }
            withContext(Dispatchers.Default) {
                while (playing) {
                    val canvas = surfaceHolder.lockCanvas()

                    // Update the frame
                    if (!paused) {
                        playGame()
                    }

                    // Draw the frame
                    drawGameObjects(canvas)

                    surfaceHolder.unlockCanvasAndPost(canvas)
                    delay(120)
                }

            }
        }
    }
    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}
    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}

    init {
        holder.addCallback(this)
    }

    private fun prepareLevel() {
        // Here we will initialize the game objects
        // Build an army of invaders
        Invader.numberOfInvaders = 0
        numInvaders = 0
        for (column in 0..10) {
            for (row in 0..5) {
                invaders.add(
                    Invader(
                        context,
                        row,
                        column,
                        size.x,
                        size.y
                    )
                )

                numInvaders++
            }
        }

        // Build the shelters
        numBricks = 0
        for (shelterNumber in 0..4) {
            for (column in 0..18) {
                for (row in 0..8) {
                    bricks.add(
                        DefenceBrick(
                            row,
                            column,
                            shelterNumber,
                            size.x,
                            size.y
                        )
                    )

                    numBricks++
                }
            }
        }

        // Initialize the invadersBullets array
        for (i in 0 until maxInvaderBullets) {
            invadersBullets.add(Bullet(size.y))
        }
    }

    private fun playGame() {
        // Update the state of all the game objects

        // Move the player's ship
        playerShip.updateObject()

        // Did an invader bump into the side of the screen
        var bumped = false

        // Has the player lost
        var lost = false

        // Update all the invaders if visible
        for (invader in invaders) {

            if (invader.isVisible) {
                // Move the next invader
                invader.updateObject()

                // Does he want to take a shot?
                if (invader.takeAim(
                        playerShip.position.left,
                        playerShip.width,
                        waves
                    )) {

                    // If so try and spawn a bullet
                    if (invadersBullets[nextBullet].shoot(
                            invader.position.left
                                    + invader.width / 2,
                            invader.position.top, playerBullet.down
                        )) {

                        // Shot fired
                        // Prepare for the next shot
                        nextBullet++

                        // Loop back to the first one if we have reached the last
                        if (nextBullet == maxInvaderBullets) {
                            // This stops the firing of bullet
                            // until one completes its journey
                            // Because if bullet 0 is still active
                            // shoot returns false.
                            nextBullet = 0
                        }
                    }
                }

                // If that move caused them to bump
                // the screen change bumped to true
                if (invader.position.left > size.x - invader.width
                    || invader.position.left < 0) {

                    bumped = true

                }
            }
        }

        // Update the players playerBullet
        if (playerBullet.isActive) {
            playerBullet.updateObject()
        }

        // Update all the invaders bullets if active

        for (bullet in invadersBullets) {
            if (bullet.isActive) {
                bullet.updateObject()
            }
        }

        // Did an invader bump into the edge of the screen
        if (bumped) {

            // Move all the invaders down and change direction
            for (invader in invaders) {
                invader.dropDownAndReverse(waves)
                // Have the invaders landed
                if (invader.position.bottom >= size.y && invader.isVisible) {
                    lost = true
                }
            }
        }

        // Has the player's playerBullet hit the top of the screen
        if (playerBullet.position.bottom < 0) {
            playerBullet.isActive =false
        }

        // Has an invaders playerBullet hit the bottom of the screen
        for (bullet in invadersBullets) {
            if (bullet.position.top > size.y) {
                bullet.isActive = false
            }
        }

        // Has the player's playerBullet hit an invader
        if (playerBullet.isActive) {
            for (invader in invaders) {
                if (invader.isVisible) {
                    if (RectF.intersects(playerBullet.position, invader.position)) {
                        invader.isVisible = false

                        if(prefs.getBool("keyMusic")){
                            soundPlayer.playSound(SoundPlayer.invaderExplodeID)
                        }

                        playerBullet.isActive = false
                        Invader.numberOfInvaders --
                        score += 100
                        if(score > currentScore){
                            currentScore = score
                        }

                        // Has the player cleared the level
                        //if (score == numInvaders * 10 * waves) {
                        if (Invader.numberOfInvaders == 0) {
                            paused = true
                            lives ++
                            invaders.clear()
                            bricks.clear()
                            invadersBullets.clear()
                            prepareLevel()
                            waves ++
                            break
                        }

                        // Don't check any more invaders
                        break
                    }
                }
            }
        }

        // Has an alien playerBullet hit a shelter brick
        for (bullet in invadersBullets) {
            if (bullet.isActive) {
                for (brick in bricks) {
                    if (brick.isVisible) {
                        if (RectF.intersects(bullet.position, brick.position)) {
                            // A collision has occurred
                            bullet.isActive = false
                            brick.isVisible = false
                            if(prefs.getBool("keyMusic")){
                                soundPlayer.playSound(SoundPlayer.damageShelterID)
                            }
                        }
                    }
                }
            }

        }

        // Has a player playerBullet hit a shelter brick
        if (playerBullet.isActive) {
            for (brick in bricks) {
                if (brick.isVisible) {
                    if (RectF.intersects(playerBullet.position, brick.position)) {
                        // A collision has occurred
                        playerBullet.isActive = false
                        brick.isVisible = false
                        if(prefs.getBool("keyMusic")){
                            soundPlayer.playSound(SoundPlayer.damageShelterID)
                        }
                    }
                }
            }
        }

        // Has an invader playerBullet hit the player ship
        for (bullet in invadersBullets) {
            if (bullet.isActive) {
                if (RectF.intersects(playerShip.position, bullet.position)) {
                    bullet.isActive = false
                    lives --
                    if(prefs.getBool("keyMusic")){
                        soundPlayer.playSound(SoundPlayer.playerExplodeID)
                    }

                    // Is it game over?
                    if (lives == 0) {
                        lost = true
                        break
                    }
                }
            }
        }

        if (lost) {
            paused = true
            lives = 3
            score = 0
            waves = 1
            invaders.clear()
            bricks.clear()
            invadersBullets.clear()
            prepareLevel()
        }
    }

    private fun drawGameObjects(canvas: Canvas) {
        val paint: Paint = Paint()
        // Make sure our drawing surface is valid or the game will crash
        if (holder.surface.isValid) {
            // Lock the canvas ready to draw
            //canvas = holder.lockCanvas()

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Choose the brush color for drawing
            paint.color = Color.argb(255, 0, 255, 0)

            // Draw all the game objects here
            // Now draw the player spaceship
            canvas.drawBitmap(
                playerShip.bitmap, playerShip.position.left,
                playerShip.position.top, paint
            )

            // Draw the invaders
            for (invader in invaders) {
                if (invader.isVisible) {
                    if (uhOrOh) {
                        canvas.drawBitmap(
                            Invader.bitmap1,
                            invader.position.left,
                            invader.position.top,
                            paint
                        )
                    } else {
                        canvas.drawBitmap(
                            Invader.bitmap2,
                            invader.position.left,
                            invader.position.top,
                            paint
                        )
                    }
                }
            }

            // Draw the bricks if visible
            for (brick in bricks) {
                if (brick.isVisible) {
                    canvas.drawRect(brick.position, paint)
                }
            }

            // Draw the players playerBullet if active
            if (playerBullet.isActive) {
                canvas.drawRect(playerBullet.position, paint)
            }

            // Draw the invaders bullets
            for (bullet in invadersBullets) {
                if (bullet.isActive) {
                    canvas.drawRect(bullet.position, paint)
                }
            }

            // Draw the score and remaining lives
            // Change the brush color
            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = 70f
            canvas.drawText(
                "Score: $score HI: $highScore", 20f, 75f, paint
            )

            // Draw everything to the screen
            //holder.unlockCanvasAndPost(canvas)
        }
    }

    // If SpaceInvadersActivity is paused/stopped
    // then shut down our thread.
    fun pause() {
        playing = false
        //val oldHighScore = prefs.getInt(playerName!!)
        if(currentScore > highScore) {
            //prefs.saveInt(playerName, highScore)
            highScore = currentScore
            activity.saveNewScore(playerName!!, highScore)
        }
        activity.saveCoins(playerName!!, currentScore)
        currentScore = 0
    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    fun resume() {
        playing = true
        prepareLevel()
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val motionArea = size.y - (size.y / 8)
        when (motionEvent.action and MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            // Or moved their finger while touching screen
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                paused = false

                if (motionEvent.y > motionArea && prefs.getBool("keyMode")) {
                    if (motionEvent.x > size.x / 2) {
                        playerShip.moving = PlayerShip.right
                    } else {
                        playerShip.moving = PlayerShip.left
                    }

                }

                if ((motionEvent.y < motionArea && prefs.getBool("keyMode")) ||
                    !prefs.getBool("keyMode")
                ) {
                    // Shots fired
                    if (playerBullet.shoot(
                            playerShip.position.left + playerShip.width / 2f,
                            playerShip.position.top,
                            playerBullet.up
                        )
                    ) {
                        if (prefs.getBool("keyMusic")) {
                            soundPlayer.playSound(SoundPlayer.shootID)
                        }

                    }
                }
            }

            // Player has removed finger from screen
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                if (motionEvent.y > motionArea && prefs.getBool("keyMode")) {
                    playerShip.moving = PlayerShip.stopped
                }
            }

        }
        return true
    }

    fun movePlayerShip(degree: Float) {
        //Log.println(Log.INFO, "Degree value", "The value of degree is $degree")
        //paused = false
        if(prefs.getBool("keyMode")) return
        when {
            degree > 1 -> playerShip.moving = PlayerShip.left
            degree < -1 -> playerShip.moving = PlayerShip.right
            else -> playerShip.moving = PlayerShip.stopped
        }
    }
}