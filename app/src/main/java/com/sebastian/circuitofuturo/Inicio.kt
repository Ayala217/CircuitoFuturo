package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Inicio : AppCompatActivity() {

    private lateinit var ivRotatingImage: ImageView

    private val imageList = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3
    )
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())


    private val imageChangeRunnable = object : Runnable {
        override fun run() {
            ivRotatingImage.setImageResource(imageList[currentIndex])
            currentIndex = (currentIndex + 1) % imageList.size
            handler.postDelayed(this, 7000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_inicio)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        ivRotatingImage = findViewById(R.id.ivRotatingImage) // Asigna el ImageView para rotación de imágenes


        handler.post(imageChangeRunnable)

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
                    val intent = Intent(this@Inicio, inscripciones::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Programacion -> {
                    val intent = Intent(this@Inicio, Programacion::class.java)
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(imageChangeRunnable) // Detiene el handler para evitar fugas de memoria
    }
}







