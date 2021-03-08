package com.example.simpletodo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.DB.Task
import com.example.simpletodo.databinding.TodolistItemBinding

class CalendarAdapter() : RecyclerView.Adapter<EventsViewHolder>() {
    private var events = ArrayList<Task>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TodolistItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.todolist_item, parent, false)
        return EventsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setList(task: List<Task>){
        events.clear()
        events.addAll(task)
    }
}

class EventsViewHolder(private val binding : TodolistItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(task : Task){
        binding.itemTitle.text = task.title
        binding.itemDate.text = task.timeStamp
    }
}