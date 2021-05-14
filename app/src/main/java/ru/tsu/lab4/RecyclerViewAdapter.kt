package ru.tsu.lab4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ranking.view.*
import ru.tsu.lab4.model.db.PlayerWithScore

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var scores: Array<PlayerWithScore> = arrayOf()

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(noteModel: PlayerWithScore){
            itemView.playerNameText.text = noteModel.playerName
            itemView.scoreText.text = noteModel.playerScore.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_ranking,
            parent,
            false
        )
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scores.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(scores[position])
    }

    fun refreshScores(scores: Array<PlayerWithScore>) {
        this.scores = scores
        notifyDataSetChanged()
    }
}