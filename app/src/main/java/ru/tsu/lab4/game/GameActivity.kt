package ru.tsu.lab4.game

import android.content.Context
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.tsu.lab4.SharedPreference
import ru.tsu.lab4.model.db.AppDatabase


class GameActivity: AppCompatActivity() {

    private lateinit var db : AppDatabase
    private lateinit var prefs: SharedPreference
    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null
    private var gameView: GameScreenView? = null

    //private var lvlNumber : Int? = null

    private val mSensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            val degrees = event!!.values
            gameView?.movePlayerShip(degrees[0])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDatabase.getDatabase(applicationContext)
        prefs = SharedPreference(this)
        val lvlNumber = intent.extras?.getInt("levelNum")
        CurrentShip.curLvlNum = lvlNumber!!

        // присвоили менеджеру работу с серсором
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Get a Display object to access screen details
        val display = windowManager.defaultDisplay
        // Load the resolution into a Point object
        val size = Point()
        display.getSize(size)

        gameView = GameScreenView(this, size)
        setContentView(gameView)
        //gameView.playing = true
    }

    // This method executes when the player starts the game
    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST)
        // Tell the gameView resume method to execute
        gameView?.resume()
    }

    // This method executes when the player quits the game
    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(mSensorEventListener)
        // Tell the gameView pause method to execute
        gameView?.pause()
    }

    fun saveNewScore(name: String, score: Int) {
        GlobalScope.launch {
            db.playerWithScoreDao().updateHighScore(name, score)
        }
    }

    fun saveCoins(name: String, coins: Int) {
        GlobalScope.launch {
            db.playerWithScoreDao().updateCoins(coins, name)
        }
    }

}