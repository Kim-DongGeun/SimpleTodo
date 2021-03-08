package com.example.simpletodo.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Task::class], version = 2)
abstract class TaskDatabase : RoomDatabase(){
    abstract val taskDAO : TaskDAO

    companion object{
        @Volatile
        private var INSTANCE : TaskDatabase? = null
        fun getInstance(context : Context) : TaskDatabase{
            synchronized(this){
                val MIGRATION_1_2 = object : Migration(1,2){
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE task_table ADD COLUMN task_datestamp TEXT")
                    }
                }

                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "task_table",
                    )
                        .addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}