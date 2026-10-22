package com.vortex.ai

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vortex.ai.ui.theme.VortexAITheme
import java.util.Locale

private data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VortexAITheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (!isLoggedIn) {
        LoginScreen(onLogin = { isLoggedIn = true })
    } else {
        VortexAIScreen()
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    val glass = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF070B14),
            Color(0xFF101827),
            Color(0xFF0B1324)
        )
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(glass)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF111A2D).copy(alpha = 0.82f))
                    .border(1.dp, Color(0xFF6F7BFF).copy(alpha = 0.35f), RoundedCornerShape(28.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "VORTEX AI",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Intelligence for your next move",
                    fontSize = 14.sp,
                    color = Color(0xFFE0E5FF)
                )

                Spacer(modifier = Modifier.height(26.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Email address") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C6BFF),
                        unfocusedBorderColor = Color(0xFF3E4A68),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C6BFF),
                        unfocusedBorderColor = Color(0xFF3E4A68),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = onLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                ) {
                    Text(
                        text = "Continue",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                TextButton(onClick = { /* create account */ }) {
                    Text(
                        text = "Create account",
                        color = Color(0xFF9DA9FF)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Secure access • Encrypted session • AI ready",
                    color = Color(0xFFB9C1FF),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun VortexAIScreen() {
    var prompt by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Welcome back. VORTEX AI is online and ready.", false),
            ChatMessage("Build a premium work plan for today.", true)
        )
    }

    val responseGenerator: (String) -> String = { input ->
        when {
            input.contains("hello", ignoreCase = true) || input.contains("hi", ignoreCase = true) ->
                "Hello! I’m VORTEX AI. I can help you plan, think, create, and move faster."

            input.contains("plan", ignoreCase = true) || input.contains("day", ignoreCase = true) ->
                "Priority plan: 1) Set your top outcome, 2) Block 60 minutes for deep work, 3) Finish with review and next steps."

            input.contains("study", ignoreCase = true) || input.contains("learn", ignoreCase = true) ->
                "Focus on one skill at a time. Use 25-minute work blocks, then reinforce with a short recap and a quick test."

            input.contains("voice", ignoreCase = true) || input.contains("mic", ignoreCase = true) ->
                "Voice input is enabled. Tell me what you want to do and I’ll turn it into a clear action plan."

            else ->
                "Understood. I can help refine your idea, structure a task, or generate a focused next step." 
        }
    }

    val sendMessage = {
        val typed = prompt.trim()
        if (typed.isNotEmpty()) {
            messages.add(ChatMessage(typed, true))
            messages.add(ChatMessage(responseGenerator(typed), false))
            prompt = ""
        }
    }

    val speechLauncher = remember { 
        androidx.activity.compose.rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val spoken = result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.firstOrNull()
                if (!spoken.isNullOrBlank()) {
                    messages.add(ChatMessage(spoken, true))
                    messages.add(ChatMessage(responseGenerator(spoken), false))
                }
            }
        }
    }

    val openVoiceInput = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to VORTEX AI")
        }
        speechLauncher.launch(intent)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF070B14),
                            Color(0xFF0D1323),
                            Color(0xFF121E35)
                        )
                    )
                )
        ) {
            TopAppBar(
                title = { Text("VORTEX AI", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (message.isUser) Color(0xFF6C63FF) else Color(0xFF1B2B42),
                            shape = RoundedCornerShape(18.dp),
                            tonalElevation = 3.dp
                        ) {
                            Text(
                                text = message.text,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Ask VORTEX AI...") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7C6BFF),
                            unfocusedBorderColor = Color(0xFF4A5875),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color(0xFF7C6BFF)
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFF7A6AF5), Color(0xFF2AC5FF))))
                            .border(1.dp, Color.White.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = openVoiceInput,
                            modifier = Modifier.fillMaxSize(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "Voice input",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = sendMessage,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C6BFF))
                    ) {
                        Text(
                            text = "Send",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
