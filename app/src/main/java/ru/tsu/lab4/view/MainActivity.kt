package ru.tsu.lab4.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ru.tsu.lab4.*
import ru.tsu.lab4.databinding.ActivityMainBinding
import ru.tsu.lab4.game.MapActivity
import ru.tsu.lab4.viewmodel.MenuViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val playerViewModel by lazy {
        ViewModelProvider(this).get(MenuViewModel::class.java)
    }

    private val newActivityIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            when(result.resultCode) {
                Activity.RESULT_OK -> {
                    playerViewModel.currentPlayer.value = result.data?.getStringExtra("newName")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.menuViewModel = playerViewModel

        startService(Intent(this, NotificationService::class.java))

        binding.changeText.setOnClickListener {
            newActivityIntent.launch(
                Intent(this, RenameActivity::class.java)
            )
        }

        binding.startGameButton.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.storeButton.setOnClickListener {
            startActivity(Intent(this, StoreActivity::class.java))
        }

        binding.rankingButton.setOnClickListener {
            startActivity(Intent(this, RankingActivity::class.java))
        }

        binding.exitButton.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.stars.onStart()
    }

    override fun onStop() {
        binding.stars.onStop()
        super.onStop()
    }
}