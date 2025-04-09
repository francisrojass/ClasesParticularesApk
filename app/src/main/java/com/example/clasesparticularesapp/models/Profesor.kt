package com.example.clasesparticularesapp.models

data class Profesor(
    val id: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val asignaturas: List<String> = emptyList(),
    val valoracion: Float = 0f,
    val fotoUrl: String = ""
)