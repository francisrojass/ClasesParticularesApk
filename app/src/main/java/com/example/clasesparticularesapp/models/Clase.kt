package com.example.clasesparticularesapp.models

data class Clase(
    var id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val horario: String = "",
    val limiteAlumnos: Int = 0,
    val profesorId: String = "",
    val asignatura: String = "",
    val precioHora: Float = 0.0f,
    val fecha: String = ""
) {
    // Necesario un constructor sin argumentos para Firebase Firestore
    constructor() : this("", "", "", "", 0, "", "", 0.0f, "")
}
