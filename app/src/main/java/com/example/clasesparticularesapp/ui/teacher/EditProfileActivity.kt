package com.example.clasesparticularesapp.ui.teacher

//import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
//import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
//import com.example.clasesparticularesapp.MainActivity
import com.example.clasesparticularesapp.R

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_teacher)

        // Configurar el botón de retroceso
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //Boton de descartar cambios
        val discardButton: Button = findViewById(R.id.btnDescartarCambiosPerfil)
        discardButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

}