package com.example.studentregister3
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentregister3.db.StudentDAO

class StudentViewModelFactory(private val dao: StudentDAO): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java))
            return StudentViewModel(dao) as T
        throw IllegalArgumentException("Unknown view model class!")
    }

}