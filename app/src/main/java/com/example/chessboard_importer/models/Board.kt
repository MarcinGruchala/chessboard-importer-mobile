package com.example.chessboard_importer.models



class Board {
    var fen = STARTING_POSITION

    fun setPlayerTurn(player: ChessPlayer) {
        fen = when (player) {
            ChessPlayer.WHITE-> {
                fen.replace(" b ", " w ")
            }
            ChessPlayer.BLACK -> {
                fen.replace(" w ", " b ")
            }
        }

    }

    companion object{
        const val STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    }
}
