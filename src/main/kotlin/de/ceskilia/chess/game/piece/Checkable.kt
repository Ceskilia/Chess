package de.ceskilia.chess.game.piece

interface Checkable : ChessPiece {

    fun isChecked(): Boolean {
        return board.opponentPieces(color)
            .filter { it::class != this::class }
            .any {
                it.canMoveTo(this.position)
            }
    }

}