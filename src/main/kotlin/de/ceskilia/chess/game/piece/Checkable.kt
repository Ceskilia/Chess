package de.ceskilia.chess.game.piece

interface Checkable : ChessPiece {

    fun isChecked(): Boolean {
        return board.opponentPieces(color).any {
            it.canMoveTo(this.position)
        }
    }

}