package com.anshultiwari.androidassignment.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anshultiwari.androidassignment.Model.Celebrity

@Database(entities = [Celebrity::class], version = 1, exportSchema = false)
abstract class CelebDatabase : RoomDatabase() {
    abstract fun celebrityDao(): CelebrityDao

    companion object {

        private var instance: CelebDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CelebDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, CelebDatabase::class.java, "celeb_database")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance as CelebDatabase
        }
    }
}
