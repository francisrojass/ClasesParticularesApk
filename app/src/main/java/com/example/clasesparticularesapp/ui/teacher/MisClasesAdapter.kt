package com.example.clasesparticularesapp.ui.teacher

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase
import com.google.firebase.firestore.FirebaseFirestore

class MisClasesAdapter(private val clases: MutableList<Clase>, private val activity: MisClasesActivity) : RecyclerView.Adapter<MisClasesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreClase: TextView = itemView.findViewById(R.id.tvNombreClase)
        val horarioClase: TextView = itemView.findViewById(R.id.tvHorarioClase)
        val eliminarClase: ImageView = itemView.findViewById(R.id.ivEliminarClase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clase_lista, parent, false) // Asegúrate de que este es el layout correcto CON el icono de la papelera
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clase = clases[position]
        holder.nombreClase.text = clase.nombre
        holder.horarioClase.text = "Horario: ${clase.horario}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetalleClaseActivity::class.java).apply {
                putExtra("nombre", clase.nombre)
                putExtra("asignatura", clase.asignatura)
                putExtra("descripcion", clase.descripcion)
                putExtra("fecha", clase.fecha)
                putExtra("horario", clase.horario)
                putExtra("limiteAlumnos", clase.limiteAlumnos)
                putExtra("precioHora", clase.precioHora)
            }
            holder.itemView.context.startActivity(intent)
        }
            holder.eliminarClase.setOnClickListener {
            val claseIdToDelete = clase.id
            if (claseIdToDelete.isNotEmpty()) {
                eliminarClaseDeFirebase(claseIdToDelete, position)
            } else {
                Toast.makeText(holder.itemView.context, "Error: ID de clase no válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarClaseDeFirebase(claseId: String, position: Int) {
        val db = FirebaseFirestore.getInstance()
        db.collection("clases")
            .document(claseId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(activity, "Clase eliminada correctamente", Toast.LENGTH_SHORT).show()
                clases.removeAt(position) // Eliminar de la lista local
                notifyItemRemoved(position) // Notificar al RecyclerView la eliminación
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error al eliminar la clase: ${e.message}", Toast.LENGTH_LONG).show()
                println("Error al eliminar la clase: $e")
            }
    }

    override fun getItemCount() = clases.size
}

