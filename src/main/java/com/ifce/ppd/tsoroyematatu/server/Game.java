package com.ifce.ppd.tsoroyematatu.server;

import com.ifce.ppd.tsoroyematatu.models.Room;

/**
 * The board:
 *       0
 *    1  2  3
 *   4   5   6
 *
 * Winner cases:
 * 0 - 1 - 4
 * 0 - 2 - 5
 * 0 - 3 - 6
 * 1 - 2 - 3
 * 4 - 5 - 6
 *
 * The first part of the game is:
 * Player A set one piece is block to set another, then Player B set another one
 */
public class Game {
    private PointBoard[] board;
    private Room room;
    private int turn;

    public Game(Room room) {
        board = new PointBoard[]{new PointBoard(0), new PointBoard(1), new PointBoard(2), new PointBoard(3),
                new PointBoard(4), new PointBoard(5), new PointBoard(6)};
        this.room = room;
        turn = 0;
    }
}

