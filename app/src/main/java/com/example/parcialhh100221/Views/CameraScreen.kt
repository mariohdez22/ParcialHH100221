package com.example.parcialhh100221.Views

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.parcialhh100221.ui.theme.ParcialHH100221Theme
import com.example.parcialhh100221.ui.theme.Red80
import java.io.File

@Composable
fun CameraScreen() {

    var hasMediaPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val activity = context.findActivity()

    // Crear un URI temporal para almacenar la imagen
    val imageFile = remember {
        File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg").apply { createNewFile() }
    }
    val imageUriTemp = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )

    // Launcher para abrir la cámara y obtener la imagen
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = imageUriTemp
            Toast.makeText(context, "Foto tomada exitosamente.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No se pudo tomar la foto.", Toast.LENGTH_SHORT).show()
        }
    }

    // Este bloque se ejecutará cuando necesitemos solicitar el permiso
    if (showPermissionRequest && !hasMediaPermission) {
        RequestPermissionWithExplanation(
            permission = Manifest.permission.CAMERA,
            onPermissionResult = { isGranted ->
                hasMediaPermission = isGranted
                showPermissionRequest = false
                if (isGranted) {
                    Toast.makeText(context, "Permiso concedido. Camera habilitada.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Permiso denegado. No puedes subir imágenes.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasMediaPermission) {

            Button(
                onClick = {
                    // Abrir la cámara
                    takePictureLauncher.launch(imageUriTemp)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red80,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Tomar Foto")
            }
            imageUri?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
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
                Text(text = "Solicitar Permiso de Cámara")
            }
        }

    }
}
