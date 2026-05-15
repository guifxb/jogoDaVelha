package com.gfbdev.jogoDaVelha.presentation.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gfbdev.jogoDaVelha.data.PastPlaysRepository
import com.gfbdev.jogoDaVelha.domain.PastPlay
import com.gfbdev.jogoDaVelha.domain.usecase.GetCpuMoveUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

data class InternalGameStatus(
    val title: String,
    val message: String,
    val index: String,
    val result: GameResult
)

class GameViewModel(
    private val pastPlaysRepository: PastPlaysRepository,
    private val getCpuMoveUseCase: GetCpuMoveUseCase
) : ViewModel() {

    private var _pastPlays = MutableStateFlow(listOf(PastPlay()))
    val pastPlays = _pastPlays.asStateFlow()

    private val _player1State = mutableStateOf(TextFieldValue())
    val player1State: State<TextFieldValue> get() = _player1State

    private val _player2State = mutableStateOf(TextFieldValue())
    val player2State: State<TextFieldValue> get() = _player2State

    private val _isCPU = mutableStateOf(false)
    val isCPU get() = _isCPU

    private val _boardSize = mutableIntStateOf(3)
    val boardSize get() = _boardSize

    private val _jogoDaVelha = mutableStateOf(JogoDaVelha(boardSize.intValue))
    
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPastPlays()
    }

    fun updatePlayer1State(newValue: TextFieldValue) { _player1State.value = newValue }
    fun updatePlayer2State(newValue: TextFieldValue) { _player2State.value = newValue }

    fun updateIsCPU(newValue: Boolean) {
        _isCPU.value = newValue
        if (newValue) _player2State.value = TextFieldValue("Robô")
    }

    fun updateBoardSize(newValue: Int) { _boardSize.intValue = newValue }

    fun startGame() {
        _jogoDaVelha.value = JogoDaVelha(boardSize.intValue)
        updateUiState()
    }

    fun resetGameResult() {
        _uiState.update { it.copy(gameResult = GameResult.NONE) }
    }

    fun makeMove(row: Int, col: Int) {
        val game = _jogoDaVelha.value
        if (game.checkGameStatus() == GameStatus.IN_PROGRESS) {
            game.makeMove(Move(row, col))
            
            if (isCPU.value && game.checkGameStatus() == GameStatus.IN_PROGRESS) {
                val cpuMove = getCpuMoveUseCase(game.getBoardState())
                game.makeMove(cpuMove)
            }
            updateUiState()
        }
    }

    private fun updateUiState() {
        val game = _jogoDaVelha.value
        val status = game.checkGameStatus()
        val boardList = game.getBoardState().board.map { it.toList() }
        
        val internalStatus = when (status) {
            GameStatus.PLAYER_X_WON -> {
                saveGame(1)
                InternalGameStatus(player1State.value.text, " venceu! 🏆", "1", GameResult.WIN)
            }
            GameStatus.PLAYER_O_WON -> {
                saveGame(2)
                InternalGameStatus(
                    player2State.value.text, 
                    " venceu! 🏆", 
                    "2", 
                    if (isCPU.value) GameResult.LOSE else GameResult.WIN
                )
            }
            GameStatus.DRAW -> InternalGameStatus("", "Deu velha!", "3", GameResult.DRAW)
            else -> {
                val currentPlayerName = if (game.currentPlayer == "X") player1State.value.text else player2State.value.text
                InternalGameStatus(currentPlayerName, ", é a sua vez 🎮", if (game.currentPlayer == "X") "1" else "2", GameResult.NONE)
            }
        }

        _uiState.update {
            it.copy(
                board = boardList,
                playerTitle = internalStatus.title,
                statusMessage = internalStatus.message,
                activePlayerIndex = internalStatus.index,
                gameResult = internalStatus.result
            )
        }
    }

    private fun saveGame(winner: Int) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault())
        val formattedDate = dateFormat.format(Date())
        
        insert(PastPlay(
            player1 = player1State.value.text,
            player2 = player2State.value.text,
            winner = winner,
            date = formattedDate,
            boardSize = boardSize.intValue
        ))
    }

    private fun getPastPlays() {
        viewModelScope.launch {
            _pastPlays.update { pastPlaysRepository.getAll() }
        }
    }

    private fun insert(pastPlay: PastPlay) {
        viewModelScope.launch {
            pastPlaysRepository.insert(pastPlay)
            getPastPlays()
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            pastPlaysRepository.deleteAll()
            getPastPlays()
        }
    }
}
