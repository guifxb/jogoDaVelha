package com.gfbdev.jogoDaVelha.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gfbdev.jogoDaVelha.domain.PastPlay

@Dao
interface PastPlaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pastPlay: PastPlay)

    @Query("SELECT * FROM past_plays")
    suspend fun getAll(): List<PastPlay>

    @Query("DELETE FROM past_plays")
    suspend fun deleteAll()

    @Query("SELECT * FROM past_plays WHERE winner = :winner")
    suspend fun get(winner: String): PastPlay


}
