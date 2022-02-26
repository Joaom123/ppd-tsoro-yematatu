package com.ifce.ppd.tsoroyematatu.server;

import com.ifce.ppd.tsoroyematatu.models.Room;

/**
 * The board:
 * 0
 * 1  2  3
 * 4   5   6
 * <p>
 * Winner cases:
 * 0 - 1 - 4
 * 0 - 2 - 5
 * 0 - 3 - 6
 * 1 - 2 - 3
 * 4 - 5 - 6
 * <p>
 * The first part of the game is:
 * Player A set one piece is block to set another, then Player B set another one
 */
public class Game {
    private final PointBoard[] board;
    private final Room room;
    private int turn = 0;

    public Game(Room room) {
        board = new PointBoard[]{new PointBoard("point-0"), new PointBoard("point-1"), new PointBoard("point-2"), new PointBoard("point-3"),
                new PointBoard("point-4"), new PointBoard("point-5"), new PointBoard("point-6")};
        this.room = room;
    }

    public void addTurn() {
        turn += 1;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isValidMove(String pieceId, String pointId) {
        doMove(pieceId, pointId);
        return true;
    }

    private void doMove(String pieceId, String pointId) {
        addTurn();
    }
}

