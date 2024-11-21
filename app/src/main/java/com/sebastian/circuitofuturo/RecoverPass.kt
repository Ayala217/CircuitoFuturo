package com.sebastian.circuitofuturo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class RecoverPass : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_pass)
        val txtemail: TextView = findViewById(R.id.txtrecovery)
        val btnRecuperar : Button = findViewById(R.id.recuperar)
        btnRecuperar.setOnClickListener(){
            enviarRecuperacion(txtemail.text.toString())
        }
        mAuth = FirebaseAuth.getInstance()
    }
    private fun enviarRecuperacion(email: String){
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(
                        this@RecoverPass,
                        "Se ha enviado el email.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }else{
                    Toast.makeText(
                        this@RecoverPass,
                        "Error al procesar su recuperacion verifique sus datos.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}