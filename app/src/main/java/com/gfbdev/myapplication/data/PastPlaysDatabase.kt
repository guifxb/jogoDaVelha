package com.gfbdev.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gfbdev.myapplication.domain.PastPlay


@Database(entities = [PastPlay::class], version = 2, exportSchema = false)
abstract class PastPlaysDatabase : RoomDatabase() {

    abstract fun pastPlaysDao(): PastPlaysDao

    companion object {
        @Volatile
        private var Instance: PastPlaysDatabase? = null

        fun getDatabase(context: Context): PastPlaysDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,PastPlaysDatabase::class.java, "past_plays_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}