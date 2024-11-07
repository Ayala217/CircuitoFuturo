
package com.sebastian.circuitofuturo

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge

class Inicio : Menu() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_inicio)

        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)
    }
}
