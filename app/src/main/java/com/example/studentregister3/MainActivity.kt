package com.example.studentregister3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentregister3.databinding.ActivityMainBinding
import com.example.studentregister3.db.Student
import com.example.studentregister3.db.StudentDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentRecyclerViewAdapter
    private lateinit var selectedStudent: Student
    private var isUpdateOrDelete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = StudentDatabase.getInstance(application).studentDAO()
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]

        binding.btnSave.setOnClickListener {
            if (isUpdateOrDelete) {
                updateStudentData()
                isUpdateOrDelete = false
            }
            else {
                saveStudentData()
            }
            clearInput()
            setTextForButton()
        }

        binding.btnClear.setOnClickListener {
            if (isUpdateOrDelete) {
                deleteStudentData()
                isUpdateOrDelete = false
            }
            clearInput()
            setTextForButton()
        }

        initRecyclerView()

    }

    private fun saveStudentData() {
        viewModel.insertStudent(
            Student(
                0,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )
    }

    private fun updateStudentData() {
        viewModel.updateStudent(
            Student(
                selectedStudent.id,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )
    }

    private fun deleteStudentData() {
        viewModel.deleteStudent(
            Student(
                selectedStudent.id,
                binding.etName.text.toString(),
                binding.etEmail.text.toString()
            )
        )
    }

    private fun clearInput() {
        binding.apply {
            etName.setText("")
            etEmail.setText("")
        }
    }
    private fun initRecyclerView() {
        binding.apply {
            rvStudent.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = StudentRecyclerViewAdapter {
                listItemClicked(it)
            }
            rvStudent.adapter = adapter

            displayStudentList()

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayStudentList() {
        viewModel.students.observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(student: Student) {
        selectedStudent = student
        isUpdateOrDelete = true
        binding.apply {
            etName.setText(selectedStudent.name)
            etEmail.setText(selectedStudent.email)
        }
        setTextForButton()
    }

    private fun setTextForButton() {
        binding.apply {
            if (isUpdateOrDelete) {
                btnSave.text = getString(R.string.update_button_text)
                btnClear.text = getString(R.string.delete_button_text)
            }
            else {
                btnSave.text = getString(R.string.save_button_text)
                btnClear.text = getString(R.string.clear_button_text)
            }
        }
    }
}