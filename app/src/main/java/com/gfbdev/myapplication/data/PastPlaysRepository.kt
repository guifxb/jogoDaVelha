package com.gfbdev.myapplication.data

import com.gfbdev.myapplication.domain.PastPlay

class PastPlaysRepository(private val pastPlaysDao: PastPlaysDao) {

    suspend fun getAll() = pastPlaysDao.getAll()

    suspend fun get(winner: String) = pastPlaysDao.get(winner)

    suspend fun insert(pastPlay: PastPlay) = pastPlaysDao.insert(pastPlay)

    suspend fun deleteAll() = pastPlaysDao.deleteAll()

}




