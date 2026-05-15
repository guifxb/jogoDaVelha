package com.gfbdev.jogoDaVelha.presentation.viewModel

import androidx.compose.runtime.Immutable

enum class GameResult {
    NONE,
    WIN,
    LOSE,
    DRAW
}

@Immutable
data class GameUiState(
    val board: List<List<String>> = emptyList(),
    val playerTitle: String = "",
    val statusMessage: String = "",
    val activePlayerIndex: String = "1",
    val gameResult: GameResult = GameResult.NONE
)