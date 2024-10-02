package com.example.parcialhh100221.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parcialhh100221.R
import com.example.parcialhh100221.ui.theme.ParcialHH100221Theme
import com.example.parcialhh100221.ui.theme.Red80

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
fun cartaOpcion(titulo: String, ruta: String, foto: Int, navController: NavController, modifier: Modifier = Modifier) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Red80
        ),
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        Surface(
            color = Red80,
            modifier = modifier.padding(vertical = 4.dp, horizontal = 4.dp)
        ) {

            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(15.dp)
            ) {

                Image(
                    painter = painterResource(id = foto),
                    contentDescription = "Descripción de la imagen", // Importante para accesibilidad
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 15.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = titulo,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )

                    ElevatedButton(
                        onClick = { navController.navigate(ruta) },
                        modifier = Modifier.padding(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Entrar" , color = Color.DarkGray)
                    }

                }

            }

        }
    }
}

@Composable
fun MainScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(750.dp)
            .padding(top = 40.dp)
            .clip(RoundedCornerShape(20.dp))
            .clipToBounds()
    ) {

        val opciones = listOf(
            Triple("Ubicación", "location", R.drawable.cuadro_mapa),
            Triple("Galería", "media", R.drawable.home_explora),
            Triple("Notificaciones", "notifications", R.drawable.notify_plus),
            Triple("Cámara", "camera", R.drawable.camera_grax)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(opciones) { item ->
                cartaOpcion(item.first, item.second, item.third, navController)
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun MainScreenPrev() {

    val navController = rememberNavController()

    ParcialHH100221Theme {
        MainScreen(navController = navController)
    }
}