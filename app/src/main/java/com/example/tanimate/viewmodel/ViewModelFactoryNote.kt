package com.example.tanimate.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactoryNote private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactoryNote? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactoryNote {
            if(INSTANCE == null){
                synchronized(ViewModelFactoryNote::class.java){
                    INSTANCE = ViewModelFactoryNote(application)
                }
            }
            return INSTANCE as ViewModelFactoryNote
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(NoteAddUpdateViewModel::class.java)) {
            return NoteAddUpdateViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}