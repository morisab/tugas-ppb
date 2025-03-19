package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
    }
}

@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage()
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = result.toString(),
            modifier = Modifier
                .size(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$result",
            fontSize = 24.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (!isRolling) {
                    isRolling = true
                    coroutineScope.launch {
                        repeat(10) {
                            result = (1..6).random()
                            delay(100)
                        }
                        isRolling = false
                    }
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .size(width = 200.dp, height = 60.dp),
            enabled = !isRolling
        ) {
            Text(
                text = if (isRolling) "Rolling..." else stringResource(R.string.roll),
                fontSize = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerPreview() {
    DiceRollerTheme {
        DiceRollerApp()
    }
}
