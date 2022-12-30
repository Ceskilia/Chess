package de.ceskilia.chess.game.windetection

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.piece.ChessPiece

class WinDetection(val game: ChessGame) {

    fun isWon(): ChessPiece.Color? {
        val pieces = game.chessboard.getPieces()

        for (color in ChessPiece.Color.values()) {
            val defendingPieces = pieces.filter { it.color == color }
            val king = defendingPieces.first { it.type == ChessPiece.Type.KING }
            val checkingPieces = pieces
                .filter { it.color != color }
                .filter { it.canMoveTo(king.position) }

            // check if there is a check at all
            if (checkingPieces.isEmpty()) {
                continue
            }

            // check if he can step aside
            if (king.hasMoves()) {
                continue
            }

            // check if piece can move to protect him / take the checker
            // check if moves of every piece is in the checker possible moves directed to the king (block) or position (take)

            if (checkingPieces.size == 1) {
                //logic for blocking / take
            }

            // you cannot defend when two pieces give a check
            return checkingPieces.first().color
        }

        return null
    }

    fun checkDraw(): DrawType? {
        val pieces = game.chessboard.getPieces()

        // stalemate
        for (color in ChessPiece.Color.values()) {
            if (pieces.filter { it.color == color }.none { it.hasMoves() }) {
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

        val chessboard = game.chessboard

        // there need to be at least 9 elements for threefold repetition/50 moves draw
        if(chessboard.moves.size < 9) {
            return null
        }

        // threefold repetition
        // take last 9 elements (4 from the one side | 5 from the other ("ababa" = 3fold))
        val repetitionMoves = chessboard.moves
            .takeLast(9)
            .groupBy { it.chessPiece.color }
            .values
            .distinct()

        // when there are only 2 distinct moves, they are going back and forth
        if (repetitionMoves.all { it.size == 2 }) {
            return DrawType.THREEFOLD_REPETITION
        }

        // there need to be at least 50 elements for 50 moves draw
        if(chessboard.moves.size < 50) {
            return null
        }

        // 50 moves
        // take last 50 elements
        val lastFiftyMoves = chessboard.moves.takeLast(50)

        // no pawn move and no capture
        if (lastFiftyMoves.none { it.chessPiece.type == ChessPiece.Type.PAWN }
            && lastFiftyMoves.none { it.type.isCapture() }) {
            return DrawType.FIFTY_MOVES
        }

        return null
    }

}