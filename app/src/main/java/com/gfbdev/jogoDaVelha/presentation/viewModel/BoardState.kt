package com.gfbdev.jogoDaVelha.presentation.viewModel

data class BoardState(val size: Int) {
    val board = Array(size) { Array(size) { "" } }

    fun copy(): BoardState {
        val newBoard = BoardState(size)
        for (i in 0 until size) {
            for (j in 0 until size) {
                newBoard.setCell(i, j, this.getCell(i, j))
            }
        }
        return newBoard
    }

    fun getCell(row: Int, col: Int): String = board[row][col]
    fun setCell(row: Int, col: Int, value: String) { board[row][col] = value }

    fun checkWin(player: String): Boolean {
        for (i in 0 until size) {
            if ((0 until size).all { j -> getCell(i, j) == player } ||
                (0 until size).all { j -> getCell(j, i) == player }
            ) return true
        }
        if ((0 until size).all { i -> getCell(i, i) == player } ||
            (0 until size).all { i -> getCell(i, size - 1 - i) == player }
        ) return true
        return false
    }

    fun isDraw(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    fun getAvailableMoves(): List<Move> {
        val moves = mutableListOf<Move>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (getCell(i, j).isEmpty()) moves.add(Move(i, j))
            }
        }
        return moves
    }
}

data class Move(val row: Int, val col: Int)

class JogoDaVelha(private val boardSize: Int) {
    private var boardState = BoardState(boardSize)
    var currentPlayer = "X"

    fun makeMove(move: Move): Boolean {
        if (isValidMove(move)) {
            boardState.setCell(move.row, move.col, currentPlayer)
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            return true
        }
        return false
    }

    private fun isValidMove(move: Move): Boolean {
        return move.row in 0 until boardSize &&
               move.col in 0 until boardSize &&
               boardState.getCell(move.row, move.col).isEmpty()
    }

    fun checkGameStatus(): GameStatus {
        if (boardState.checkWin("X")) return GameStatus.PLAYER_X_WON
        if (boardState.checkWin("O")) return GameStatus.PLAYER_O_WON
        if (boardState.isDraw()) return GameStatus.DRAW
        return GameStatus.IN_PROGRESS
    }

    fun getBoardState(): BoardState = boardState
}

enum class GameStatus {
    IN_PROGRESS, PLAYER_X_WON, PLAYER_O_WON, DRAW
}
