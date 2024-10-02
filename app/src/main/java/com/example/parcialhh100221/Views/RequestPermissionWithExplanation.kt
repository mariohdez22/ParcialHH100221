package com.example.parcialhh100221.Views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun PermissionExplanationDialog(
    permission: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Permiso Requerido") },
        text = {
            Text(
                text = when (permission) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> "La aplicación necesita acceder a tu ubicación para mostrar mapas personalizados."
                    Manifest.permission.READ_MEDIA_IMAGES -> "Necesitamos acceder a tus imágenes para que puedas subir fotos."
                    Manifest.permission.POST_NOTIFICATIONS -> "Permite notificaciones para mantenerte al día con las últimas actualizaciones."
                    Manifest.permission.CAMERA -> "Se requiere acceso a la cámara para tomar fotos."
                    else -> "Este permiso es necesario para el funcionamiento de la aplicación."
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Continuar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

//--------------------------------------------------------------------------------------------------

@Composable
fun RequestPermissionWithExplanation(
    permission: String,
    onPermissionResult: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity() ?: return

    var shouldShowExplanation by remember { mutableStateOf(false) }
    var permissionRequested by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionResult(true)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) || !permissionRequested -> {
                shouldShowExplanation = true
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    if (shouldShowExplanation) {
        PermissionExplanationDialog(
            permission = permission,
            onDismiss = {
                shouldShowExplanation = false
                onPermissionResult(false)
            },
            onConfirm = {
                shouldShowExplanation = false
                permissionRequested = true
                permissionLauncher.launch(permission)
            }
        )
    }
}

//--------------------------------------------------------------------------------------------------

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}