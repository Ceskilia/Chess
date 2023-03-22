package de.ceskilia.chess

import de.ceskilia.chess.game.impl.TextChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.result.MoveResult
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class ChessBoardTest {

    private val game = TextChessGame()
    private val chessboard = game.board

    @Test
    fun can_take_huge_Step() {
        with(chessboard) {
            setup()
            assertNotNull(move(Position.of(0, 6), Position.of(0, 4)), "")
            assertNull(move(Position.of(0, 4), Position.of(0, 2)), "")
        }
    }

    @Test
    fun can_en_passant_white() {
        with(game) {
            setup()
            assertAll(
                Executable { assertSuccess(move(Position.of(0, 6), Position.of(0, 4))) },
                Executable { assertSuccess(move(Position.of(7, 1), Position.of(7, 2))) },
                Executable { assertSuccess(move(Position.of(0, 4), Position.of(0, 3))) },
                Executable { assertSuccess(move(Position.of(1, 1), Position.of(1, 3))) }
            )
            assertNotNull(move(Position.of(0, 3), Position.of(1, 2)), "")
            assertNull(board.pieceAt(Position.of(1, 3)), "")
        }
    }

    @Test
    fun can_en_passant_black() {
        with(game) {
            setup()
            assertAll(
                Executable { assertSuccess(move(Position.of(7, 6), Position.of(7, 5))) },
                Executable { assertSuccess(move(Position.of(0, 1), Position.of(0, 3))) },
                Executable { assertSuccess(move(Position.of(7, 5), Position.of(7, 4))) },
                Executable { assertSuccess(move(Position.of(0, 3), Position.of(0, 4))) },
                Executable { assertSuccess(move(Position.of(1, 6), Position.of(1, 4))) }
            )
            assertNotNull(move(Position.of(0, 4), Position.of(1, 5)))
            assertNull(board.pieceAt(Position.of(1, 4)), "")
        }
    }

    @Test
    fun is_pinned() {
        with(chessboard) {
            setup()
            assertAll(
                Executable { assertNotNull(move(Position.of(4, 6), Position.of(4, 4))) },
                Executable { assertNotNull(move(Position.of(3, 7), Position.of(7, 3))) },
            )
            assertTrue(pieceAt(5, 1)?.isPinned() == true)
        }
    }

    @Test
    fun is_pinned_but_can_take() {
        with(chessboard) {
            setup()
            assertAll(
                Executable { assertNotNull(move(Position.of(4, 6), Position.of(4, 4))) },
                Executable { assertNotNull(move(Position.of(3, 7), Position.of(7, 3))) },
                Executable { assertNotNull(move(Position.of(7, 3), Position.of(6, 2))) }
            )

            val piece = pieceAt(5, 1)

            assertNotNull(piece)
            assertTrue(piece!!.isPinned())
            assertTrue(piece.calculateMoves().first() == Position.of(6, 2))
        }
    }

    @Test
    fun can_castle_short_normal_move() {
        with(chessboard) {
            setup()
            assertAll(
                Executable { assertNotNull(move(Position.of(4, 6), Position.of(4, 4))) },
                Executable { assertNotNull(move(Position.of(0, 1), Position.of(0, 2))) },
                Executable { assertNotNull(move(Position.of(5, 7), Position.of(2, 4))) },
                Executable { assertNotNull(move(Position.of(0, 2), Position.of(0, 3))) },
                Executable { assertNotNull(move(Position.of(6, 7), Position.of(5, 5))) },
                Executable { assertNotNull(move(Position.of(0, 3), Position.of(0, 4))) }
            )

            assertNotNull(move(Position.of(4, 7), Position.of(6, 7)))
            assertNull(pieceAt(Position.of(7, 7)))
            assertNotNull(pieceAt(Position.of(5, 7)))
        }
    }

    @Test
    fun can_castle_short_rook_move() {
        with(chessboard) {
            setup()
            assertAll(
                Executable { assertNotNull(move(Position.of(4, 6), Position.of(4, 4))) },
                Executable { assertNotNull(move(Position.of(0, 1), Position.of(0, 2))) },
                Executable { assertNotNull(move(Position.of(5, 7), Position.of(2, 4))) },
                Executable { assertNotNull(move(Position.of(0, 2), Position.of(0, 3))) },
                Executable { assertNotNull(move(Position.of(6, 7), Position.of(5, 5))) },
                Executable { assertNotNull(move(Position.of(0, 3), Position.of(0, 4))) }
            )

            assertNotNull(move(Position.of(4, 7), Position.of(7, 7)))
            assertNull(pieceAt(Position.of(7, 7)))
            assertNotNull(pieceAt(Position.of(5, 7)))
        }
    }
    private fun assertSuccess(result: MoveResult?, messsage: String? = null) {
        assertTrue(result == MoveResult.SUCCESS, messsage)
    }

}