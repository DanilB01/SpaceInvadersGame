package ru.tsu.lab4.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerWithScoreDao {
    @Insert
    fun addPlayer(player: PlayerWithScore)

    @Delete
    fun deletePlayer(player: PlayerWithScore)

    @Query("SELECT * FROM players_table")
    fun getAll(): Array<PlayerWithScore>

    @Query("UPDATE players_table SET playerCoins = :coins WHERE playerName = :name")
    fun updateCoins(coins: Int, name: String)

    @Query("UPDATE players_table SET playerScore = :score WHERE playerName = :name")
    fun updateHighScore(name: String, score: Int)

    @Query("SELECT playerScore FROM players_table WHERE playerName LIKE :name LIMIT 1")
    fun getPlayerHighScore(name: String): Int

    @Query("SELECT playerCoins FROM players_table WHERE playerName LIKE :name LIMIT 1")
    fun getPlayerCoins(name: String): Int

    @Query("SELECT curSpaceship FROM players_table WHERE playerName LIKE :name LIMIT 1")
    fun getCurSpaceship(name: String): Int

    @Query("UPDATE players_table SET curSpaceship = :newShip WHERE playerName LIKE :name")
    fun setCurSpaceship(name: String, newShip: Int)

    @Query("SELECT possibleShips FROM players_table WHERE playerName LIKE :name LIMIT 1")
    fun getPossibleSpaceships(name: String): Int

    @Query("UPDATE players_table SET possibleShips = :newShip WHERE playerName LIKE :name")
    fun setPossibleSpaceships(name: String, newShip: Int)
}