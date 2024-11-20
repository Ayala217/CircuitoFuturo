
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

                R.id.Principal -> {
                    val intent = Intent(this@Menu, Inicio::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Inscripciones -> {
                    val intent = Intent(this@Menu, inscripciones::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Programacion -> {
                    val intent = Intent(this@Menu, programacion::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Ranking -> {
                    val intent = Intent(this@Menu, NuevosAdminActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Partidos -> {
                    val intent = Intent(this@Menu, Partidos::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Torneos -> {
                    val intent = Intent(this@Menu, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.Galeria -> {
                    val intent = Intent(this@Menu, Galeria::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Recomendaciones -> {
                    val intent = Intent(this@Menu, Recomendaciones::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Perfil -> {
                    val intent = Intent(this@Menu, Perfil::class.java)
                    startActivity(intent)
                    true
                }

                R.id.inscritos -> {
                    val intent = Intent(this@Menu, Inscritos::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Administradores -> {
                    val intent = Intent(this@Menu, AdminActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
