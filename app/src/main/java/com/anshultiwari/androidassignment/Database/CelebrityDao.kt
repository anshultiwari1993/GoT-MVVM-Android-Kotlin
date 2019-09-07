package com.anshultiwari.androidassignment.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anshultiwari.androidassignment.Model.Celebrity

@Dao
interface CelebrityDao {

    @Query("SELECT * FROM Celebrity")
    fun getAllCelebs(): LiveData<List<Celebrity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCelebs(celebs: List<Celebrity>)
}
