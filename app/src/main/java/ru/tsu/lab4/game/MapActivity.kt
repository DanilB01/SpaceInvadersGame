package ru.tsu.lab4.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_map.*
import ru.tsu.lab4.R

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        firstLevel.setOnClickListener {
            val intent = Intent(Intent(this, GameActivity::class.java))
            intent.putExtra("levelNum", 1)
            startActivity(intent)
        }

        secondLevel.setOnClickListener {
            val intent = Intent(Intent(this, GameActivity::class.java))
            intent.putExtra("levelNum", 2)
            startActivity(intent)
        }

        thirdLevel.setOnClickListener {
            val intent = Intent(Intent(this, GameActivity::class.java))
            intent.putExtra("levelNum", 3)
            startActivity(intent)
        }

        fourthLevel.setOnClickListener {
            val intent = Intent(Intent(this, GameActivity::class.java))
            intent.putExtra("levelNum", 4)
            startActivity(intent)
        }

        fifthLevel.setOnClickListener {
            val intent = Intent(Intent(this, GameActivity::class.java))
            intent.putExtra("levelNum", 5)
            startActivity(intent)
        }
    }
}