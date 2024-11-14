package com.sebastian.circuitofuturo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Galeria : Menu() {

    private lateinit var image1: ImageView
    private lateinit var image2: ImageView
    private lateinit var image3: ImageView

    private var currentImageView: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeria)


        image1 = findViewById(R.id.image1)
        image2 = findViewById(R.id.image2)
        image3 = findViewById(R.id.image3)


        val tvMenu: TextView = findViewById(R.id.tvMenu)
        setupMenu(tvMenu)


        image1.setOnClickListener { checkPermissionAndOpenGallery(1) }
        image2.setOnClickListener { checkPermissionAndOpenGallery(2) }
        image3.setOnClickListener { checkPermissionAndOpenGallery(3) }
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            val targetImageView = when (currentImageView) {
                1 -> image1
                2 -> image2
                3 -> image3
                else -> null
            }
            targetImageView?.setImageURI(selectedImageUri)
        }
    }

    private fun checkPermissionAndOpenGallery(imageViewId: Int) {
        currentImageView = imageViewId

        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_PERMISSION)
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    companion object {
        const val REQUEST_CODE_PERMISSION = 101
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val selectedImages = mutableListOf<Uri>()

    private fun addImageToList(uri: Uri) {
        selectedImages.add(uri)
        // Luego de seleccionar todas las imágenes
        val intent = Intent(this, Inicio::class.java)
        intent.putParcelableArrayListExtra("IMAGES", ArrayList(selectedImages))
        startActivity(intent)
    }

}
