package de.ceskilia.chess.piece.annotation

/**
 * Used to indicate classes, moreover [chess pieces][de.ceskilia.chess.piece.ChessPiece],
 * that can be switched with a [king][de.ceskilia.chess.piece.standardtype.AbstractKing].
 */
@Target(AnnotationTarget.CLASS)
annotation class Interchangeable