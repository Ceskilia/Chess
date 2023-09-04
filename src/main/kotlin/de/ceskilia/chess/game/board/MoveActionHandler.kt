package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece

internal class MoveActionHandler {

    private val queuedMoveActions = mutableMapOf<ChessPiece, MutableSet<Action>>()

    fun queueMoveAction(
        piece: ChessPiece,
        position: Position,
        result: Action.Result,
        action: (Position) -> Unit
    ) {
        queuedMoveActions.compute(piece) { _, value ->
            val actions = value ?: mutableSetOf()
            if (actions.none { it.position == position })
                actions.add(Action(position, result, action))
            return@compute actions
        }
    }

    fun evaluateQueuedAction(piece: ChessPiece, endPosition: Position): Action.Result {
        val actionMovingTo = queuedMoveActions[piece]
            ?.firstOrNull { it.position == endPosition }
        queuedMoveActions.remove(piece)

        if (actionMovingTo == null) {
            return Action.Result.DEFAULT
        }

        actionMovingTo.action.invoke(endPosition)
        return actionMovingTo.result
    }

}