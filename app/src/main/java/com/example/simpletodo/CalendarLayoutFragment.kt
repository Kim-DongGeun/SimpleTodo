package com.example.simpletodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.simpletodo.databinding.FragmentCalendarBinding
import com.example.simpletodo.databinding.FragmentCalendarLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarLayoutFragment : Fragment() {
    private lateinit var binding : FragmentCalendarLayoutBinding
    private lateinit var calendarFragmentBinding : FragmentCalendarBinding
    private lateinit var adapter : CalendarLayoutAdapter
    private lateinit var currentDate : Date
    private lateinit var calendarViewModel : CalendarViewModel
    lateinit var date : Date
    var pageIndex = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar_layout, container, false)
        calendarFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        calendarFragmentBinding.viewModel = calendarViewModel
        calendarFragmentBinding.lifecycleOwner = this

        initView()
        return binding.root
    }

    fun initView(){
        pageIndex -= (Int.MAX_VALUE / 2)
        date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA).run{
            add(Calendar.MONTH, pageIndex)
            time
        }
        currentDate = date
        val datetime : String = SimpleDateFormat("y년 M월", Locale.KOREA).format(date.time)
        binding.calendarYearMonthText.text = datetime
        //calendarViewModel.changeDate(datetime)
        adapter = CalendarLayoutAdapter(binding.calendarLayout, currentDate) { selectedItem : Int -> listItemClicked(selectedItem) }
        binding.calendarView.adapter = adapter



    }

    private fun listItemClicked(date : Int){
        val datetime : String = binding.calendarYearMonthText.text.toString() + date.toString() + "일"
        calendarViewModel.changeDate(datetime)
    }

}