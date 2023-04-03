package de.ceskilia.chess

import de.ceskilia.chess.game.impl.TextChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.result.MoveResult
import de.ceskilia.chess.game.result.Win
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class ChessGameTest {

    private val game = TextChessGame()

    @Test
    fun picks_wrong_color() {
        with(game) {
            board.setup()
            assertTrue(move(Position.fromNotation("d1"), Position.fromNotation("d2")) == MoveResult.WRONG_COLOR)
        }
    }

    @Test
    fun no_piece_at_position() {
        with(game) {
            board.setup()
            assertTrue(move(Position.fromNotation("e4"), Position.fromNotation("e5")) == MoveResult.INVALID_POSITION)
        }
    }

    @Test
    fun cannot_move_to_position() {
        with(game) {
            board.setup()
            assertTrue(move(Position.fromNotation("d8"), Position.fromNotation("d7")) == MoveResult.INVALID_MOVE)
        }
    }

    @Test
    fun is_checked() {
        with(game) {
            board.setup()
            assertAll(
                Executable { assertSuccess(move(Position.of(4, 6), Position.of(4, 4))) },
                Executable { assertSuccess(move(Position.of(5, 1), Position.of(5, 2))) },
                Executable { assertSuccess(move(Position.of(3, 7), Position.of(7, 3))) }
            )

            assertTrue(history.lastMove()!!.isCheck)
        }
    }

    @Test
    fun is_checkmated_range_white() {
        with(game) {
            board.setup()
            assertAll(
                Executable { assertSuccess(move(Position.fromNotation("f7"), Position.fromNotation("f6"))) },
                Executable { assertSuccess(move(Position.fromNotation("e2"), Position.fromNotation("e4"))) },
                Executable { assertSuccess(move(Position.fromNotation("g7"), Position.fromNotation("g5"))) },
                Executable { assertSuccess(move(Position.fromNotation("d1"), Position.fromNotation("h5"))) },
            )
            assertTrue(isFinished())
            assertTrue(result is Win)
            assertTrue((result as Win).value.pieceColor == ChessPiece.Color.BLACK)
        }
    }

    @Test
    fun is_checkmated_close_bishop_white() {
        with(game) {
            board.setup()
            assertAll(
                Executable { assertSuccess(move(Position.fromNotation("e7"), Position.fromNotation("e5"))) },
                Executable { assertSuccess(move(Position.fromNotation("e2"), Position.fromNotation("e4"))) },
                Executable { assertSuccess(move(Position.fromNotation("f8"), Position.fromNotation("c5"))) },
                Executable { assertSuccess(move(Position.fromNotation("a2"), Position.fromNotation("a3"))) },
                Executable { assertSuccess(move(Position.fromNotation("d8"), Position.fromNotation("f6"))) },
                Executable { assertSuccess(move(Position.fromNotation("a3"), Position.fromNotation("a4"))) },
                Executable { assertSuccess(move(Position.fromNotation("f6"), Position.fromNotation("f2"))) },
            )
            assertTrue(isFinished())
            assertTrue(result is Win)
            assertTrue((result as Win).value.pieceColor == ChessPiece.Color.WHITE)
        }
    }

    @Test
    fun is_checkmated_close_knight_white() {
        with(game) {
            board.setup()
            assertAll(
                Executable { assertSuccess(move(Position.fromNotation("e7"), Position.fromNotation("e5"))) },
                Executable { assertSuccess(move(Position.fromNotation("a2"), Position.fromNotation("a3"))) },
                Executable { assertSuccess(move(Position.fromNotation("g8"), Position.fromNotation("f6"))) },
                Executable { assertSuccess(move(Position.fromNotation("a3"), Position.fromNotation("a4"))) },
                Executable { assertSuccess(move(Position.fromNotation("f6"), Position.fromNotation("g4"))) },
                Executable { assertSuccess(move(Position.fromNotation("a4"), Position.fromNotation("a5"))) },
                Executable { assertSuccess(move(Position.fromNotation("d8"), Position.fromNotation("f6"))) },
                Executable { assertSuccess(move(Position.fromNotation("a5"), Position.fromNotation("a6"))) },
                Executable { assertSuccess(move(Position.fromNotation("f6"), Position.fromNotation("f2"))) },
            )
            assertTrue(isFinished())
            assertTrue(result is Win)
            assertTrue((result as Win).value.pieceColor == ChessPiece.Color.WHITE)
        }
    }

    @Test
    fun is_checkmated_close_pawn_white() {
        with(game) {
            board.setup()
            assertAll(
                Executable { assertSuccess(move(Position.fromNotation("e7"), Position.fromNotation("e5"))) },
                Executable { assertSuccess(move(Position.fromNotation("a2"), Position.fromNotation("a3"))) },
                Executable { assertSuccess(move(Position.fromNotation("g7"), Position.fromNotation("g5"))) },
                Executable { assertSuccess(move(Position.fromNotation("a3"), Position.fromNotation("a4"))) },
                Executable { assertSuccess(move(Position.fromNotation("g5"), Position.fromNotation("g4"))) },
                Executable { assertSuccess(move(Position.fromNotation("a4"), Position.fromNotation("a5"))) },
                Executable { assertSuccess(move(Position.fromNotation("g4"), Position.fromNotation("g3"))) },
                Executable { assertSuccess(move(Position.fromNotation("a5"), Position.fromNotation("a6"))) },
                Executable { assertSuccess(move(Position.fromNotation("d8"), Position.fromNotation("f6"))) },
                Executable { assertSuccess(move(Position.fromNotation("a6"), Position.fromNotation("b7"))) },
                Executable { assertSuccess(move(Position.fromNotation("f6"), Position.fromNotation("f2"))) },
            )
            assertTrue(isFinished())
            assertTrue(result is Win)
            assertTrue((result as Win).value.pieceColor == ChessPiece.Color.WHITE)
        }
    }
    private fun assertSuccess(result: MoveResult, messsage: String? = null) {
        assertTrue(result == MoveResult.SUCCESS, messsage)
    }

}