import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val buttonColor = Color(0xFF7E57C2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Simple Calculator",
            fontSize = 24.sp,
            color = buttonColor,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = a,
            onValueChange = { a = it },
            label = { Text("Enter number A") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = b,
            onValueChange = { b = it },
            label = { Text("Enter number B") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val btnModifier = Modifier.weight(1f)

            CalculatorButton("Add", buttonColor, btnModifier) {
                val numA = a.toFloatOrNull()
                val numB = b.toFloatOrNull()
                result = if (numA != null && numB != null) (numA + numB).toString() else "Invalid input"
            }

            CalculatorButton("Sub", buttonColor, btnModifier) {
                val numA = a.toFloatOrNull()
                val numB = b.toFloatOrNull()
                result = if (numA != null && numB != null) (numA - numB).toString() else "Invalid input"
            }

            CalculatorButton("Mul", buttonColor, btnModifier) {
                val numA = a.toFloatOrNull()
                val numB = b.toFloatOrNull()
                result = if (numA != null && numB != null) (numA * numB).toString() else "Invalid input"
            }

            CalculatorButton("Div", buttonColor, btnModifier) {
                val numA = a.toFloatOrNull()
                val numB = b.toFloatOrNull()
                result = if (numA != null && numB != null && numB != 0f) (numA / numB).toString() else "Invalid input"
            }
        }

        if (result.isNotEmpty()) {
            Text(
                text = "Result: $result",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun CalculatorButton(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(50),
        modifier = modifier.height(48.dp)
    ) {
        Text(label, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculatorApp() {
    CalculatorApp()
}
