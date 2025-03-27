package com.example.clasesparticularesapp.models

data class Clase(
    var id: String = "",
    val nombre: String = "",
    val horario: String = "", // Antes "fecha"
    val limiteAlumnos: Int = 0,
    val alumnosInscritos: List<String> = emptyList(),
    val valoracion: Float = 0f,
    val profesorId: String = ""
)
