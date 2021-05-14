package ru.tsu.lab4.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players_table")
data class PlayerWithScore (
    val playerName: String,
    val playerScore: Int,
    val playerCoins: Int,
    val possibleShips: Int,
    val curSpaceship: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Comparable<PlayerWithScore> {
    override fun compareTo(other: PlayerWithScore): Int = this.playerScore.compareTo(other.playerScore)
}