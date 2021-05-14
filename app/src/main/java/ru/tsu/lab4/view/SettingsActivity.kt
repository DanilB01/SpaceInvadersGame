package ru.tsu.lab4.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.tsu.lab4.R
import ru.tsu.lab4.databinding.ActivitySettingsBinding
import ru.tsu.lab4.viewmodel.SettingViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private val playerViewModel by lazy {
        ViewModelProvider(this).get(SettingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.lifecycleOwner = this
        binding.settingViewModel = playerViewModel

        playerViewModel.isFinish.observe(this, Observer {
            if(it) {
                playerViewModel.clearFinishFlag()
                finish()
            }
        })
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