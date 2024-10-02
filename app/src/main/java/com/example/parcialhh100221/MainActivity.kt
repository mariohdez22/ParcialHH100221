package com.example.parcialhh100221

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.parcialhh100221.Views.AppNavigation
import com.example.parcialhh100221.ui.theme.ParcialHH100221Theme

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParcialHH100221Theme {
        AppNavigation()
    }
}

