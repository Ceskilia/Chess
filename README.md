# ♟ Chess
This project features the complete game of chess, currently based on console text output.

![Example chess game](/img/img.png)

## 📖 About
This application provides a range of features, from a timer, the detection of different draw types, promotion, special moves, up to a game history and FEN export.

The main focus in developing this project is being able to implement new pieces, game modes or UIs easily.

## ℹ️ Overview

### Input
Specify a move in this format: `<from-position>-<to-position>`, for example `a7a6` or `b8c6`.
All valid moves for all pieces are listed in the console.

> You have to try again, if an input or a move is illegal.

### Features
The following features are implemented:

- only legal moves as by the chess ruleset
- promotion
- castling
- different draw types
- timer (+ preparation time for the first move)
- game history

### Custom implementations
It is easy to create new implementations for a chess game. 

For a new piece, extend the [ChessPiece](/src/main/kotlin/de/ceskilia/chess/piece/ChessPiece.kt) or [DefaultChessPiece](/src/main/kotlin/de/ceskilia/chess/piece/impl/DefaultChessPiece.kt) base class and define its moveset.
Different interfaces and annotations exist, helping to define and handle common logic:
- [Blockable](/src/main/kotlin/de/ceskilia/chess/piece/variation/Blockable.kt) (check can be blocked by other pieces)
- [Checkable](/src/main/kotlin/de/ceskilia/chess/piece/variation/Checkable.kt) (i.e. king)
- [Creatable](/src/main/kotlin/de/ceskilia/chess/piece/variation/Creatable.kt) (can be promoted to)
- [Promotable](/src/main/kotlin/de/ceskilia/chess/piece/variation/Promotable.kt) (can be converted to other pieces) 
- [@Valuable](/src/main/kotlin/de/ceskilia/chess/piece/annotation/Valuable.kt) (fixed piece value, for example 1 for pawn, 3 for bishop etc.)
- [@Interchangeable](/src/main/kotlin/de/ceskilia/chess/piece/annotation/Interchangeable.kt) (i.e. rooks in castling)

For a new game implementation, simply extend the [ChessGame](/src/main/kotlin/de/ceskilia/chess/game/ChessGame.kt) base class and define the game loop and the user input / overlay.

```kotlin
fun main() {
    val game = YourChessGameImpl()
    game.start()
}
```

## 🚀 Installation & Execution
To build the project with all dependencies, use:
```
./gradlew shadowJar
```

To run the application, either execute the jar directly or use the following console-command:
```
./gradlew run
```