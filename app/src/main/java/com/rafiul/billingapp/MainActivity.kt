package com.rafiul.billingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafiul.billingapp.components.InputAmountField
import com.rafiul.billingapp.ui.theme.BillingAppTheme
import com.rafiul.billingapp.ui.theme.Lavender
import com.rafiul.billingapp.utils.calculateTotalPerPerson
import com.rafiul.billingapp.utils.calculateTotalTip
import com.rafiul.billingapp.widgets.RoundIconButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {

    BillingAppTheme {
        Surface(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp), color = Color.White
        ) {
            content()
        }
    }
}


@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(all = 16.dp),
        color = Lavender,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val formattedTotal = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "$${formattedTotal}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainContent() {

    val numberOfPerson = remember {
        mutableIntStateOf(1)
    }

    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.0)
    }

    Column(
        modifier = Modifier.padding(all = 4.dp)
    ) {
        BillForm(
            numberOfPerson = numberOfPerson,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState
        ) {}
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    personRange: IntRange = 1..100,
    numberOfPerson: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValueChange: (String) -> Unit = {}
) {

    val totalBill = remember {
        mutableStateOf("")
    }

    val inputState = remember(totalBill.value) {
        totalBill.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }

    val keyboardController = LocalSoftwareKeyboardController.current


    TopHeader(totalPerPerson = totalPerPersonState.value)


    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .background(color = Color.White),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 2.dp, color = Lavender)
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .background(color = Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            InputAmountField(modifier = modifier
                .background(color = Color.White)
                .fillMaxWidth(1f),
                valueState = totalBill,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!inputState) return@KeyboardActions
                    onValueChange(totalBill.value.trim())
                    keyboardController?.hide()
                })

            if (inputState) {
                Row(
                    modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.width(120.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                numberOfPerson.value =
                                    if (numberOfPerson.value > 1) numberOfPerson.value - 1 else 1

                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBill.value.toDouble(),
                                    tipPercentage = sliderPositionState.floatValue,
                                    splitAmongPerson = numberOfPerson.value
                                )

                            },
                        )

                        Text(
                            text = "${numberOfPerson.value}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if (numberOfPerson.value < personRange.last) {
                                    numberOfPerson.value = numberOfPerson.value + 1
                                    totalPerPersonState.value = calculateTotalPerPerson(
                                        totalBill = totalBill.value.toDouble(),
                                        tipPercentage = sliderPositionState.floatValue,
                                        splitAmongPerson = numberOfPerson.value
                                    )
                                }
                            },
                        )

                    }


                }

                Row(
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {

                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.width(150.dp))

                    Text(
                        text = "$${String.format("%.0f", tipAmountState.value)}.00",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )

                }

                Column(
                    modifier = Modifier.padding(all = 6.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${
                            String.format(
                                "%.0f",
                                sliderPositionState.floatValue * 100
                            )
                        }%",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Slider(
                        value = sliderPositionState.floatValue,
                        onValueChange = { newValue ->
                            sliderPositionState.floatValue = newValue

                            tipAmountState.value =
                                calculateTotalTip(
                                    totalBill = totalBill.value.toDouble(),
                                    tipPercentage = sliderPositionState.floatValue
                                )

                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBill = totalBill.value.toDouble(),
                                tipPercentage = sliderPositionState.floatValue,
                                splitAmongPerson = numberOfPerson.value
                            )

                        },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        steps = 5,
                        onValueChangeFinished = {
                        },
                    )
                }
            } else {
                Box {
                }
            }
        }

    }
}


