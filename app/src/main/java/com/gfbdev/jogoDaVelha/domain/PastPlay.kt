package com.gfbdev.jogoDaVelha.domain

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "past_plays")
data class PastPlay(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val player1 : String = "",
    val player2 : String = "",
    val winner : Int = 0,
    val date : String = "",
    val boardSize : Int = 3
)