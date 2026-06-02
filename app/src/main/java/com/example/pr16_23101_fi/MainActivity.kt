package com.example.pr16_23101_fi

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pr16_23101_fi.ui.theme.Pr1623101fiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pr1623101fiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PreferencesScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getSharedPreferences("app_preferences", MODE_PRIVATE)
    }
}

@Composable
fun PreferencesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        outputText = loadSavedText(context)
        inputText = outputText
    }

    Column(
        modifier = modifier
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { newText ->
                inputText = newText
                saveText(context, newText)
            },
            label = { Text("Введите текст для сохранения") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = outputText.ifEmpty { "Здесь появится сохранённый текст" },
                fontSize = 18.sp,
                color = if (outputText.isEmpty()) Color.Gray else Color.Black
            )
        }

        Button(
            onClick = {
                outputText = inputText
                Toast.makeText(context, "Текст сохранён", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Сохранить",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Button(
            onClick = {
                outputText = loadSavedText(context)
                inputText = outputText
                Toast.makeText(context, "Текст загружен", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Загрузить",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@SuppressLint("UseKtx", "ApplySharedPref")
private fun saveText(context: Context, text: String) {
    val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    prefs.edit().putString("saved_text", text).commit()
}

private fun loadSavedText(context: Context): String {
    val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return prefs.getString("saved_text", "") ?: ""
}