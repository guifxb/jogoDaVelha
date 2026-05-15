package com.gfbdev.jogoDaVelha.presentation.screens

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
import com.gfbdev.jogoDaVelha.R
import com.gfbdev.jogoDaVelha.ui.theme.Dimens
import com.google.common.collect.ImmutableList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    player1State: TextFieldValue,
    onPlayer1Changed: (TextFieldValue) -> Unit,
    player2State: TextFieldValue,
    onPlayer2Changed: (TextFieldValue) -> Unit,
    onGameClicked: (Int) -> Unit,
    onHistoryClicked: () -> Unit,
    isCPU: Boolean,
    onCPUChanged: (Boolean) -> Unit,
) {
    val boardSizes = remember {
        ImmutableList.of("3x3", "4x4", "5x5", "6x6", "7x7", "8x8", "9x9", "10x10")
    }
    
    var selectedBoardSize by remember { mutableIntStateOf(0) }
    var player1Error by rememberSaveable { mutableStateOf(false) }
    var player2Error by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    fun validate(text: String): Boolean = " " in text || text.isEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        HeaderSection()

        PlayersInputSection(
            player1State = player1State,
            onPlayer1Changed = onPlayer1Changed,
            player1Error = player1Error,
            player2State = player2State,
            onPlayer2Changed = onPlayer2Changed,
            player2Error = player2Error,
            isCPU = isCPU,
            onCPUChanged = onCPUChanged,
            onPlayer1Done = {
                keyboardController?.hide()
                player1Error = validate(player1State.text)
            },
            onPlayer2Done = {
                keyboardController?.hide()
                player2Error = validate(player2State.text)
            }
        )

        BoardSizeSection(
            boardSizes = boardSizes,
            selectedBoardSize = selectedBoardSize,
            onSizeSelected = { selectedBoardSize = it }
        )

        ActionButtonsSection(
            onPlayClicked = {
                player1Error = validate(player1State.text)
                player2Error = validate(player2State.text)
                if (!player1Error && !player2Error) {
                    onGameClicked(selectedBoardSize + 3)
                }
            },
            onHistoryClicked = onHistoryClicked
        )
    }
}

@Composable
private fun HeaderSection() {
    Image(
        painter = painterResource(R.drawable.start_screen),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(fraction = 0.6f)
            .aspectRatio(1.5f)
    )
}

@Composable
private fun PlayersInputSection(
    player1State: TextFieldValue,
    onPlayer1Changed: (TextFieldValue) -> Unit,
    player1Error: Boolean,
    player2State: TextFieldValue,
    onPlayer2Changed: (TextFieldValue) -> Unit,
    player2Error: Boolean,
    isCPU: Boolean,
    onCPUChanged: (Boolean) -> Unit,
    onPlayer1Done: () -> Unit,
    onPlayer2Done: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 3

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = player1State,
            onValueChange = onPlayer1Changed,
            isError = player1Error,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onPlayer1Done() }),
            label = { Text("Jogador 1") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = {
                if (player1Error) {
                    Text("Nome inválido. Não use espaço.", fontSize = Dimens.SmallFont)
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = player2State,
                onValueChange = onPlayer2Changed,
                label = { Text("Jogador 2") },
                modifier = Modifier
                    .width(itemWidth)
                    .weight(0.33f),
                singleLine = true,
                enabled = !isCPU,
                isError = player2Error,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onPlayer2Done() }),
                supportingText = {
                    if (player2Error) {
                        Text("Nome inválido. Não use espaço.", fontSize = Dimens.SmallFont)
                    }
                }
            )
            SegmentedControl(
                modifier = Modifier
                    .width(itemWidth)
                    .weight(0.20f),
                defaultSelectedItemIndex = if (isCPU) 1 else 0,
                items= ImmutableList.of("👵🏼", "🤖"),
                onItemSelection = { onCPUChanged(it == 1) }
            )
        }
    }
}

@Composable
private fun BoardSizeSection(
    boardSizes: ImmutableList<String>,
    selectedBoardSize: Int,
    onSizeSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Tamanho do tabuleiro",
            style = LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = Dimens.MediumFont
            ),
        )

        SegmentedControl(
            items = boardSizes,
            defaultSelectedItemIndex = selectedBoardSize,
            onItemSelection = onSizeSelected
        )
    }
}

@Composable
private fun ActionButtonsSection(
    onPlayClicked: () -> Unit,
    onHistoryClicked: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier
                .padding(Dimens.ItemPadding)
                .wrapContentWidth()
                .fillMaxWidth(0.5f),
            onClick = onPlayClicked
        ) {
            Text("Jogar", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
        Button(
            modifier = Modifier
                .padding(Dimens.ItemPadding)
                .wrapContentWidth()
                .fillMaxWidth(),
            onClick = onHistoryClicked
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

@Composable
fun SegmentedControl(
    modifier: Modifier = Modifier,
    items: ImmutableList<String>,
    defaultSelectedItemIndex: Int = 0,
    itemWidth: Dp = 120.dp,
    cornerRadius: Int = 10,
    color: Color = MaterialTheme.colorScheme.primary,
    onItemSelection: (selectedItemIndex: Int) -> Unit,
) {
    val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }

    LazyVerticalGrid(
        modifier = modifier.padding(Dimens.ItemPadding),
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
                    onItemSelection(index)
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
                    if (selectedIndex.value == index) color else color.copy(alpha = 0.75f)
                ),
                colors = if (selectedIndex.value == index) {
                    ButtonDefaults.outlinedButtonColors(containerColor = color)
                } else {
                    ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                },
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light,
                    color = if (selectedIndex.value == index) Color.White else color.copy(alpha = 0.9f),
                )
            }
        }
    }
}
