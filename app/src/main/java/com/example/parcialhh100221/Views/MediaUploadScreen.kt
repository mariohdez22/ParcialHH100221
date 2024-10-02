package com.example.parcialhh100221.Views

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.parcialhh100221.ui.theme.Red80

@Composable
fun MediaUploadScreen() {

    var hasMediaPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // Launcher para seleccionar una imagen de la galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        } else {
            Toast.makeText(context, "No se seleccionó ninguna imagen.", Toast.LENGTH_SHORT).show()
        }
    }

    // Este bloque se ejecutará cuando necesitemos solicitar el permiso
    if (showPermissionRequest && !hasMediaPermission) {
        RequestPermissionWithExplanation(
            permission = Manifest.permission.READ_MEDIA_IMAGES,
            onPermissionResult = { isGranted ->
                hasMediaPermission = isGranted
                showPermissionRequest = false
                if (isGranted) {
                    // Proceder con la selección de imagen
                    Toast.makeText(context, "Permiso concedido. Puedes subir una imagen.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Permiso denegado. No puedes subir imágenes.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasMediaPermission) {
            Button(
                onClick = {
                // Abrir galería para seleccionar imagen
                pickImageLauncher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red80,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Seleccionar Imagen")
            }
            imageUri?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
        } else {
            Button(
                onClick = {
                showPermissionRequest = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red80,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Solicitar Permiso de Medios")
            }
        }

    }
}