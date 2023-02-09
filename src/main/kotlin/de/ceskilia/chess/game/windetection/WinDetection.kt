package de.ceskilia.chess.game.windetection

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.player.Player

class WinDetection(val game: ChessGame) {

    fun isWon(): Player? {

        game.players.forEach {
            it.getOpponents().forEach { opponent ->
                if(opponent.isCheckmating(it)) {
                    return opponent
                }
            }
        }

        return null
    }

    fun checkDraw(): DrawType? {
        val pieces = game.board.getPieces()

        // stalemate
        for (player in game.players) {
            if (player.isStalemated()) {
                return DrawType.STALEMATE
            }
        }

        // insufficient material
        // king vs king
        // king and bishop vs king
        // king and knight vs king
        // king and bishop vs king and bishop (bishop on the same square color)
        when (pieces.size) {
            2 -> return DrawType.INSUFFICIENT_MATERIAL // 2 kings
            3 -> run {
                // 2 kings and 1 other piece
                if (pieces.none { it.type == ChessPiece.Type.BISHOP || it.type == ChessPiece.Type.KNIGHT }) {
                    return@run
                }

                return DrawType.INSUFFICIENT_MATERIAL
            }

            4 -> run {

                // filter only bishops
                val bishops = pieces.filter { it.type != ChessPiece.Type.KING }
                    .filter { it.type == ChessPiece.Type.BISHOP }

                // the last remaining pieces must be bishops
                if (bishops.size != 2) {
                    return@run
                }

                val (first, second) = bishops

                // the bishops need to have a different color and the square color must be the same
                if (first.color == second.color || first.position.color() != second.position.color()) {
                    return@run
                }

                return DrawType.INSUFFICIENT_MATERIAL
            }
        }

        val chessboard = game.board

        // there need to be at least 9 elements for threefold repetition/50 moves draw
        if (chessboard.history.moves.size < 9) {
            return null
        }

        // threefold repetition
        // take last 9 elements (4 from the one side | 5 from the other ("ababa" = 3fold)) TODO ("abcabcabc" is also 3fold)
        val repetitionMoves = chessboard.history.lastMoves(9)
            .groupBy { it.chessPiece.color }
            .values
            .distinct()

        // when there are only 2 distinct moves, they are going back and forth
        if (repetitionMoves.all { it.size == 2 }) {
            return DrawType.THREEFOLD_REPETITION
        }

        // there need to be at least 50 elements for 50 moves draw
        if (chessboard.history.moves.size < 50) {
            return null
        }

        // 50 moves
        // take last 50 elements
        val lastFiftyMoves = chessboard.history.lastMoves(50)

        // no pawn move and no capture
        if (lastFiftyMoves.none { it.chessPiece.type == ChessPiece.Type.PAWN }
            && lastFiftyMoves.none { it.isCapture }) {
            return DrawType.FIFTY_MOVES
        }

        return null
    }

}