package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editLogIn: Button
    private lateinit var editSignUp: TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        editEmail = findViewById(R.id.etEmail)
        editPassword = findViewById(R.id.etPassword)
        editLogIn = findViewById(R.id.btnLogin)
        editSignUp = findViewById(R.id.tvRegister)

        editSignUp.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        editLogIn.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Verificar si el correo es del administrador
                    if (email == "jsayala217@gmail.com") {
                        Toast.makeText(
                            this@Login,
                            "Bienvenido, Admin!",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Redirigir a una pantalla de administrador si es necesario
                        val adminIntent = Intent(this@Login, AdminActivity::class.java)
                        finish()
                        startActivity(adminIntent)
                    } else {
                        Toast.makeText(
                            this@Login,
                            "Inicio de sesión exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
                        val userIntent = Intent(this@Login, Inicio::class.java)
                        finish()
                        startActivity(userIntent)
                    }
                } else {
                    Toast.makeText(
                        this@Login,
                        "Error: el usuario no existe",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
