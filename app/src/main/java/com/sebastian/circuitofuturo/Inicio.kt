package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_inicio)

        val tvMenu: TextView = findViewById(R.id.tvMenu)

        tvMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu) // Usa el archivo XML de menú creado

        // Maneja los clics en las opciones del menú
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.IniciarSesion -> {

                    val intent = Intent(this@Inicio, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Principal -> {

                    val intent = Intent(this@Inicio, Inicio::class.java)
                    startActivity(intent)
                    true

                }
                R.id.Inscripciones -> {
                    val intent = Intent(this@Inicio, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Programacion -> {
                    val intent = Intent(this@Inicio, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Ranking -> {
                    val intent = Intent(this@Inicio, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Partidos -> {
                    val intent = Intent(this@Inicio, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Torneos -> {
                    val intent = Intent(this@Inicio, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Galeria -> {
                    val intent = Intent(this@Inicio, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show() // Muestra el menú emergente
    }
}







