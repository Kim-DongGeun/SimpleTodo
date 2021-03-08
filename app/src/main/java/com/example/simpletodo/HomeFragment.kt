package com.example.simpletodo

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.simpletodo.DB.Task
import com.example.simpletodo.DB.TaskDatabase
import com.example.simpletodo.DB.TaskRepository
import com.example.simpletodo.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var todayAdapter: RecyclerViewAdapter
    private lateinit var completeAdapter: RecyclerViewAdapter
    private lateinit var taskViewModel : TaskViewModel
    private lateinit var completedViewModel: CompletedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val dao = TaskDatabase.getInstance(requireContext()).taskDAO
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)

        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)
        val factory2 = CompletedViewModelFactory(repository)
        completedViewModel = ViewModelProvider(this, factory2).get(CompletedViewModel::class.java)
        //ViewModel 전달
        homeBinding.viewModel = taskViewModel
        homeBinding.lifecycleOwner = this
        // 리사이클러뷰 어댑터 초기화
        initRecyclerView()
        // 토스트 메시지 처리
        taskViewModel.message.observe(viewLifecycleOwner, Observer {
            //키보드 숨기기
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(homeBinding.addTaskText.windowToken, 0)
            // 글자 굵기 재설정
            homeBinding.addTaskNextWeek.setTypeface(null, Typeface.NORMAL)
            homeBinding.addTaskSetTime.setTypeface(null, Typeface.NORMAL)
            homeBinding.addTaskTomorrow.setTypeface(null, Typeface.NORMAL)
            homeBinding.addTaskToday.setTypeface(null, Typeface.NORMAL)
            homeBinding.addTaskSetTime.text = "시간 설정"

            it.getContentIfNotHandled()?.let{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        homeBinding.apply {
            addTaskTomorrow.setOnClickListener {
                homeBinding.addTaskNextWeek.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskSetTime.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskToday.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskSetTime.text = "시간 설정"
                homeBinding.addTaskTomorrow.setTypeface(null, Typeface.BOLD)


                taskViewModel.setDateNextDay()
            }

            addTaskNextWeek.setOnClickListener {
                homeBinding.addTaskTomorrow.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskSetTime.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskToday.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskSetTime.text = "시간 설정"
                homeBinding.addTaskNextWeek.setTypeface(null, Typeface.BOLD)

                taskViewModel.setDateNextWeek()
            }

            addTaskToday.setOnClickListener {
                homeBinding.addTaskTomorrow.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskSetTime.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskNextWeek.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskSetTime.text = "시간 설정"
                homeBinding.addTaskToday.setTypeface(null, Typeface.BOLD)

                taskViewModel.setDateToday()
            }

            addTaskSetTime.setOnClickListener {
                homeBinding.addTaskTomorrow.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskNextWeek.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskToday.setTypeface(null, Typeface.NORMAL)
                homeBinding.addTaskSetTime.setTypeface(null, Typeface.BOLD)
                var totalDate : String = ""

                val cal : Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA)
                val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)

                        homeBinding.addTaskSetTime.text = homeBinding.addTaskSetTime.text.toString() + SimpleDateFormat(" a hh시 mm분", Locale.KOREA).format(cal.time)
                        taskViewModel.setDateNTime(totalDate + SimpleDateFormat(" a hh시 mm분", Locale.KOREA).format(cal.time))
                    }
                    val tp = TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false)
                    tp.setOnShowListener{
                        tp.getButton(Dialog.BUTTON_NEGATIVE).visibility = View.GONE
                    }
                    tp.setOnCancelListener {
                        homeBinding.addTaskSetTime.text = "시간 설정"
                        homeBinding.addTaskSetTime.setTypeface(null, Typeface.NORMAL)
                    }

                    tp.show()

                    homeBinding.addTaskSetTime.text = SimpleDateFormat("M월 d일", Locale.KOREA).format(cal.time)
                    totalDate += SimpleDateFormat("y년 M월 d일", Locale.KOREA).format(cal.time)
                }
                val dp = DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                dp.setOnCancelListener {
                    homeBinding.addTaskSetTime.text = "시간 설정"
                    homeBinding.addTaskSetTime.setTypeface(null, Typeface.NORMAL)
                }
                dp.show()
            }

        }

        return homeBinding.root
    }
    // 리사이클러뷰 초기화
    private fun initRecyclerView(){
        todayAdapter = RecyclerViewAdapter(taskViewModel, completedViewModel, {selectedItem: Task->listItemClicked(selectedItem)})
        homeBinding.todayRecyclerView.adapter = todayAdapter
        completeAdapter = RecyclerViewAdapter(taskViewModel, completedViewModel, {selectedItem: Task->listItemClicked(selectedItem)})
        homeBinding.completeRecyclerView.adapter = completeAdapter

        var itemTouchHelper = ItemTouchHelper(TaskSwipeToDelete(todayAdapter))
        itemTouchHelper.attachToRecyclerView(homeBinding.todayRecyclerView)
        itemTouchHelper = ItemTouchHelper(CompletedSwipeToDelete(completeAdapter))
        itemTouchHelper.attachToRecyclerView(homeBinding.completeRecyclerView)

        displayTaskList()
    }
    // 오늘 할일 화면에 출력
    private fun displayTaskList(){
        val date = SimpleDateFormat("y년 M월 d일", Locale.KOREA)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA)
        val curDate = date.format(cal.time)
        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            todayAdapter.setList(it.filter { it.dateStamp == curDate })
            todayAdapter.notifyDataSetChanged()
        })

        completedViewModel.tasks.observe(viewLifecycleOwner, Observer {
            completeAdapter.setList(it.filter{it.dateStamp == curDate})
            completeAdapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(task : Task){
        val bottomSheet = BottomSheet(task, taskViewModel)
        bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
    }

}