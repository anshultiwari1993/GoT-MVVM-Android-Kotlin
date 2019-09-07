package com.anshultiwari.androidassignment.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Celebrity(@field:PrimaryKey
                val name: String, val height: String, val age: String, val popularity: String, val imageUrl: String)
