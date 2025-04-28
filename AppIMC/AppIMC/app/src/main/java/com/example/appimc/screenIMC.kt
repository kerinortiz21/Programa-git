package com.example.appimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appimc.ui.theme.AppIMCTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        setContent {
            AppIMCTheme {
                // A surface container using the 'background' color from the theme that use the system
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    imcCalculatorApp()
                }
            }
        }
    }
}

// Function to calculate and display the IMC
@Composable
fun imcCalculatorApp() {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val colorMessage = if (message.contains("válidos")) Color.Red else MaterialTheme.colorScheme.onBackground

    // Function to calculate the IMC
    fun calculateIMC() {

        if (weight.contains(".") || height.contains(".")) {
            imc = ""
            message = " Ingrese valores válidos"
            return
        }

        val weightValue = weight.toFloatOrNull()
        val heightValue = height.toFloatOrNull()
        if (weightValue != null && heightValue != null && weightValue > 0 && heightValue > 0) {
            val imcResult = weightValue / (heightValue / 100).pow(2)
            imc = "Su IMC es de %.2f".format(imcResult)
            message = when (imcResult) {
                in 0.0..18.5 -> "Estás en bajo peso."
                in 18.5..24.9 -> "Estás en un peso normal."
                in 25.0..29.9 -> "Estás en sobrepeso."
                in 30.0..34.9 -> "Estás en obesidad grado 1."
                in 35.0..39.9 -> "Estás en obesidad grado 2."
                else -> "Estás en obesidad grado 3."
            }
        } else {
            message = "Ingrese valores válidos"
        }
    }

    // visual part of the app
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // show an image with a title
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            model = "https://martinquirosinternista.com/wp-content/uploads/2023/07/INDICE-DE-MASA-CORPORAL.jpg",
            contentDescription = "Índice de masa corporal"
        )

        Text(
            text = "Imagen: Dr. Martin Quiros",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Calculadora IMC",
            fontSize = 28.sp,
            color = Color(0xFF32CD32),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Introduce tu altura y tu peso para conocer tu índice de masa corporal",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // field to enter the weight
        OutlinedTextField(
            value = weight,
            onValueChange = {
                weight = it
                if (it.isNotBlank() && message.isNotEmpty()) message = ""
            },
            label = { Text("Peso (kg)") },
            singleLine = true,
            placeholder = { Text("Ejemplo: 70kg") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF32CD32),
                cursorColor = Color(0xFF32CD32)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // field to enter the height
        OutlinedTextField(
            value = height,
            onValueChange = {
                height = it
                if (it.isNotBlank() && message.isNotEmpty()) message = ""
            },
            label = { Text("Altura (cm)") },
            singleLine = true,
            placeholder = { Text("Ejemplo: 180 cm") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF32CD32),
                cursorColor = Color(0xFF32CD32)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // show a error message if the fields are empty
        Text(text = message, color = colorMessage)
        Spacer(modifier = Modifier.height(8.dp))
        // show the result of the calculation
        Text(text = imc, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(24.dp))

        // make a row with two buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { calculateIMC() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32CD32)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Calcular IMC")
            }

            if (weight.isNotBlank() || height.isNotBlank() || imc.isNotEmpty() || message.isNotEmpty()) {
                Button(
                    onClick = {
                        weight = ""
                        height = ""
                        imc = ""
                        message = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2DFDB)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Resetear", color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32CD32)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Información sobre el IMC", color = MaterialTheme.colorScheme.onBackground)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Información sobre el IMC") },
                text = {
                    Text("El IMC, o Índice de Masa Corporal, es una medida utilizada para evaluar si una persona tiene un peso saludable en relación con su estatura. Esta fórmula es empleada por muchos profesionales de la salud, incluidos médicos, enfermeras y nutricionistas, para determinar rápidamente si una persona necesita ganar o perder peso.")
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Lo entiendo")
                    }
                }
            )
        }
    }
}
