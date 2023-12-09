package com.gfbdev.myapplication.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gfbdev.myapplication.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun StartScreen(
    player1State: TextFieldValue,
    onPlayer1Changed: (TextFieldValue) -> Unit,
    player2State: TextFieldValue,
    onPlayer2Changed: (TextFieldValue) -> Unit,
    onGameClicked: (Int) -> Unit,
    onHistoryClicked: () -> Unit,
    isCPU: Boolean,
    onCPUChanged: (Boolean) -> Unit,
) {
    val logo = painterResource(R.drawable.start_screen)

    val keyboardController = LocalSoftwareKeyboardController.current

    val boardSizes = listOf("3x3", "4x4", "5x5", "6x6", "7x7", "8x8", "9x9", "10x10")
    var selectedBoardSize by remember { mutableIntStateOf(0) }

    var player1Error by rememberSaveable { mutableStateOf(false) }
    var player2Error by rememberSaveable { mutableStateOf(false) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 3

    fun validate(text: String): Boolean {
        return " " in text || text.isEmpty()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = logo,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .aspectRatio(1.5f)
        )

        OutlinedTextField(
            value = player1State,
            onValueChange = onPlayer1Changed,
            isError = player1Error,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    player1Error = validate(player1State.text)
                }
            ),
            label = { Text("Jogador 1") },
            modifier = Modifier.padding(4.dp),
            singleLine = true,
            supportingText = {
                if (player1Error) {
                    Text(
                        "Nome inválido. Não use espaço em branco.",
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Spacer(modifier = Modifier.width(itemWidth))
            OutlinedTextField(
                value = player2State,
                onValueChange = onPlayer2Changed,
                label = { Text("Jogador 2") },
                modifier = Modifier.width(itemWidth).weight(0.33f),
                singleLine = true,
                enabled = !isCPU,
                isError = player2Error,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    player2Error = validate(player2State.text) }
                ),
                supportingText = {
                    if (player2Error) {
                        Text(
                            "Nome inválido. Não use espaço em branco.",
                        )
                    }
                }
            )
            SegmentedControl(
                modifier = Modifier.width(itemWidth).weight(0.33f),
                defaultSelectedItemIndex = if (isCPU) 1 else 0,
                items = listOf(
                    "👵🏼",
                    "🤖",
                )
            ) {
                onCPUChanged(it == 1)
            }
        }

        Text(
            text = "Tamanho do tabuleiro",
            style = LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            ),
        )

        SegmentedControl(
            items = boardSizes,
            defaultSelectedItemIndex = selectedBoardSize,
            onItemSelection = { selectedBoardSize = it }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentWidth()
                    .fillMaxWidth(0.5f), onClick = {
                    player1Error = validate(player1State.text)
                    player2Error = validate(player2State.text)
                    if (!player1Error && !player2Error) {
                        onGameClicked(selectedBoardSize + 3)
                    }
                }
            ) {
                Text(
                    "Jogar",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentWidth()
                    .fillMaxWidth(), onClick = { onHistoryClicked() }
            ) {
                Text(
                    text = "Histórico",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}

@Composable
fun SegmentedControl(
    modifier: Modifier = Modifier,
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    itemWidth: Dp = 120.dp,
    cornerRadius: Int = 10,
    color: Color = MaterialTheme.colorScheme.primary,
    onItemSelection: (selectedItemIndex: Int) -> Unit,
) {
    val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }

    LazyVerticalGrid(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, bottom = 0.dp, top = 0.dp),
        columns = GridCells.Fixed(if (items.size < 4) items.size else 4),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        itemsIndexed(items) { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    4, 5, 6, 7 -> {
                        modifier
                            .width(itemWidth)
                            .offset(0.dp, (-10).dp)
                    }

                    else -> {
                        modifier
                            .width(itemWidth)
                    }
                },
                onClick = {
                    selectedIndex.value = index
                    onItemSelection(selectedIndex.value)
                },
                shape = when (index) {
                    0 -> if (items.size < 4) {
                        RoundedCornerShape(
                            topStartPercent = cornerRadius,
                            topEndPercent = 0,
                            bottomStartPercent = cornerRadius,
                            bottomEndPercent = 0
                        )
                    } else {
                        RoundedCornerShape(
                            topStartPercent = cornerRadius,
                            topEndPercent = 0,
                            bottomStartPercent = 0,
                            bottomEndPercent = 0
                        )
                    }

                    3 -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = cornerRadius,
                        bottomStartPercent = 0,
                        bottomEndPercent = 0
                    )

                    4 -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomStartPercent = cornerRadius,
                        bottomEndPercent = 0
                    )

                    items.size - 1 -> if (items.size < 4) {
                        RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = cornerRadius,
                            bottomStartPercent = 0,
                            bottomEndPercent = cornerRadius
                        )
                    } else {
                        RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 0,
                            bottomStartPercent = 0,
                            bottomEndPercent = cornerRadius
                        )
                    }

                    else -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomStartPercent = 0,
                        bottomEndPercent = 0
                    )
                },
                border = BorderStroke(
                    1.dp,
                    if (selectedIndex.value == index) {
                        color
                    } else {
                        color.copy(alpha = 0.75f)
                    }
                ),
                colors = if (selectedIndex.value == index) {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = color
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                },
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light,
                    color = if (selectedIndex.value == index) {
                        Color.White
                    } else {
                        color.copy(alpha = 0.9f)
                    },
                )
            }
        }
    }
}