package com.example.simpletodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simpletodo.DB.TaskDatabase
import com.example.simpletodo.DB.TaskRepository
import com.example.simpletodo.databinding.CalendarDayBinding
import com.example.simpletodo.databinding.CalendarHeaderBinding
import com.example.simpletodo.databinding.FragmentCalendarBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var calendarBinding : FragmentCalendarBinding
    private lateinit var dayBinding: CalendarDayBinding
    private lateinit var headerBinding : CalendarHeaderBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var calendarAdapter: CalendarAdapter
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.KOREAN)
    private val titleFormatter = DateTimeFormatter.ofPattern("yyyy년 MMM", Locale.KOREAN)
    private val selectionFormatter = DateTimeFormatter.ofPattern("yyy년 MMM d일", Locale.KOREAN)

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        val binding = CalendarDayBinding.bind(view)

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    selectDate(day.date)
                    calendarAdapter.setList(taskViewModel.tasks.value!!.filter {
                        val formatter = DateTimeFormatter.ofPattern("y년 M월 d일")
                        it.dateStamp == formatter.format(day.date)
                    })
                    calendarAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_calendar,
            container,
            false
        )
        dayBinding = DataBindingUtil.inflate(inflater, R.layout.calendar_day, container, false)
        headerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.calendar_header,
            container,
            false
        )

        val dao = TaskDatabase.getInstance(requireContext()).taskDAO
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)

        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)


        return calendarBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarAdapter = CalendarAdapter()
        calendarBinding.exThreeRv.adapter = calendarAdapter

        // 달력에 출력할 날짜들 지정
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        calendarBinding.exThreeCalendar.apply {
            setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }
        // 스크롤 시 월 변경
        calendarBinding.exThreeCalendar.monthScrollListener = {
            calendarBinding.monthText.text = if(it.year == today.year){
                        titleSameYearFormatter.format(it.yearMonth)
                    }
                    else{
                        titleFormatter.format(it.yearMonth)
                    }
                    selectDate(it.yearMonth.atDay(1))
                }
        // 선택되는 날 색깔 변경
        calendarBinding.exThreeCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.exThreeDayText
                val dotView = container.binding.exThreeDotView
                textView.text = day.date.dayOfMonth.toString()

                if(day.owner == DayOwner.THIS_MONTH){
                    textView.makeVisible()
                    when(day.date){
                        today -> {
                            textView.setTextColorRes(R.color.example_3_white)
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.makeVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.example_3_blue)
                            textView.setBackgroundResource(R.drawable.selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_3_black)
                            textView.background = null
                            //dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                        }
                    }
                }
                else{
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view){
            val legendLayout = headerBinding.legendLayout
        }
        // 요일 지정
        calendarBinding.exThreeCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View): MonthViewContainer {
                return MonthViewContainer(view)
            }
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                if(container.legendLayout.tag == null){
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map {it as TextView }.forEachIndexed { index, textView ->
                        textView.text = daysOfWeek[index].getDisplayName(
                            TextStyle.NARROW,
                            Locale.KOREAN
                        )
                        textView.setTextColorRes(R.color.example_3_black)
                    }
                }
            }
        }

        displayTaskList()
    }

    private fun displayTaskList(){
        val date = SimpleDateFormat("y년 M월 d일", Locale.KOREA)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
        val curDate = date.format(cal.time)
        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            calendarAdapter.setList(it.filter { it.dateStamp == curDate })
            calendarAdapter.notifyDataSetChanged()
        })
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { calendarBinding.exThreeCalendar.notifyDateChanged(it) }
            calendarBinding.exThreeCalendar.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        /*eventsAdapter.apply {
            events.clear()
            events.addAll(this@CalendarFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }*/
        calendarBinding.exThreeSelectedDateText.text = selectionFormatter.format(date)
    }
}


