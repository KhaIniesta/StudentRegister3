package com.example.studentregister3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregister3.db.Student
import com.example.studentregister3.db.StudentDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var clearButton: Button

    private lateinit var viewModel: StudentViewModel
    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var adapter: StudentRecyclerViewAdapter
    private lateinit var selectedStudent: Student
    private var isUpdateOrDelete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.etName)
        emailEditText = findViewById(R.id.etEmail)
        saveButton = findViewById(R.id.btnSave)
        clearButton = findViewById(R.id.btnClear)
        studentRecyclerView = findViewById(R.id.rvStudent)

        val dao = StudentDatabase.getInstance(application).studentDAO()
        val factory = StudentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]

        saveButton.setOnClickListener {
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

        clearButton.setOnClickListener {
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
                nameEditText.text.toString(),
                emailEditText.text.toString()
            )
        )
    }

    private fun updateStudentData() {
        viewModel.updateStudent(
            Student(
                selectedStudent.id,
                nameEditText.text.toString(),
                emailEditText.text.toString()
            )
        )
    }

    private fun deleteStudentData() {
        viewModel.deleteStudent(
            Student(
                selectedStudent.id,
                nameEditText.text.toString(),
                emailEditText.text.toString()
            )
        )
    }

    private fun clearInput() {
        nameEditText.setText("")
        emailEditText.setText("")
    }
    private fun initRecyclerView() {
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentRecyclerViewAdapter {
            listItemClicked(it)
        }
        studentRecyclerView.adapter = adapter

        displayStudentList()
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
        nameEditText.setText(selectedStudent.name)
        emailEditText.setText(selectedStudent.email)
        setTextForButton()
    }

    private fun setTextForButton() {
        if (isUpdateOrDelete) {
            saveButton.text = getString(R.string.update_button_text)
            clearButton.text = getString(R.string.delete_button_text)
        }
        else {
            saveButton.text = getString(R.string.save_button_text)
            clearButton.text = getString(R.string.clear_button_text)
        }
    }
}