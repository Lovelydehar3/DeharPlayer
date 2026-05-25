package com.dehar.player.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dehar.player.data.PreferencesManager
import com.dehar.player.ui.theme.DeharBackground
import com.dehar.player.ui.theme.DeharBlue
import com.dehar.player.ui.theme.DeharSurface
import kotlinx.coroutines.launch

@Composable
fun LockScreen(
    preferencesManager: PreferencesManager,
    onUnlocked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var isPinSet by remember { mutableStateOf(false) }
    var step by remember { mutableStateOf(LockStep.ENTER_PIN) }
    var inputPin by remember { mutableStateOf("") }
    var originalPin by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        val pinSet = preferencesManager.isPinSet()
        isPinSet = pinSet
        step = if (pinSet) LockStep.ENTER_PIN else LockStep.CREATE_PIN
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeharBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top status or back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (step == LockStep.CONFIRM_PIN) {
                IconButton(
                    onClick = {
                        step = LockStep.CREATE_PIN
                        inputPin = ""
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        // Header Message
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // App name / Branding
            Text(
                text = "DEHAR PLAYER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DeharBlue,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Local Pass Lock",
                fontSize = 14.sp,
                color = Color.Gray,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(48.dp))

            val headerText = when (step) {
                LockStep.ENTER_PIN -> "Enter your 4-digit passcode"
                LockStep.CREATE_PIN -> "Set a 4-digit passcode"
                LockStep.CONFIRM_PIN -> "Confirm your passcode"
            }
            Text(
                text = headerText,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Passcode dots indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until 4) {
                    val active = i < inputPin.length
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (active) DeharBlue else DeharSurface)
                    )
                }
            }
        }

        // Numerical Keyboard
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("", "0", "DEL")
            )

            for (row in rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (key in row) {
                        if (key.isEmpty()) {
                            // Empty placeholder for symmetry
                            Box(modifier = Modifier.size(72.dp))
                        } else {
                            KeyButton(
                                text = key,
                                onClick = {
                                    if (key == "DEL") {
                                        if (inputPin.isNotEmpty()) {
                                            inputPin = inputPin.dropLast(1)
                                        }
                                    } else {
                                        if (inputPin.length < 4) {
                                            inputPin += key
                                            if (inputPin.length == 4) {
                                                // Process completed passcode
                                                val pin = inputPin
                                                scope.launch {
                                                    when (step) {
                                                        LockStep.ENTER_PIN -> {
                                                            if (preferencesManager.verifyPin(pin)) {
                                                                onUnlocked()
                                                            } else {
                                                                Toast.makeText(context, "Wrong passcode", Toast.LENGTH_SHORT).show()
                                                                inputPin = ""
                                                            }
                                                        }
                                                        LockStep.CREATE_PIN -> {
                                                            originalPin = pin
                                                            step = LockStep.CONFIRM_PIN
                                                            inputPin = ""
                                                        }
                                                        LockStep.CONFIRM_PIN -> {
                                                            if (pin == originalPin) {
                                                                preferencesManager.setPin(pin)
                                                                Toast.makeText(context, "Passcode set", Toast.LENGTH_SHORT).show()
                                                                onUnlocked()
                                                            } else {
                                                                Toast.makeText(context, "Passcodes do not match. Try again.", Toast.LENGTH_SHORT).show()
                                                                step = LockStep.CREATE_PIN
                                                                inputPin = ""
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(DeharSurface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (text == "DEL") {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = text,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

enum class LockStep {
    ENTER_PIN, CREATE_PIN, CONFIRM_PIN
}
