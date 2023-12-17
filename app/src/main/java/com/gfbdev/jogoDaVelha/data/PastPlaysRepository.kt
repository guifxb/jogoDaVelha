package com.gfbdev.jogoDaVelha.data

import com.gfbdev.jogoDaVelha.domain.PastPlay

class PastPlaysRepository(private val pastPlaysDao: PastPlaysDao) {

    suspend fun getAll() = pastPlaysDao.getAll()

    suspend fun get(winner: String) = pastPlaysDao.get(winner)

    suspend fun insert(pastPlay: PastPlay) = pastPlaysDao.insert(pastPlay)

    suspend fun deleteAll() = pastPlaysDao.deleteAll()

}




