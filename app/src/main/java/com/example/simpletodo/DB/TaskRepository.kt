package com.example.simpletodo.DB

class TaskRepository(private val dao: TaskDAO) {

    val UnCompletedTasks = dao.getUnCompletedTask()
    val CompletedTasks = dao.getCompletedTask()

    suspend fun insert(task: Task) : Long{
        return dao.insertTask(task)
    }

    suspend fun update(task: Task) : Int{
        return dao.updateTask(task)
    }

    suspend fun delete(task: Task) : Int{
        return  dao.deleteTask(task)
    }

    suspend fun deleteAll() : Int{
        return dao.deleteAll()
    }
}