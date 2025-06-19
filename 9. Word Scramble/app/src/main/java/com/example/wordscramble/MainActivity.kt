package com.example.wordscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordscramble.ui.theme.WordScrambleTheme
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordScrambleTheme {
                WordScrambleGame()
            }
        }
    }
}

enum class Difficulty(val label: String, val wordLength: IntRange) {
    EASY("Easy", 3..5),
    MEDIUM("Medium", 6..8),
    HARD("Hard", 9..12)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun WordScrambleGame() {
    // Game state
    var score by remember { mutableStateOf(0) }
    var currentWord by remember { mutableStateOf("") }
    var scrambledWord by remember { mutableStateOf("") }
    var userGuess by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showWord by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    var gameActive by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(60) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.MEDIUM) }

    // Extended word list (categorized by difficulty)
    val wordList = remember {
        mapOf(
            Difficulty.EASY to listOf(
                "cat", "dog", "sun", "run", "big", "red", "fun", "top", "hat", "pen",
                "cup", "key", "map", "jam", "leg", "arm", "eye", "bed", "car", "bus"
            ),
            Difficulty.MEDIUM to listOf(
                "android", "kotlin", "mobile", "coding", "laptop", "screen", "keyboard",
                "mouse", "window", "button", "garden", "flower", "summer", "winter",
                "orange", "purple", "circle", "square", "rocket", "planet"
            ),
            Difficulty.HARD to listOf(
                "programming", "algorithm", "developer", "interface", "architecture",
                "encryption", "repository", "javascript", "framework", "responsive",
                "sustainability", "environmental", "communication", "administration",
                "quantum", "blockchain", "cryptocurrency", "metaverse", "artificial",
                "biometrics"
            )
        )
    }

    // Game logic
    fun scrambleWord(word: String): String {
        return word.toCharArray().toList().shuffled().joinToString("")
    }

    fun pickNewWord() {
        val filteredWords = wordList[selectedDifficulty] ?: emptyList()
        if (filteredWords.isNotEmpty()) {
            currentWord = filteredWords.random()
            scrambledWord = scrambleWord(currentWord)
            userGuess = ""
            showWord = false
            message = ""
        }
    }

    fun startGame() {
        score = 0
        timeLeft = 60
        gameActive = true
        pickNewWord()
    }

    fun endGame() {
        gameActive = false
        message = "Game Over! Final Score: $score"
    }

    fun checkAnswer() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            score += when (selectedDifficulty) {
                Difficulty.EASY -> 5
                Difficulty.MEDIUM -> 10
                Difficulty.HARD -> 15
            }
            message = "Correct! +${when (selectedDifficulty) {
                Difficulty.EASY -> 5
                Difficulty.MEDIUM -> 10
                Difficulty.HARD -> 15
            }} points"
            showWord = true
            isAnimating = true
        } else {
            message = "Wrong! Try again"
        }
    }

    fun skipWord() {
        score = maxOf(0, score - 5)
        message = "Skipped! -5 points"
        showWord = true
        isAnimating = true
    }

    // Timer logic
    LaunchedEffect(gameActive, timeLeft) {
        if (gameActive && timeLeft > 0) {
            delay(1.seconds)
            timeLeft--
            if (timeLeft == 0) {
                endGame()
            }
        }
    }

    // Auto proceed to next word
    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            delay(1500)
            pickNewWord()
            isAnimating = false
        }
    }

    // UI Layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Game header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Score: $score",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (gameActive) {
                    Text(
                        text = "Time: $timeLeft",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            timeLeft > 30 -> Color.Green
                            timeLeft > 10 -> Color(0xFFFFA500) // Orange
                            else -> Color.Red
                        }
                    )
                }
            }

            // Difficulty selector (only before game starts)
            if (!gameActive) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Difficulty",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Difficulty.values().forEach { difficulty ->
                            val selected = difficulty == selectedDifficulty
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .selectable(
                                        selected = selected,
                                        onClick = { selectedDifficulty = difficulty }
                                    ),
                                shape = MaterialTheme.shapes.medium,
                                color = if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                shadowElevation = if (selected) 8.dp else 2.dp
                            ) {
                                Text(
                                    text = difficulty.label,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Game area (only when game is active)
            if (gameActive) {
                // Scrambled word card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Scrambled Word",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = scrambledWord,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Original word (shown after answer/skip)
                AnimatedVisibility(
                    visible = showWord,
                    enter = fadeIn() + expandVertically(animationSpec = tween(300)),
                    exit = fadeOut() + shrinkVertically(animationSpec = tween(300))
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Original Word",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = currentWord,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // Guess input
                OutlinedTextField(
                    value = userGuess,
                    onValueChange = { userGuess = it },
                    label = { Text("Your guess") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isAnimating && gameActive,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Message display
                AnimatedVisibility(
                    visible = message.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Text(
                        text = message,
                        color = when {
                            message.startsWith("Correct") -> Color(0xFF4CAF50) // Green
                            message.startsWith("Wrong") -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.secondary
                        },
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            checkAnswer()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isAnimating && userGuess.isNotEmpty() && gameActive,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Submit", fontSize = 16.sp)
                    }

                    Button(
                        onClick = {
                            skipWord()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        enabled = !isAnimating && gameActive
                    ) {
                        Text("Skip", fontSize = 16.sp)
                    }
                }

                // Hint
                Text(
                    text = "Hint: ${currentWord.length} letters",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                // Start game or game over screen
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    Button(
                        onClick = { startGame() },
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Start Game", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordScramblePreview() {
    WordScrambleTheme {
        WordScrambleGame()
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WordScrambleDarkPreview() {
    WordScrambleTheme {
        WordScrambleGame()
    }
}