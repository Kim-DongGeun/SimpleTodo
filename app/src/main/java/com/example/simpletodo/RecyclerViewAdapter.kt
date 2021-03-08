package com.example.simpletodo

import android.icu.util.IslamicCalendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.EntityDeletionOrUpdateAdapter
import com.example.simpletodo.DB.Task
import com.example.simpletodo.databinding.TodolistItemBinding
import com.example.simpletodo.generated.callback.OnClickListener

class RecyclerViewAdapter(private val taskViewModel: TaskViewModel, private val completedViewModel: CompletedViewModel,
private val clickListener: (Task) -> Unit) : RecyclerView.Adapter<MyViewHolder>() {
    private var taskList = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TodolistItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.todolist_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(taskList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }


    fun setList(task: List<Task>){
        taskList.clear()
        taskList.addAll(task)
    }

    fun deleteItem(pos: Int){
        taskViewModel.deleteTask(taskList[pos].id)
        notifyItemRemoved(pos)
    }

    fun removeItem(pos : Int){
        completedViewModel.removeItem(taskList[pos].id)
        notifyItemRemoved(pos)
    }

    fun delayItem(pos : Int){
        taskViewModel.delayTask(taskList[pos].id)
        notifyItemChanged(pos)
    }

    fun completeItem(pos : Int){
        taskViewModel.updateTask(taskList[pos].id, true)
        notifyItemChanged(pos)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) : Boolean{
        val temp : Task = taskList[fromPosition]
        taskList.removeAt(fromPosition)
        taskList.add(toPosition, temp)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}


class MyViewHolder(val binding: TodolistItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(task: Task, clickListener: (Task) -> Unit){
        binding.itemTitle.text = task.title
        binding.itemDate.text = task.timeStamp
        binding.listItemLayout.setOnClickListener {
            clickListener(task)
        }
    }
}