package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Partidos : Menu() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_partidos)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)

        val btnMarcador= findViewById<Button>(R.id.btMarcador)

        btnMarcador.setOnClickListener {
            val intent = Intent(this@Partidos, marcador::class.java)
            startActivity(intent)
        }
    }
}