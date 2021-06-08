package com.example.chessboard_importer.models

private const val STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

class Board {
    var fen = STARTING_POSITION

    fun setPlayerTurn(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE-> {
                fen = fen.replace(" b ", " w ")
            }
            ChessPlayer.BLACK -> {
                fen = fen.replace(" w ", " b ")
            }
        }
    }
}
