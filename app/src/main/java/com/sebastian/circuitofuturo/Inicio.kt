package com.sebastian.circuitofuturo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Inicio : Menu() {

    private lateinit var ivRotatingImage: ImageView
    private val images = arrayOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3
    )
    private var currentImageIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    private val imageSwitcher = object : Runnable {
        override fun run() {
            // Cambia la imagen
            ivRotatingImage.setImageResource(images[currentImageIndex])
            currentImageIndex = (currentImageIndex + 1) % images.size
            // Ejecuta el Runnable nuevamente después de 3 segundos (3000 ms)
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // Configurar el botón del menú
        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu) // Este método viene de la clase Menu

        ivRotatingImage = findViewById(R.id.ivRotatingImage)

        // Inicia el cambio de imágenes
        handler.post(imageSwitcher)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detiene el cambio de imágenes cuando la actividad es destruida
        handler.removeCallbacks(imageSwitcher)
    }
}
