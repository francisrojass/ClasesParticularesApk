package com.example.clasesparticularesapp.ui.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase
import com.google.firebase.firestore.FirebaseFirestore

class ClassAdapter(private var clases: List<Clase>) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profesor, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val clase = clases[position]
        holder.asignatura.text = clase.asignatura
        holder.horario.text = clase.horario
        holder.precio.text = "Precio: ${clase.precioHora} €/h"
        holder.limiteAlumnos.text = "Límite alumnos: ${clase.limiteAlumnos}"
        holder.valoracion.text = "Valoración: pendiente" // Opcional

        // Obtener el nombre del profesor usando el profesorId
        clase.profesorId?.let { profesorId ->
            db.collection("profesores")
                .document(profesorId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombreProfesor = document.getString("nombre") // Asegúrate de que el campo se llame "nombre"
                        holder.nombre.text = nombreProfesor ?: "Nombre no encontrado"
                    } else {
                        holder.nombre.text = "Profesor no encontrado"
                    }
                }
                .addOnFailureListener { e ->
                    holder.nombre.text = "Error al cargar el nombre"
                    // Log the error
                }
        } ?: run {
            holder.nombre.text = "ID de profesor no disponible"
        }
    }

    override fun getItemCount(): Int = clases.size

    fun actualizarLista(nuevaLista: List<Clase>) {
        clases = nuevaLista
        notifyDataSetChanged()
    }

    class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.profesor_nombre)
        val asignatura: TextView = view.findViewById(R.id.profesor_asignatura)
        val horario: TextView = view.findViewById(R.id.profesor_horario)
        val precio: TextView = view.findViewById(R.id.profesor_precio)
        val limiteAlumnos: TextView = view.findViewById(R.id.profesor_limite_alumnos)
        val valoracion: TextView = view.findViewById(R.id.profesor_valoracion)
    }
}
