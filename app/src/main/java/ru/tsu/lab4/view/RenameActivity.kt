package ru.tsu.lab4.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.tsu.lab4.R
import ru.tsu.lab4.databinding.ActivityRenameBinding
import ru.tsu.lab4.viewmodel.RenameViewModel

class RenameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRenameBinding

    private val playerViewModel by lazy {
        ViewModelProvider(this).get(RenameViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rename)
        binding.lifecycleOwner = this
        binding.renameViewModel = playerViewModel

        playerViewModel.isFinish.observe(this, Observer {
            if(it) {
                playerViewModel.clearFinishFlag()
                setResult(Activity.RESULT_OK, intent.putExtra("newName", playerViewModel.currentPlayer.value))
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