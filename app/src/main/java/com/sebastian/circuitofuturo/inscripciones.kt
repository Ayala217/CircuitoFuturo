package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class inscripciones : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripciones)


        val btnInscribirme = findViewById<Button>(R.id.btnInscribirse)
        val btnRecomendaciones = findViewById<Button>(R.id.btnRecomendar)

        btnInscribirme.setOnClickListener {
            val intent = Intent(this@inscripciones, Inscribirse::class.java)
            startActivity(intent)
        }

        btnRecomendaciones.setOnClickListener {
            val intent = Intent(this@inscripciones, Recomendaciones::class.java)
            startActivity(intent)
        }


        }
    }
