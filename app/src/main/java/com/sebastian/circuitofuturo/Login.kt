package com.sebastian.circuitofuturo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    //instanciar
    private lateinit var editEmail:EditText
    private lateinit var editPassword:EditText
    private lateinit var editLogIn: Button
    private lateinit var editSignUp:Button

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()



        editEmail= findViewById(R.id.etCorreo)
        editPassword = findViewById(R.id.etPassword)
        editLogIn = findViewById(R.id.btLogin)
        editSignUp= findViewById(R.id.btRegister)

        editSignUp.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        editSignUp.setOnClickListener{
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
             login(email,password)
        }

    }
     private fun login(email: String, password: String){
         mAuth.signInWithEmailAndPassword(email,password)
             .addOnCompleteListener(this){ task ->
                 if (task.isSuccessful) {
                     val intent = Intent(this@Login, MainActivity::class.java)
                     finish()
                     startActivity(intent)

                 }else {
                     Toast.makeText(
                         this@Login,
                         "Error el usuario no existe",
                         Toast.LENGTH_SHORT,
                     ).show()
                 }

             }
     }

}


