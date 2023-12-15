package com.example.studentregister3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentregister3.db.Student
import com.example.studentregister3.db.StudentDAO
import kotlinx.coroutines.launch

class StudentViewModel(private val dao: StudentDAO): ViewModel() {
    val students = dao.getAllStudent()

    fun insertStudent(student: Student) = viewModelScope.launch {
        dao.insertStudent(student)
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        dao.updateStudent(student)
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        dao.deleteStudent(student)
    }
}