
package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class Menu : AppCompatActivity() {


    fun setupMenu(view: View) {
        view.setOnClickListener {
            showPopupMenu(view)
        }
    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.IniciarSesion -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.Principal -> {
                    startActivity(Intent(this, Inicio::class.java))
                    true
                }
                R.id.Inscripciones -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.Programacion -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.Ranking -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.Partidos -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.Torneos -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.Galeria -> {
                    startActivity(Intent(this, Galeria::class.java))
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
