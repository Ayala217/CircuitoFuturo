package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

class AdminActivity : Menu() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)

        // Ajuste para Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       /* // Botón Torneos PDF
        findViewById<Button>(R.id.btnTorneosPdf).setOnClickListener {
            val intent = Intent(this, TorneosActivity::class.java)
            startActivity(intent)
        }

        // Botón Crear Partidos
        findViewById<Button>(R.id.btnCrearPartidos).setOnClickListener {
            val intent = Intent(this, NuevosPartidosActivity::class.java)
            startActivity(intent)
        }*/

        // Botón Crear Administradores
        findViewById<Button>(R.id.btnCrearAdmins).setOnClickListener {
            val intent = Intent(this, NuevosAdminActivity::class.java)
            startActivity(intent)
        }
    }
}
