package com.gfbdev.jogoDaVelha.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.gfbdev.jogoDaVelha.R
import com.gfbdev.jogoDaVelha.presentation.viewModel.GameUiState
import com.gfbdev.jogoDaVelha.ui.theme.Dimens
import com.gfbdev.jogoDaVelha.ui.theme.PlayerOColor
import com.gfbdev.jogoDaVelha.ui.theme.PlayerXColor
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    uiState: GameUiState,
    onMakeMove: (Int, Int) -> Unit,
    onRestartClicked: () -> Unit,
) {
    val bannerAd = LocalContext.current.getString(R.string.AD_BANNER_KEY)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (uiState.activePlayerIndex == "1") PlayerXColor else PlayerOColor
                        )
                    ) {
                        append(uiState.playerTitle)
                    }
                    append(uiState.statusMessage)
                },
                style = TextStyle(fontSize = Dimens.LargeFont),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(Dimens.ComponentPadding)
            )

            Column(
                modifier = Modifier
                    .padding(Dimens.ComponentPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                for (rowIndex in uiState.board.indices) {
                    Row {
                        for (colIndex in uiState.board[rowIndex].indices) {
                            val cell = uiState.board[rowIndex][colIndex]
                            Box(
                                modifier = Modifier
                                    .border(2.dp, MaterialTheme.colorScheme.primary)
                                    .background(color = if (cell == "") MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.secondaryContainer)
                                    .size(itemSize(LocalDensity.current, uiState.board.size))
                                    .clickable {
                                        if (cell == "") {
                                            onMakeMove(rowIndex, colIndex)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cell,
                                    fontSize = Dimens.LargeFont,
                                    fontWeight = FontWeight.Bold,
                                    color = if (cell == "X") PlayerXColor else PlayerOColor,
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { onRestartClicked() },
                modifier = Modifier.padding(Dimens.ComponentPadding)
            ) {
                Text("Reiniciar")
            }

            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = bannerAd
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}

private fun itemSize(density: Density, size: Int): Dp {
    val availableSize = 300.dp
    return with(density) {
        availableSize / size.toFloat()
    }
}
