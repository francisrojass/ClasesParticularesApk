package com.example.clasesparticularesapp.ui.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clasesparticularesapp.R
import com.example.clasesparticularesapp.models.Profesor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var tvAsignaturas: TextView

    private val asignaturasDisponibles = arrayOf("Matemáticas", "Física", "Inglés", "Química", "Historia")
    private val asignaturasSeleccionadas = mutableListOf<String>()
    private val seleccionEstado = BooleanArray(asignaturasDisponibles.size)

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_teacher)

        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        tvAsignaturas = findViewById(R.id.tvAsignaturas)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        firestore = FirebaseFirestore.getInstance()
        if (userId != null) {
            cargarDatosPerfil() // Cargar los datos iniciales al crear la actividad
        }

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val discardButton: Button = findViewById(R.id.btnDescartarCambiosPerfil)
        discardButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val saveButton: Button = findViewById(R.id.btnGuardarCambiosPerfil)
        saveButton.setOnClickListener {
            // Mostrar el diálogo de confirmación al pulsar el botón Guardar
            mostrarDialogoConfirmacion()
        }

        tvAsignaturas.setOnClickListener {
            mostrarDialogoSeleccion()
        }
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar cambios")
            .setMessage("¿Estás seguro de que deseas guardar los cambios?")
            .setPositiveButton("Sí") { _, _ ->
                // Si el usuario dice que sí, guardar los cambios y navegar
                guardarCambios()
            }
            .setNegativeButton("No", null) // Si el usuario dice que no, no hacer nada
            .show()
    }

    private fun guardarCambios() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val nombre = etNombre.text.toString()
        val apellidos = etApellidos.text.toString()
        val asignaturas = asignaturasSeleccionadas.toList()

        val profesorActualizado = Profesor(
            id = userId,
            nombre = nombre,
            apellidos = apellidos,
            asignaturas = asignaturas,
            valoracion = 0f,
            fotoUrl = ""
        )

        db.collection("profesores").document(userId)
            .set(profesorActualizado)
            .addOnSuccessListener {
                Log.d("EditProfile", "Perfil actualizado correctamente (Listener)")
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                Log.d("EditProfile", "Toast mostrado")
                val intent = Intent(this, TeacherActivity::class.java)
                Log.d("EditProfile", "Intent creado")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                Log.d("EditProfile", "Flags del Intent configurados")
                startActivity(intent)
                Log.d("EditProfile", "startActivity llamado")
                finish()
                Log.d("EditProfile", "finish llamado")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarDatosPerfil() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("profesores").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nombre = document.getString("nombre") ?: ""
                    val apellidos = document.getString("apellidos") ?: ""
                    val asignaturasGuardadas = document.get("asignaturas") as? List<*> ?: emptyList<Any>()

                    Log.d("EditProfile", "Nombre cargado: $nombre")
                    Log.d("EditProfile", "Apellidos cargados: $apellidos")
                    Log.d("EditProfile", "Asignaturas cargadas: $asignaturasGuardadas")

                    etNombre.setText(nombre)
                    etApellidos.setText(apellidos)

                    asignaturasSeleccionadas.clear()
                    asignaturasGuardadas.forEach { asig ->
                        if (asig is String) {
                            asignaturasSeleccionadas.add(asig)
                        }
                    }

                    for (i in asignaturasDisponibles.indices) {
                        seleccionEstado[i] = asignaturasSeleccionadas.contains(asignaturasDisponibles[i])
                    }

                    if (asignaturasSeleccionadas.isNotEmpty()) {
                        tvAsignaturas.text = asignaturasSeleccionadas.joinToString(", ")
                    }
                } else {
                    Log.d("EditProfile", "El documento del profesor no existe")
                }
            }
            .addOnFailureListener { e ->
                Log.e("EditProfile", "Error al cargar datos: ${e.message}")
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDialogoSeleccion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona tus asignaturas")
        builder.setMultiChoiceItems(asignaturasDisponibles, seleccionEstado) { _, index, isChecked ->
            if (isChecked) {
                if (!asignaturasSeleccionadas.contains(asignaturasDisponibles[index])) {
                    asignaturasSeleccionadas.add(asignaturasDisponibles[index])
                }
            } else {
                asignaturasSeleccionadas.remove(asignaturasDisponibles[index])
            }
        }

        builder.setPositiveButton("Aceptar") { _, _ ->
            tvAsignaturas.text = asignaturasSeleccionadas.joinToString(", ")
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }
}