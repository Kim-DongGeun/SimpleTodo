package com.example.simpletodo.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id : Int,
    @ColumnInfo(name = "task_title")
    var title: String,
    @ColumnInfo(name = "task_datestamp")
    var dateStamp: String,
    @ColumnInfo(name = "task_timestamp")
    var timeStamp: String,
    @ColumnInfo(name = "task_iscomplete")
    var isComplete: Boolean
)