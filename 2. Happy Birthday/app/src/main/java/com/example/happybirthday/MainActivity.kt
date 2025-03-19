package com.example.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.happybirthday.ui.theme.HappyBirthdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappyBirthdayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BirthdayCard(
                        message = "Happy Birthday Sam!",
                        from = "From Emma"
                    )
                }
            }
        }
    }
}

@Composable
fun BirthdayCard(message: String, from: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.birthday_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .offset(y = (80).dp),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xAAFFFFFF)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = message,
                        fontSize = 50.sp,
                        lineHeight = 56.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF6A0572)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = from,
                        fontSize = 24.sp,
                        color = Color(0xFF003366),
                        modifier = Modifier.align(alignment = Alignment.End)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdayCardPreview() {
    HappyBirthdayTheme {
        BirthdayCard(message = "Happy Birthday Sam!", from = "From Emma")
    }
}
