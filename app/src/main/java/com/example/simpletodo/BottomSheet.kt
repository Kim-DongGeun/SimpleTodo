package com.example.simpletodo

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.provider.CalendarContract
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import com.example.simpletodo.DB.Task
import com.example.simpletodo.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import kotlin.math.exp

class BottomSheet(private var task : Task, private val taskViewModel: TaskViewModel) : BottomSheetDialogFragment(){
    private lateinit var binding: BottomSheetDialogBinding

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog, container, false)
        initNumberPicker()
        return binding.root
    }

    fun initNumberPicker(){
        binding.bottomSheetText.text = Editable.Factory.getInstance().newEditable(task.title)
        binding.timeTextView.setOnClickListener {
            var cal : Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA)
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)

                binding.timeTextView.text = SimpleDateFormat("a hh시 mm분").format(cal.time)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }

        binding.dateTextView.setOnClickListener {
            var cal : Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA)
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.dateTextView.text = SimpleDateFormat("M월 d일").format(cal.time)
            }
            DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.buttonOk.setOnClickListener {
            task.title = binding.bottomSheetText.text.toString()
            taskViewModel.updateTitle(task.id, task.title)
            dismiss()
        }
    }

}