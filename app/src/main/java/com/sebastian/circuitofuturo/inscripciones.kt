package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class inscripciones : Menu() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripciones)

        val btnInscribirme = findViewById<Button>(R.id.btnInscribirse)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)

        btnInscribirme.setOnClickListener {
            val intent = Intent(this@inscripciones, inscripcion::class.java)
            startActivity(intent)
        }


        }
    }
