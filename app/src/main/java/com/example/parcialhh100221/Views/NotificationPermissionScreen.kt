package com.example.parcialhh100221.Views

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.core.app.NotificationCompat
import com.example.parcialhh100221.R
import com.example.parcialhh100221.ui.theme.Red80

@Composable
fun NotificationPermissionScreen() {

    var hasNotificationPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Este bloque se ejecutará cuando necesitemos solicitar el permiso
    if (showPermissionRequest && !hasNotificationPermission) {
        RequestPermissionWithExplanation(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { isGranted ->
                hasNotificationPermission = isGranted
                showPermissionRequest = false
                if (isGranted) {
                    // Proceder con la selección de imagen
                    Toast.makeText(context, "Permiso concedido. Notificaciones habilitadas.", Toast.LENGTH_SHORT).show()
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
        if (hasNotificationPermission) {
            Button(
                onClick = {
                // Enviar notificación
                sendNotification(context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red80,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Enviar Notificación")
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
                Text(text = "Solicitar Permiso de Notificaciones")
            }
        }
    }
}

// Función para enviar una notificación
fun sendNotification(context: Context) {
    val channelId = "default_channel"
    val notificationId = 1

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Creacio de canal de notificación por si es necesario
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Canal Predeterminado",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    // Crear la notificación
    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Notificación de prueba")
        .setContentText("¡Hola soy Mario Hernandez y esta es mi noti!")
        .setSmallIcon(R.drawable.ic_notification) // Asegúrate de tener un icono en drawable
        .build()

    // Enviar la notificación
    notificationManager.notify(notificationId, notification)
}