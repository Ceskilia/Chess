package de.ceskilia.chess.game

data class Move(
    val chessPiece: ChessPiece,
    val newPosition: Position,
    val type: Type
) {

    companion object {

        const val CAPTURE_NOTATION = 'x'
        const val CHECK_NOTATION = '+'

    }

    override fun toString(): String {
        // (if PAWN: use column/y, if same pieces can make same move insert ???)
        return chessPiece.type.notation.toString() +
                (if (type.isCapture()) CAPTURE_NOTATION else "") +
                newPosition.toString() +
                (if (type.isCheck()) CHECK_NOTATION else "")
    }

    enum class Type {

        NO_PIECE_AT_POSITION,
        INVALID_END_POSITION,
        NORMAL,
        CAPTURE,
        CAPTURE_CHECK,
        CHECK;


        // TODO: IS THIS DESIGN GOOD?
        fun isCapture(): Boolean {
            return this == CAPTURE || this == CAPTURE_CHECK
        }

        fun isCheck(): Boolean {
            return this == CHECK || this == CAPTURE_CHECK
        }

    }

}