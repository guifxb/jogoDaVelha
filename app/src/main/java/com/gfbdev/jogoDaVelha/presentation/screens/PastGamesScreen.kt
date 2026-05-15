package com.gfbdev.jogoDaVelha.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.gfbdev.jogoDaVelha.R
import com.gfbdev.jogoDaVelha.domain.PastPlay
import com.gfbdev.jogoDaVelha.ui.theme.Dimens
import com.gfbdev.jogoDaVelha.ui.theme.PlayerXColor
import com.gfbdev.jogoDaVelha.ui.theme.PlayerOColor
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun PastGamesScreen(
    modifier: Modifier = Modifier,
    pastPlays: List<PastPlay>,
    onDeleteAll: () -> Unit
) {

    var deleteAllDialogShown by remember { mutableStateOf(false) }
    val bannerAd = LocalContext.current.getString(R.string.AD_BANNER_KEY)

    if (pastPlays.isEmpty()) {
        Text(text = "Nenhuma partida encontrada.",
            modifier = Modifier
                .padding(Dimens.ScreenPadding)
                .size(48.dp),
            textAlign = TextAlign.Center
        )
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(Dimens.ScreenPadding)
    ) {
        itemsIndexed(pastPlays) { index, pastPlay,  ->
            PastPlayCard(pastPlay)
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = Dimens.ComponentPadding)
            )
            if (index % 3 == 0) {
                AdBanner(bannerAd)
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = Dimens.ComponentPadding)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                deleteAllDialogShown = true
            },
            content = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Limpar tudo"
                )
            }
        )

        if (deleteAllDialogShown) {
            AlertDialog(
                onDismissRequest = {
                    deleteAllDialogShown = false
                },
                title = { Text(text = "Limpar tudo") },
                text = {
                    Text(text = "Tem certeza de que deseja limpar todos os registros?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            deleteAllDialogShown = false
                            onDeleteAll()
                        }
                    ) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            deleteAllDialogShown = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun AdBanner(bannerAd: String) {
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

@Composable
fun PastPlayCard(pastPlay: PastPlay) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.ComponentPadding),
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.ItemPadding)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (pastPlay.winner == 1) "🏆 " + pastPlay.player1 else pastPlay.player1,
                    color = PlayerXColor,
                    fontSize = Dimens.MediumFont,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = "  vs  ", fontSize = Dimens.MediumFont)
                Text(
                    text = pastPlay.player2 + if (pastPlay.winner == 2) " 🏆" else "",
                    color = PlayerOColor,
                    fontSize = Dimens.MediumFont,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${pastPlay.boardSize} x ${pastPlay.boardSize}",
                    color = Color.Gray,
                    fontSize = Dimens.SmallFont,
                )
            }
            Spacer(modifier = Modifier.height(Dimens.ComponentPadding))
            Text(
                text = pastPlay.date,
                fontSize = Dimens.SmallFont
            )
        }
    }
}

