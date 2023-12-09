package com.gfbdev.myapplication.presentation.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.gfbdev.myapplication.domain.PastPlay
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun PastGamesScreen(
    modifier: Modifier,
    pastPlays: List<PastPlay>,
    onDeleteAll: () -> Unit
) {

    var deleteAllDialogShown by remember { mutableStateOf(false) }

    if (pastPlays.isEmpty()) {
        Text(text = "Nenhuma partida encontrada.",
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp),
            textAlign = TextAlign.Center
        )
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(pastPlays) { index, pastPlay,  ->
            PastPlayCard(pastPlay)
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            if (index % 3 == 0) {
                AdBanner()
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
fun AdBanner() {
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

@Composable
fun PastPlayCard(pastPlay: PastPlay) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (pastPlay.winner == 1) "🏆 " + pastPlay.player1 else pastPlay.player1,
                    color = Color.Blue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = "  vs  ", fontSize = 18.sp)
                Text(
                    text = pastPlay.player2 + if (pastPlay.winner == 2) " 🏆" else "",
                    color = Color.Red,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${pastPlay.boardSize} x ${pastPlay.boardSize}",
                    color = Color.Gray,
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pastPlay.date,
                fontSize = 12.sp
            )
        }
    }
}

