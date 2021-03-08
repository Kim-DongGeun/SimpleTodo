package com.example.simpletodo

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpletodo.DB.Task
import com.example.simpletodo.DB.TaskRepository
import kotlinx.coroutines.launch

class CompletedViewModel(private val repository: TaskRepository) : ViewModel(), Observable {
    val tasks = repository.CompletedTasks

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    fun removeItem(id : Int){
        val target = tasks.value!!.filter { it.id == id }
        delete(target[0])
    }

    fun delete(task : Task){
        viewModelScope.launch {
            val newRowId : Int = repository.delete(task)
            if(newRowId > -1){
                statusMessage.value = Event("삭제 완료!")
            }
            else{
                statusMessage.value = Event("삭제 실패!")
            }
        }
    }

    fun update(task : Task){
        viewModelScope.launch {
            val newRowId : Int = repository.update(task)
            if(newRowId > -1){
                statusMessage.value = Event("끝!")
            }
            else{
                statusMessage.value = Event("수정 실패!")
            }
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}