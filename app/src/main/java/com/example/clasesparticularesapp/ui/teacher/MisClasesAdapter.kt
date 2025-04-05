package com.example.clasesparticularesapp.ui.teacher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase

class MisClasesAdapter(private val clases: List<Clase>) : RecyclerView.Adapter<MisClasesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreClase: TextView = itemView.findViewById(R.id.tvNombreClase) // Asegúrate de que este ID exista en item_clase.xml
        val horarioClase: TextView = itemView.findViewById(R.id.tvHorarioClase) // Asegúrate de que este ID exista en item_clase.xml
        // val alumnosInscritos: TextView = itemView.findViewById(R.id.tvAlumnosInscritos) // Si tienes este TextView
        val btnGuardar: Button = itemView.findViewById(R.id.btnGuardarClaseItem) // Asegúrate de que este ID exista en item_clase.xml (si tienes botones en la tarjeta)
        val btnDescartar: Button = itemView.findViewById(R.id.btnDescartarClaseItem) // Asegúrate de que este ID exista en item_clase.xml (si tienes botones en la tarjeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clase_lista, parent, false) // Infla el layout de cada item
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clase = clases[position]
        holder.nombreClase.text = clase.nombre
        holder.horarioClase.text = "Horario: ${clase.horario}"
        // holder.alumnosInscritos.text = "Inscritos: ${clase.alumnosInscritos.size}/${clase.limiteAlumnos}"

        // Acciones de editar y eliminar (Ejemplo de cómo podrías manejar los clics)
        holder.btnGuardar.setOnClickListener {
            println("Guardar clase: ${clase.nombre}")
            // Aquí podrías implementar la lógica para editar la clase
        }

        holder.btnDescartar.setOnClickListener {
            println("Descartar clase: ${clase.nombre}")
            // Aquí podrías implementar la lógica para eliminar la clase
        }
    }

    override fun getItemCount() = clases.size
}

