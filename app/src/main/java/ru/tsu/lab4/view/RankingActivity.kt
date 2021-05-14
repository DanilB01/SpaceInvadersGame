package ru.tsu.lab4.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.tsu.lab4.R
import ru.tsu.lab4.RecyclerViewAdapter
import ru.tsu.lab4.databinding.ActivityRankingBinding
import ru.tsu.lab4.viewmodel.RankViewModel

class RankingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRankingBinding
    private val playerViewModel by lazy {
        ViewModelProvider(this).get(RankViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ranking)
        binding.lifecycleOwner = this
        binding.rankViewModel = playerViewModel

        val adapter = RecyclerViewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        playerViewModel.getPlayersScore().observe(this, Observer {
            it?.let {
                adapter.refreshScores(it)
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