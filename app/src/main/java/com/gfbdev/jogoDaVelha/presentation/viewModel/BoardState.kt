package com.gfbdev.jogoDaVelha.presentation.viewModel

import kotlin.random.Random

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

    fun getCell(row: Int, col: Int): String {
        return board[row][col]
    }

    fun setCell(row: Int, col: Int, value: String) {
        board[row][col] = value
    }

    fun checkWin(player: String): Boolean {
        for (i in 0 until size) {
            if ((0 until size).all { j -> getCell(i, j) == player } ||
                (0 until size).all { j -> getCell(j, i) == player }
            ) {
                return true
            }
        }

        if ((0 until size).all { i -> getCell(i, i) == player } ||
            (0 until size).all { i -> getCell(i, size - 1 - i) == player }
        ) {
            return true
        }

        return false
    }

    fun isDraw(): Boolean {
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (getCell(i, j).isEmpty()) {
                    return false
                }
            }
        }
        return true
    }

    fun getAvailableMoves(): List<Move> {
        val moves = mutableListOf<Move>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (getCell(i, j).isEmpty()) {
                    moves.add(Move(i, j))
                }
            }
        }
        return moves.toList()
    }
}

data class Move(val row: Int, val col: Int)

class JogoDaVelha(
    private val boardSize: Int
) {
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
        return (
                move.row in 0 until boardSize &&
                        move.col in 0 until boardSize &&
                        boardState.getCell(move.row, move.col).isEmpty()
                )
    }

    fun checkGameStatus(): GameStatus {
        if (boardState.checkWin("X")) {
            return GameStatus.PLAYER_X_WON
        }
        if (boardState.checkWin("O")) {
            return GameStatus.PLAYER_O_WON
        }
        if (boardState.isDraw()) {
            return GameStatus.DRAW
        }
        return GameStatus.IN_PROGRESS
    }

    fun getBoardState(): BoardState {
        return boardState
    }

    fun makeCpuMove(): Move {
        val winningMove = findWinningMove(boardState)
        if (winningMove != null) {
            return winningMove
        }

        val smartMove = makeSmartMove(boardState)
        if (smartMove != null) {
            return smartMove
        }

        val availableMoves = boardState.getAvailableMoves()
        return if (availableMoves.isNotEmpty()) availableMoves[Random.nextInt(availableMoves.size)]
        else Move(-1, -1)
    }

    private fun findWinningMove(board: BoardState): Move? {
        val availableMoves = board.getAvailableMoves()
        for (move in availableMoves) {
            val newBoard = board.copy()
            newBoard.setCell(move.row, move.col, "O")
            if (newBoard.checkWin("O")) {
                return move
            }
            newBoard.setCell(move.row, move.col, "X")
            if (newBoard.checkWin("X")) {
                return move
            }
        }
        return null
    }

    private fun makeSmartMove(board: BoardState): Move? {
        val player = "O"
        val availableMoves = board.getAvailableMoves()

        //Verificando laterais
        for (move in availableMoves) {
            if (move.row > 0) {
                val leftMove = Move(move.row, move.col - 1)
                if (isValidMove(leftMove, board) && board.getCell(leftMove.row, leftMove.col) == player) {
                    return move
                }
            }
            if (move.row < board.size - 1) {
                val rightMove = Move(move.row, move.col + 1)
                if (isValidMove(rightMove, board) && board.getCell(rightMove.row, rightMove.col) == player) {
                    return move
                }
            }
        }

        //Verificando em cima e embaixo
        for (move in availableMoves) {
            if (move.row > 0) {
                val aboveMove = Move(move.row - 1, move.col)
                if (board.getCell(aboveMove.row, aboveMove.col) == player) {
                    return move
                }
            }
            if (move.row < board.size - 1) {
                val belowMove = Move(move.row + 1, move.col)
                if (board.getCell(belowMove.row, belowMove.col) == player) {
                    return move
                }
            }
        }

        //Verificando diagonais
        for (move in availableMoves) {
            if (move.row > 0 && move.col > 0) {
                val diagonalMove = Move(move.row - 1, move.col - 1)
                if (board.getCell(diagonalMove.row, diagonalMove.col) == player) {
                    return move
                }
            }
            if (move.row < board.size - 1 && move.col < board.size - 1) {
                val diagonalMove = Move(move.row + 1, move.col + 1)
                if (board.getCell(diagonalMove.row, diagonalMove.col) == player) {
                    return move
                }
            }
        }

        return null
    }


    private fun isValidMove(move: Move, board: BoardState): Boolean {
        return move.row in 0 until board.size && move.col in 0 until board.size
    }

}

enum class GameStatus() {
    IN_PROGRESS,
    PLAYER_X_WON,
    PLAYER_O_WON,
    DRAW
}
