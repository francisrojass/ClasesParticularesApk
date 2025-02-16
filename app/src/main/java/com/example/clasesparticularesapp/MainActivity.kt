package com.example.clasesparticularesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.clasesparticularesapp.ui.student.StudentActivity
import com.example.clasesparticularesapp.ui.teacher.TeacherActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val studentCard = findViewById<CardView>(R.id.student_card)
        val teacherCard = findViewById<CardView>(R.id.teacher_card)
        val titleText = findViewById<TextView>(R.id.title_text)

        studentCard.setOnClickListener {
            startActivity(Intent(this, StudentActivity::class.java))
        }

        teacherCard.setOnClickListener {
            startActivity(Intent(this, TeacherActivity::class.java))
        }
    }
}






