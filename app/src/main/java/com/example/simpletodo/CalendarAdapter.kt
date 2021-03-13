package com.example.simpletodo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.DB.Task
import com.example.simpletodo.databinding.TodolistItemBinding

class CalendarAdapter(private val clickListener: (Task) -> Unit) : RecyclerView.Adapter<MyViewHolder>() {
    private var events = ArrayList<Task>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TodolistItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.todolist_item, parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(events[position], clickListener)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setList(task: List<Task>){
        events.clear()
        events.addAll(task)
    }
}

