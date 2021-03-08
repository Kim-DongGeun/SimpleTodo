package com.example.simpletodo

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpletodo.DB.Task
import com.example.simpletodo.DB.TaskRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TaskViewModel(private val repository: TaskRepository) : ViewModel(), Observable {
    val tasks = repository.UnCompletedTasks

    @Bindable
    val inputTitle = MutableLiveData<String>()
    @Bindable
    var curDate : String = "시간 설정"
    var setTime : Boolean = false


    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    fun addTask(){
        if(inputTitle.value == null){
            statusMessage.value = Event("입력칸이 비었어요!")
        }
        else if(curDate == "시간 설정"){
            statusMessage.value = Event("날짜를 지정해주세요!")
        }
        else{
            val title = inputTitle.value!!
            val dateFormat = SimpleDateFormat("y년 M월 d일 a hh시 mm분", Locale.KOREA)
            val temp : Date = dateFormat.parse(curDate)!!
            val dateFormat2 = SimpleDateFormat("a hh시 mm분", Locale.KOREA)
            var time : String = dateFormat2.format(temp.time)
            val dateFormat3 = SimpleDateFormat("y년 M월 d일", Locale.KOREA)
            val date : String = dateFormat3.format(temp.time)
            if(!setTime) time = ""
            insert(Task(0, title, date, time, false))
            inputTitle.value = null
        }
    }

    //시간 날짜 설정
    fun setDateNTime(date : String){
        curDate = date
        setTime = true
    }

    // 내일로 날짜 설정
    fun setDateNextDay(){
        val cal : Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        curDate = SimpleDateFormat("y년 M월 d일 a hh시 mm분", Locale.KOREA).format(cal.time)
        setTime = false
    }

    // 다음주로 날짜 설정
    fun setDateNextWeek(){
        val cal : Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA)
        cal.add(Calendar.DAY_OF_MONTH, 7)
        curDate = SimpleDateFormat("y년 M월 d일 a hh시 mm분", Locale.KOREA).format(cal.time)
        setTime = false
    }

    // 오늘로 날짜 설정
    fun setDateToday(){
        val cal : Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"),Locale.KOREA)
        curDate = SimpleDateFormat("y년 M월 d일 a hh시 mm분", Locale.KOREA).format(cal.time)
        setTime = false
    }

    fun deleteTask(id : Int){
        val target = tasks.value!!.filter { it.id == id }
        delete(target[0])
    }

    fun delayTask(id : Int){
        val target = tasks.value!!.filter { it.id == id }
        //날짜 변경
        //target[0].timeStamp =
        val dateFormat = SimpleDateFormat("y년 M월 d일")
        val date : Date = dateFormat.parse(target[0].dateStamp)
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DAY_OF_MONTH, 1)
        target[0].dateStamp = SimpleDateFormat("y년 M월 d일").format(cal.time).toString()
        update(target[0])
    }

    fun updateTask(id : Int, flag : Boolean){
        val target = tasks.value!!.filter{it.id == id}
        target[0].isComplete = flag
        update(target[0])
    }

    fun updateTitle(id : Int, title : String){
        val target = tasks.value!!.filter{it.id == id}
        target[0].title = title
        update(target[0])
    }


    fun insert(task: Task){
        viewModelScope.launch {
            val newRowId : Long = repository.insert(task)
            if(newRowId > -1){
                statusMessage.value = Event("할일 생성 완료!")
            }
            else{
                statusMessage.value = Event("할일 생성에 실패했어요")
            }
        }
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
                statusMessage.value = Event("내일로 미뤄짐")
            }
            else{
                statusMessage.value = Event("수정 실패!")
            }
        }
    }

    fun isExist(date : Date){

    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}