package com.gfbdev.myapplication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.gfbdev.myapplication.presentation.viewModel.JogoDaVelha
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun GameScreen(
    modifier: Modifier,
    currentGame: JogoDaVelha,
    gameState: List<String>,
    onMakeMove: (Int, Int) -> Unit,
    onRestartClicked: () -> Unit,

    ) {
    val boardState = currentGame.getBoardState().board

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = if (gameState[2] == "1") Color.Blue else Color.Red
                    )
                ) {
                    append(gameState[0])
                }
                append(gameState[1])
            },
            style = TextStyle(fontSize = 20.sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            for (row in boardState.indices) {
                Row {
                    for (col in boardState[row].indices) {
                        val cell = boardState[row][col]
                        Box(
                            modifier = Modifier
                                .border(2.dp, MaterialTheme.colorScheme.primary)
                                .background(color = if (cell == "") MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.secondaryContainer)
                                .size(itemSize(LocalDensity.current, boardState.size))
                                .clickable {
                                    if (cell == "") {
                                        onMakeMove(row, col)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cell,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (cell == "X") Color.Blue else Color.Red,
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = { onRestartClicked() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Reiniciar")
        }

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}



private fun itemSize(density: Density, size: Int): Dp {

    val availableSize = 300.dp
    return with(density) {
        availableSize / size.toFloat()
    }
}




