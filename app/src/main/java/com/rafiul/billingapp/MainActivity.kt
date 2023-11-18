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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafiul.billingapp.components.InputAmountField
import com.rafiul.billingapp.ui.theme.BillingAppTheme
import com.rafiul.billingapp.ui.theme.CustomTheme
import com.rafiul.billingapp.ui.theme.Lavender
import com.rafiul.billingapp.widgets.RoundIconButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
//                TopHeader()
                MainContent()

            }

        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {

    BillingAppTheme {
        CustomTheme {
            Surface(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                color = Color.White
            ) {
                content()
            }
        }

    }
}

@Preview()
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color = Lavender
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


@Preview
@Composable
fun MainContent() {


    BillForm() { billAmount ->

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {

    val totalBill = remember {
        mutableStateOf("")
    }

    val inputState = remember(totalBill.value) {
        totalBill.value.trim().isNotEmpty()
    }

    val numberOfPerson = remember {
        mutableIntStateOf(1)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

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
            InputAmountField(
                modifier = modifier
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
                }
            )

            if (inputState) {
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start
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
                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                            if (numberOfPerson.intValue >  1) {
                              numberOfPerson.intValue =  numberOfPerson.intValue - 1
                            }
                        })

                        Text(
                            text = "${numberOfPerson.intValue}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                            numberOfPerson.intValue = numberOfPerson.intValue + 1
                        })

                    }

                }
            } else {
                Box {

                }
            }
        }

    }
}


@Composable
fun GreetingPreview() {
    BillingAppTheme {
        MyApp {

        }
    }
}