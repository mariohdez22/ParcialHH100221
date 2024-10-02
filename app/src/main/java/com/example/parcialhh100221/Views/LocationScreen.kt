package com.example.parcialhh100221.Views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import com.example.parcialhh100221.ui.theme.Red80
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationScreen() {

    var hasLocationPermission by remember { mutableStateOf(false) }
    var showPermissionRequest by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf<LatLng?>(null) }

    val context = LocalContext.current

    // Manejo del permiso de ubicación
    if (showPermissionRequest && !hasLocationPermission) {
        RequestPermissionWithExplanation(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionResult = { isGranted ->
                hasLocationPermission = isGranted
                showPermissionRequest = false
                if (isGranted) {
                    // Obtener la ubicación actual
                    getCurrentLocation(context) { latLng ->
                        location = latLng
                    }
                } else {
                    Toast.makeText(context, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // Interfaz de usuario
    Column(modifier = Modifier.fillMaxSize()) {
        if (hasLocationPermission) {
            val cameraPositionState = rememberCameraPositionState()
            val markerState = remember { MarkerState(position = LatLng(0.0, 0.0)) }

            // Actualizar posición del marcador cuando la ubicación cambie
            LaunchedEffect(location) {
                location?.let { loc ->
                    markerState.position = loc
                    // Mover la cámara a la nueva ubicación
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(loc, 15f)
                    )
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = markerState
                )
            }

            // Si la ubicación es nula, obtenerla
            if (location == null) {
                getCurrentLocation(context) { latLng ->
                    location = latLng
                }
            }

        } else {
            // Mostrar botón para solicitar permiso
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                    showPermissionRequest = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red80,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Solicitar Permiso de Ubicación")
                }
            }
        }
    }
}

// Función para obtener la ubicación actual
@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationReceived: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                onLocationReceived(LatLng(location.latitude, location.longitude))
            } else {
                // Si lastLocation es nulo, solicita actualizaciones de ubicación
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    5000L // Intervalo en milisegundos
                )
                    .setWaitForAccurateLocation(true)
                    .setMinUpdateIntervalMillis(2000L)
                    .setMaxUpdates(1)
                    .build()

                val cancellationTokenSource = CancellationTokenSource()

                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )
                    .addOnSuccessListener { loc: Location? ->
                        if (loc != null) {
                            onLocationReceived(LatLng(loc.latitude, loc.longitude))
                        } else {
                            Toast.makeText(context, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al obtener la ubicación.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show()
        }
}
