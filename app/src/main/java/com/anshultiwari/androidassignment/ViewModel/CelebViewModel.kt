package com.anshultiwari.androidassignment.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anshultiwari.androidassignment.Model.Celebrity
import com.anshultiwari.androidassignment.Repository.CelebRepository

class CelebViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CelebRepository
//    private val celebs: LiveData<List<Celebrity>>

    init {
        repository = CelebRepository(application)
//        celebs = repository.getAllCelebs()
    }

    fun fetchCelebsAndStore() {
        repository.celebsApi()
    }

    fun getAllCelebs(): LiveData<List<Celebrity>> {
        return repository.getAllCelebs()
    }

}
