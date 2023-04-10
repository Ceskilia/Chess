package de.ceskilia.chess.piece

interface Checkable : ChessPiece {

    fun isChecked(): Boolean {
        return board.opponentPieces(color)
            .filter { it::class != this::class }
            .any {
                it.canMoveTo(this.position)
            }
    }

}