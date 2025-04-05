package com.example.clasesparticularesapp.models

data class Clase(
    var id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val horario: String = "",
    val limiteAlumnos: Int = 0,
    val profesorId: String = ""
) {
    // Necesario un constructor sin argumentos para Firebase Firestore
    constructor() : this("", "", "", "", 0, "")
}
