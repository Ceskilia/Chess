package de.ceskilia.chess.piece.annotation

/**
 * Used to attribute a constant value to a class.
 *
 * @property value the attributed value
 */
@Target(AnnotationTarget.CLASS)
annotation class Valuable(val value: Int)