package com.vortex.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vortex.ai.ui.theme.VortexAITheme

private data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VortexAITheme {
                VortexAIScreen()
            }
        }
    }
}

@Composable
fun VortexAIScreen() {
    var prompt by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hello, I am VORTEX AI. Ask me anything.", false),
            ChatMessage("Help me plan my day.", true)
        )
    }

    val responseGenerator: (String) -> String = { input ->
        when {
            input.contains("hello", ignoreCase = true) || input.contains("hi", ignoreCase = true) ->
                "Hello! I’m VORTEX AI. I’m here to help you with planning, study, and productivity."

            input.contains("plan", ignoreCase = true) || input.contains("day", ignoreCase = true) ->
                "Here’s a simple plan: 1) Start with your top priority, 2) Complete one important task before distractions, 3) Review what you accomplished at the end of the day."

            input.contains("study", ignoreCase = true) || input.contains("learn", ignoreCase = true) ->
                "Use the 25/5 method: 25 minutes of focus, 5 minutes of break, then repeat. Build one small goal at a time."

            input.contains("idea", ignoreCase = true) || input.contains("creative", ignoreCase = true) ->
                "Try this: build a mini project that solves one real problem in your life, then improve it in small steps."

            else ->
                "I’m VORTEX AI. I can help with productivity, planning, quick summaries, creativity, and smart suggestions."
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
                            Color(0xFF090D18),
                            Color(0xFF101A2F),
                            Color(0xFF1A1D2D)
                        )
                    )
                )
        ) {
            TopAppBar(
                title = { Text("VORTEX AI", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
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
                            color = if (message.isUser) Color(0xFF6A5CFF) else Color(0xFF1C2B3F),
                            shape = RoundedCornerShape(18.dp),
                            tonalElevation = 2.dp
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
                    .padding(horizontal = 16.dp, vertical = 12.dp)
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

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = sendMessage,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C6BFF)
                        )
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
