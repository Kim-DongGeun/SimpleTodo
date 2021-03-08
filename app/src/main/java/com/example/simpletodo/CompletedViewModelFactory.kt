package com.example.simpletodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simpletodo.DB.TaskRepository
import java.lang.IllegalArgumentException

class CompletedViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CompletedViewModel::class.java)){
            return CompletedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}