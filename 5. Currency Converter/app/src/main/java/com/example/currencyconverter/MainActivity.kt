package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import java.util.Locale

object CustomIcons {
    val CompareArrows: ImageVector
        get() {
            if (_compareArrows != null) return _compareArrows!!
            _compareArrows = ImageVector.Builder(
                name = "Compare_arrows",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(320f, 800f)
                    lineToRelative(-56f, -57f)
                    lineToRelative(103f, -103f)
                    horizontalLineTo(80f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(287f)
                    lineTo(264f, 457f)
                    lineToRelative(56f, -57f)
                    lineToRelative(200f, 200f)
                    close()

                    moveToRelative(320f, -240f)
                    lineTo(440f, 360f)
                    lineToRelative(200f, -200f)
                    lineToRelative(56f, 57f)
                    lineToRelative(-103f, 103f)
                    horizontalLineToRelative(287f)
                    verticalLineToRelative(80f)
                    horizontalLineTo(593f)
                    lineToRelative(103f, 103f)
                    close()
                }
            }.build()
            return _compareArrows!!
        }

    private var _compareArrows: ImageVector? = null
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CurrencyConverterScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CurrencyConverterScreen() {
    val currencies = mapOf(
        "USD" to "\uD83C\uDDFA\uD83C\uDDF8",
        "IDR" to "\uD83C\uDDEE\uD83C\uDDE9",
        "EUR" to "\uD83C\uDDEA\uD83C\uDDFA",
        "JPY" to "\uD83C\uDDEF\uD83C\uDDF5",
        "GBP" to "\uD83C\uDDEC\uD83C\uDDE7",
        "SGD" to "\uD83C\uDDF8\uD83C\uDDEC",
        "AUD" to "\uD83C\uDDE6\uD83C\uDDFA"
    )
    val rates = mapOf(
        "USD" to 1.0,
        "IDR" to 16795.7824,
        "EUR" to 0.8819,
        "JPY" to 143.3659,
        "GBP" to 0.7653,
        "SGD" to 1.3192,
        "AUD" to 1.5961
    )

    var fromCurrency by remember { mutableStateOf("EUR") }
    var toCurrency by remember { mutableStateOf("USD") }
    var inputAmount by remember { mutableStateOf("0") }

    val inputDouble = inputAmount.toDoubleOrNull() ?: 0.0
    val converted = if (rates[fromCurrency] != null && rates[toCurrency] != null) {
        (inputDouble / rates[fromCurrency]!!) * rates[toCurrency]!!
    } else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CurrencyDropdown(currencies, fromCurrency) { fromCurrency = it }
            IconButton(onClick = {
                val temp = fromCurrency
                fromCurrency = toCurrency
                toCurrency = temp
            }) {
                Icon(
                    imageVector = CustomIcons.CompareArrows,
                    contentDescription = "Switch currencies"
                )
            }
            CurrencyDropdown(currencies, toCurrency) { toCurrency = it }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(targetState = converted) { value ->
                Text(
                    text = String.format(Locale.getDefault(), "%,.2f %s", value, toCurrency),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$inputAmount $fromCurrency",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val rateFromUSD = rates[fromCurrency] ?: 1.0
            val rateToUSD = rates[toCurrency] ?: 1.0

            val exchangeRate = (rateToUSD / rateFromUSD)

            Text(
                text = String.format(Locale.getDefault(), "1 $fromCurrency = %.3f $toCurrency", exchangeRate),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
            )

        }

        NumericKeypad(
            input = inputAmount,
            onNumberClick = { digit ->
                inputAmount = if (inputAmount == "0") digit else inputAmount + digit
            },
            onDelete = {
                inputAmount = inputAmount.dropLast(1).ifEmpty { "0" }
            },
        )
    }
}

@Composable
fun NumericKeypad(
    input: String,
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "←")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                row.forEach { key ->
                    Button(
                        onClick = {
                            when (key) {
                                "←" -> onDelete()
                                else -> onNumberClick(key)
                            }
                        },
                        shape = RectangleShape,
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .border(1.dp, Color.White)
                    ) {
                        Text(key, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyDropdown(
    currencies: Map<String, String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${currencies[selected]} $selected",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            fontSize = 18.sp
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { (code, emoji) ->
                DropdownMenuItem(
                    text = { Text("$emoji $code") },
                    onClick = {
                        onSelect(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyConverterPreview() {
    CurrencyConverterTheme {
        CurrencyConverterScreen()
    }
}

