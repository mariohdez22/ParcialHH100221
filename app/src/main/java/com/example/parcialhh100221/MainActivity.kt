package com.example.parcialhh100221

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parcialhh100221.ui.theme.ParcialHH100221Theme
import com.example.parcialhh100221.ui.theme.Pink80
import com.example.parcialhh100221.ui.theme.Purple40
import com.example.parcialhh100221.ui.theme.Purple80
import com.example.parcialhh100221.ui.theme.Red80

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParcialHH100221Theme {
                AppNavigation()
            }
        }
    }
}

//--------------------------------------------------------------------------------------------------

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("location") {
            LocationScreen()
        }
        composable("media") {
            MediaUploadScreen()
        }
        composable("notifications") {
            NotificationPermissionScreen()
        }
        composable("camera") {
            CameraScreen()
        }
    }
}

//--------------------------------------------------------------------------------------------------

@Composable
fun BotonPrincipal(ruta: String, texto: String, navController: NavController) {

    Button(
        onClick = { navController.navigate(ruta) },
        modifier = Modifier.padding(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Red80,
            contentColor = Color.White
        )
    ) {
        Text(text = texto)
    }
}

@Composable
fun MainScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BotonPrincipal("location", "Ir a Mapa", navController)
        BotonPrincipal("media", "Subir Imagen/Video", navController)
        BotonPrincipal("notifications", "Configurar Notificaciones", navController)
        BotonPrincipal("camera", "Tomar Foto", navController)
    }
}

//--------------------------------------------------------------------------------------------------

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

//--------------------------------------------------------------------------------------------------

@Composable
fun LocationScreen() {
    var hasLocationPermission by remember { mutableStateOf(false) }

    if (!hasLocationPermission) {
        RequestPermissionWithExplanation(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionResult = { isGranted ->
                hasLocationPermission = isGranted
            }
        )
    } else {
        // Mostrar el mapa o funcionalidad que requiere ubicación
        Text(text = "Permiso de ubicación concedido")
    }
}

//--------------------------------------------------------------------------------------------------

@Composable
fun MediaUploadScreen() {

    var hasMediaPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
        Button(
            onClick = {
                if (hasMediaPermission) {
                    // Proceder con la selección de imagen
                    Toast.makeText(context, "Selecciona una imagen para subir.", Toast.LENGTH_SHORT).show()
                } else {
                    // Cambiar el estado para mostrar la solicitud de permiso
                    showPermissionRequest = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Red80,
                contentColor = Color.White
            )
        ) {
            Text(text = "Subir Imagen")
        }
    }
}

//--------------------------------------------------------------------------------------------------

@Composable
fun NotificationPermissionScreen() {

    var hasMediaPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Este bloque se ejecutará cuando necesitemos solicitar el permiso
    if (showPermissionRequest && !hasMediaPermission) {
        RequestPermissionWithExplanation(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { isGranted ->
                hasMediaPermission = isGranted
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
        Button(
            onClick = {
                if (hasMediaPermission) {
                    // Proceder a mandar notificacion
                    Toast.makeText(context, "Mandar notificacion", Toast.LENGTH_SHORT).show()
                } else {
                    // Cambiar el estado para mostrar la solicitud de permiso
                    showPermissionRequest = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Red80,
                contentColor = Color.White
            )
        ) {
            Text(text = "Mandar Notificacion")
        }
    }
}

//--------------------------------------------------------------------------------------------------

@Composable
fun CameraScreen() {

    var hasMediaPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Este bloque se ejecutará cuando necesitemos solicitar el permiso
    if (showPermissionRequest && !hasMediaPermission) {
        RequestPermissionWithExplanation(
            permission = Manifest.permission.CAMERA,
            onPermissionResult = { isGranted ->
                hasMediaPermission = isGranted
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
        Button(
            onClick = {
                if (hasMediaPermission) {
                    // Proceder a abrir camara
                    Toast.makeText(context, "Abriendo camara", Toast.LENGTH_SHORT).show()
                } else {
                    // Cambiar el estado para mostrar la solicitud de permiso
                    showPermissionRequest = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Red80,
                contentColor = Color.White
            )
        ) {
            Text(text = "Tomar Foto")
        }
    }
}

//--------------------------------------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParcialHH100221Theme {
        AppNavigation()
    }
}