package com.example.simpletodo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel

class CalendarViewModel : ViewModel(), Observable {

    @Bindable
    var selectedDate : String = "20년"

    fun changeDate(date : String){
        selectedDate = date
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}