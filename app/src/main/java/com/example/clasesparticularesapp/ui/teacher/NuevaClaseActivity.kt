package com.example.clasesparticularesapp.ui.teacher

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Clase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class NuevaClaseActivity : AppCompatActivity() {

    private lateinit var etNombreClase: EditText
    private lateinit var tvAsignaturaClase: TextView
    private lateinit var etPrecioHora: EditText
    private lateinit var etFecha: EditText
    private lateinit var etHorario: EditText
    private lateinit var etLimiteAlumnos: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnGuardarClase: Button
    private lateinit var btnDescartarClase: Button
    private lateinit var backButton: ImageButton

    private val asignaturasDisponibles = arrayOf("Matemáticas", "Física", "Inglés", "Química", "Historia")
    private var asignaturaSeleccionada: String = ""

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_clase)

        etNombreClase = findViewById(R.id.etNombreClase)
        tvAsignaturaClase = findViewById(R.id.tvAsignaturaClase)
        etPrecioHora = findViewById(R.id.etPrecioHora)
        etFecha = findViewById(R.id.etFecha)
        etHorario = findViewById(R.id.etHorario)
        etLimiteAlumnos = findViewById(R.id.etLimiteAlumnos)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnGuardarClase = findViewById(R.id.btnGuardarClase)
        btnDescartarClase = findViewById(R.id.btnDescartarClase)
        backButton = findViewById(R.id.back_button)

        etFecha.inputType = InputType.TYPE_NULL
        etFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val fechaFormateada = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                etFecha.setText(fechaFormateada)
            }, year, month, day)

            datePicker.show()
        }

        etHorario.inputType = InputType.TYPE_NULL
        etHorario.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val horaFormateada = String.format("%02d:%02d", selectedHour, selectedMinute)
                etHorario.setText(horaFormateada)
            }, hour, minute, true) // true para formato 24h

            timePicker.show()
        }

        tvAsignaturaClase.setOnClickListener {
            var seleccionTemporal = asignaturaSeleccionada // valor preseleccionado

            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Selecciona una asignatura")
            builder.setSingleChoiceItems(asignaturasDisponibles, asignaturasDisponibles.indexOf(asignaturaSeleccionada)) { _, which ->
                seleccionTemporal = asignaturasDisponibles[which]
            }

            builder.setPositiveButton("Aceptar") { dialog, _ ->
                asignaturaSeleccionada = seleccionTemporal
                tvAsignaturaClase.text = asignaturaSeleccionada
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }


        btnGuardarClase.setOnClickListener {
            guardarClase()
        }

        btnDescartarClase.setOnClickListener {
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun guardarClase() {
        val nombre = etNombreClase.text.toString().trim()
        val asignatura = tvAsignaturaClase.text.toString().trim()
        val precioHoraStr = etPrecioHora.text.toString().trim()
        val fecha = etFecha.text.toString().trim()
        val horario = etHorario.text.toString().trim()
        val limiteAlumnosStr = etLimiteAlumnos.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (nombre.isEmpty() || asignatura.isEmpty() || precioHoraStr.isEmpty() || fecha.isEmpty() || horario.isEmpty() || limiteAlumnosStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val precioHora = precioHoraStr.toFloatOrNull()
        if (precioHora == null) {
            Toast.makeText(this, "El precio por hora debe ser un número", Toast.LENGTH_SHORT).show()
            return
        }

        val limiteAlumnos = limiteAlumnosStr.toIntOrNull()
        if (limiteAlumnos == null) {
            Toast.makeText(this, "El límite de alumnos debe ser un número entero", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaClase = Clase(
            id = "",
            nombre = nombre,
            descripcion = descripcion,
            horario = horario,
            limiteAlumnos = limiteAlumnos,
            profesorId = userId,
            asignatura = asignatura,
            precioHora = precioHora,
            fecha = fecha
        )

        db.collection("clases").add(nuevaClase)
            .addOnSuccessListener {
                Toast.makeText(this, "Clase guardada correctamente", Toast.LENGTH_SHORT).show()
                // Iniciar la actividad de MisClasesActivity
                val intent = Intent(this, MisClasesActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad actual (NuevaClaseActivity)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar la clase", Toast.LENGTH_SHORT).show()
            }
    }
}
