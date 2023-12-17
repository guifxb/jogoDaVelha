package com.gfbdev.jogoDaVelha.presentation.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gfbdev.jogoDaVelha.data.PastPlaysRepository
import com.gfbdev.jogoDaVelha.domain.PastPlay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class GameViewModel(
    private val pastPlaysRepository: PastPlaysRepository,
) : ViewModel() {


    init {
        getPastPlays()
    }

    private var _pastPlays = MutableStateFlow(listOf(PastPlay()))
    val pastPlays = _pastPlays.asStateFlow()

    private val _player1State = mutableStateOf(TextFieldValue())
    val player1State: State<TextFieldValue> get() = _player1State

    private val _player2State = mutableStateOf(TextFieldValue())
    val player2State: State<TextFieldValue> get() = _player2State

    private val _isCPU = mutableStateOf(false)
    val isCPU get() = _isCPU

    private val _boardSize = mutableIntStateOf(0)
    val boardSize get() = _boardSize

    private val _jogoDaVelha = mutableStateOf(JogoDaVelha(boardSize.intValue))
    val jogoDaVelha get() = _jogoDaVelha

    private val _gameStatus = mutableStateOf(listOf("", "", ""))
    val gameStatus get() = _gameStatus



    fun updatePlayer1State(newValue: TextFieldValue) {
        _player1State.value = newValue
    }

    fun updatePlayer2State(newValue: TextFieldValue) {
        _player2State.value = newValue

    }

    fun updateIsCPU(newValue: Boolean) {
        _isCPU.value = newValue
        _player2State.value = TextFieldValue().copy("Robô")
    }

    fun updateBoardSize(newValue: Int) {
        _boardSize.intValue = newValue
    }

    fun startGame() {
        _jogoDaVelha.value = JogoDaVelha(boardSize.intValue)
        checkGameStatus()
    }

    fun makeMove(row: Int, col: Int) {
        val move = Move(row, col)

        if (_jogoDaVelha.value.checkGameStatus() == GameStatus.IN_PROGRESS) {
            _jogoDaVelha.value.makeMove(move)
            checkGameStatus()
        }

        if (isCPU.value && _jogoDaVelha.value.checkGameStatus() == GameStatus.IN_PROGRESS) {
            _jogoDaVelha.value.makeMove(_jogoDaVelha.value.makeCpuMove())
            checkGameStatus()
        }
    }

    private fun checkGameStatus() {

        val currentDate = Date()

        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault())

        val formattedDate = dateFormat.format(currentDate)

        when (_jogoDaVelha.value.checkGameStatus()) {
            GameStatus.PLAYER_X_WON -> {
                _gameStatus.value = listOf(player1State.value.text, " venceu! 🏆", "1")
                insert(PastPlay(player1 = player1State.value.text, player2 = player2State.value.text, winner = 1, date = formattedDate, boardSize = boardSize.intValue))
            }
            GameStatus.PLAYER_O_WON -> {
                _gameStatus.value = listOf(player2State.value.text, " venceu! 🏆", "2")
                insert(PastPlay(player1 = player1State.value.text, player2 = player2State.value.text, winner = 2, date = formattedDate, boardSize = boardSize.intValue))
            }
            GameStatus.DRAW -> {
                _gameStatus.value = listOf("", "👵🏼 Deu velha! 👵🏼", "3")
            }
            else -> {
                _gameStatus.value = listOf(if (_jogoDaVelha.value.currentPlayer == "X") player1State.value.text else player2State.value.text, ", é a sua vez 🎮",if (_jogoDaVelha.value.currentPlayer == "X") "1" else "2")
            }
        }
    }

    private fun getPastPlays() {
        viewModelScope.launch {
            val pastPlays = pastPlaysRepository.getAll()
            _pastPlays.update { pastPlays }

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