package com.gfbdev.jogoDaVelha.domain.usecase

import com.gfbdev.jogoDaVelha.presentation.viewModel.BoardState
import com.gfbdev.jogoDaVelha.presentation.viewModel.Move
import kotlin.random.Random

class GetCpuMoveUseCase {
    operator fun invoke(board: BoardState): Move {
        val winningMove = findWinningMove(board)
        if (winningMove != null) return winningMove

        val smartMove = makeSmartMove(board)
        if (smartMove != null) return smartMove

        val availableMoves = board.getAvailableMoves()
        return if (availableMoves.isNotEmpty()) {
            availableMoves[Random.nextInt(availableMoves.size)]
        } else {
            Move(-1, -1)
        }
    }

    private fun findWinningMove(board: BoardState): Move? {
        val availableMoves = board.getAvailableMoves()
        for (move in availableMoves) {
            val testBoardO = board.copy()
            testBoardO.setCell(move.row, move.col, "O")
            if (testBoardO.checkWin("O")) return move
        }
        for (move in availableMoves) {
            val testBoardX = board.copy()
            testBoardX.setCell(move.row, move.col, "X")
            if (testBoardX.checkWin("X")) return move
        }
        return null
    }

    private fun makeSmartMove(board: BoardState): Move? {
        val player = "O"
        val availableMoves = board.getAvailableMoves()

        // Prioriza o centro se disponível (clássico do jogo da velha)
        val center = board.size / 2
        if (board.getCell(center, center).isEmpty()) {
            return Move(center, center)
        }

        // Tenta jogar adjacente a uma peça já existente do robô
        for (move in availableMoves) {
            // Laterais
            if (move.col > 0) {
                val leftMove = Move(move.row, move.col - 1)
                if (board.getCell(leftMove.row, leftMove.col) == player) return move
            }
            if (move.col < board.size - 1) {
                val rightMove = Move(move.row, move.col + 1)
                if (board.getCell(rightMove.row, rightMove.col) == player) return move
            }
            // Vertical
            if (move.row > 0) {
                val aboveMove = Move(move.row - 1, move.col)
                if (board.getCell(aboveMove.row, aboveMove.col) == player) return move
            }
            if (move.row < board.size - 1) {
                val belowMove = Move(move.row + 1, move.col)
                if (board.getCell(belowMove.row, belowMove.col) == player) return move
            }
            // Diagonais
            if (move.row > 0 && move.col > 0) {
                val diagUpLeft = Move(move.row - 1, move.col - 1)
                if (board.getCell(diagUpLeft.row, diagUpLeft.col) == player) return move
            }
            if (move.row < board.size - 1 && move.col < board.size - 1) {
                val diagDownRight = Move(move.row + 1, move.col + 1)
                if (board.getCell(diagDownRight.row, diagDownRight.col) == player) return move
            }
        }

        return null
    }
}
