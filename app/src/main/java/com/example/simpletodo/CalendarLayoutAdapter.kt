package com.example.simpletodo

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.databinding.CalendarDayBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarLayoutAdapter(val calendarLayout: LinearLayout, val date: Date, private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<CalendarLayoutAdapter.CalendarItemHolder>(){
    var dataList : ArrayList<Int> = arrayListOf()

    var furangCalendar : FurangCalendar = FurangCalendar(date)
    init{
        furangCalendar.initBaseCalendar()
        dataList = furangCalendar.dateList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemHolder {
        var binding : CalendarDayBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.calendar_day, parent, false)
        return CalendarItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarItemHolder, position: Int) {
        val h = calendarLayout.height / 6
        val w = calendarLayout.width / 7
        holder.itemView.layoutParams.height = h
        holder.itemView.layoutParams.width = w

        holder.bind(dataList[position], position, clickListener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CalendarItemHolder(val binding : CalendarDayBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: Int, position: Int, clickListener: (Int) -> Unit){
            val firstDateIndex = furangCalendar.prevTail
            val lastDateindex = dataList.size - furangCalendar.nextHead - 1

            binding.exThreeDayText.text = data.toString()
            binding.exThreeDayText.background = null

            val dateString = SimpleDateFormat("dd", Locale.KOREA).format(date)
            val dateInt = dateString.toInt()
            if(dataList[position] == dateInt){
                binding.exThreeDayText.setTypeface(binding.exThreeDayText.typeface, Typeface.BOLD)
            }

            if(position < firstDateIndex || position > lastDateindex){
                binding.exThreeDayText.setTextColor(Color.parseColor("#CCCCCC"))
                binding.exThreeDotView.background = null
            }

            binding.dayLayout.setOnClickListener {
                clickListener(data)
            }
        }

    }

}


