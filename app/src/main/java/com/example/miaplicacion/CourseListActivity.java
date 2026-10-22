package com.example.miaplicacion;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity {

    private RecyclerView rvCourses;
    private ArrayList<CourseModel> courseList;
    private CourseAdapter courseAdapter;
    private ImageButton btnBackCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        rvCourses = findViewById(R.id.rvCourses);
        btnBackCourse = findViewById(R.id.btnBackCourse);

        btnBackCourse.setOnClickListener(v -> finish());

        // Inicializar conjunto de datos (Dataset) según las diapositivas
        courseList = new ArrayList<>();
        courseList.add(new CourseModel("DSA in Java", 4.8f, R.drawable.ic_course_icon, "140 alumnos matriculados"));
        courseList.add(new CourseModel("Java Course", 4.5f, R.drawable.ic_course_icon, "95 alumnos matriculados"));
        courseList.add(new CourseModel("C++ Course", 4.7f, R.drawable.ic_course_icon, "80 alumnos matriculados"));
        courseList.add(new CourseModel("Desarrollo Android Nativo", 5.0f, R.drawable.ic_course_icon, "210 alumnos matriculados"));
        courseList.add(new CourseModel("Arquitectura de Software Móvil", 4.9f, R.drawable.ic_course_icon, "75 alumnos matriculados"));
        courseList.add(new CourseModel("Bases de Datos para Móviles", 4.6f, R.drawable.ic_course_icon, "110 alumnos matriculados"));
        courseList.add(new CourseModel("Python & Inteligencia Artificial", 4.9f, R.drawable.ic_course_icon, "320 alumnos matriculados"));
        courseList.add(new CourseModel("Diseño UX/UI para Aplicaciones", 4.8f, R.drawable.ic_course_icon, "130 alumnos matriculados"));

        // Configurar LayoutManager (Lista Vertical)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        // Asociar Adaptador
        courseAdapter = new CourseAdapter(this, courseList);
        rvCourses.setAdapter(courseAdapter);
    }
}
