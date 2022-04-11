package com.ifce.ppd.tsoroyematatu;

import java.rmi.RemoteException;

/**
 * This class represent the game's logic.
 */
public class Game {
    private final Room room;
    private final PointBoard[] board;
    private final PieceBoard[] firstPlayerPieces;
    private final PieceBoard[] secondPlayerPieces;
    private int turn = 0;
    private boolean isFinished = false;

    public Game(Room room) {
        board = new PointBoard[]{
                new PointBoard("point-0"),
                new PointBoard("point-1"), new PointBoard("point-2"), new PointBoard("point-3"),
                new PointBoard("point-4"), new PointBoard("point-5"), new PointBoard("point-6")
        };
        this.room = room;
        firstPlayerPieces = new PieceBoard[]{
                new PieceBoard("piece-first-1", room.getFirstPlayer()),
                new PieceBoard("piece-first-2", room.getFirstPlayer()),
                new PieceBoard("piece-first-3", room.getFirstPlayer())
        };
        secondPlayerPieces = new PieceBoard[]{
                new PieceBoard("piece-second-1", room.getSecondPlayer()),
                new PieceBoard("piece-second-2", room.getSecondPlayer()),
                new PieceBoard("piece-second-3", room.getSecondPlayer())
        };
    }

    /**
     * Add one to turn number.
     */
    public void addTurn() {
        turn += 1;
    }

    /**
     * Turn's getter.
     *
     * @return The turn's number.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Check if the move is valid. If it is,execute the move.
     *
     * @param pieceId The pieceId
     * @param pointId The pointId
     * @return True if the move is valid. False otherwise.
     */
    public boolean isValidMove(String pieceId, String pointId) throws RemoteException {
        Player player = getOwnerOfPiece(pieceId);
        PointBoard pointBoard = getPointBoardById(pointId);

        if (pointBoard == null) return false; // If point not found, the move is invalid
        if (player == null) return false; // If player not found, the move is invalid

        PieceBoard pieceBoard = getPieceBoard(player, pieceId);

        if (!canMove(pointBoard, pieceBoard)) return false;

        doMove(pointBoard, pieceBoard);
        return true;
    }

    /**
     * Verify if the move can be done, following the given rules.
     *
     * @param pointBoardDest The destination point
     * @param pieceBoard     The PieceBoard.
     * @return True if the move can be done. False otherwise.
     */
    private boolean canMove(PointBoard pointBoardDest, PieceBoard pieceBoard) {
        if (!isSecondPhase()) return true;
        if (isFinished) return false;

        PointBoard pointBoardOrigin = getOccupiedPointBoard(pieceBoard);

        if (pointBoardOrigin == null) return false;

        String originId = pointBoardOrigin.getId();
        String destinyId = pointBoardDest.getId();

        // 1 <-/-> 5
        if (originId.equals("point-1") && destinyId.equals("point-5"))
            return false;
        if (originId.equals("point-5") && destinyId.equals("point-1"))
            return false;
        // 1 <-/-> 6
        if (originId.equals("point-1") && destinyId.equals("point-6"))
            return false;
        if (originId.equals("point-6") && destinyId.equals("point-1"))
            return false;
        // 3 <-/-> 4
        if (originId.equals("point-3") && destinyId.equals("point-4"))
            return false;
        if (originId.equals("point-4") && destinyId.equals("point-3"))
            return false;
        // 3 <-/-> 5
        if (originId.equals("point-3") && destinyId.equals("point-5"))
            return false;
        if (originId.equals("point-5") && destinyId.equals("point-3"))
            return false;
        // 2 <-/-> 4
        if (originId.equals("point-2") && destinyId.equals("point-4"))
            return false;
        if (originId.equals("point-4") && destinyId.equals("point-2"))
            return false;
        // 2 <-/-> 6
        if (originId.equals("point-2") && destinyId.equals("point-6"))
            return false;
        if (originId.equals("point-6") && destinyId.equals("point-2"))
            return false;

        // 0 <-> 4, 1 must be occupied
        if (originId.equals("point-0") && destinyId.equals("point-4") && !getPointBoardById("point-1").isOccupied())
            return false;
        if (originId.equals("point-4") && destinyId.equals("point-0") && !getPointBoardById("point-1").isOccupied())
            return false;
        // 0 <-> 6, 3 must be occupied
        if (originId.equals("point-0") && destinyId.equals("point-6") && !getPointBoardById("point-3").isOccupied())
            return false;
        if (originId.equals("point-6") && destinyId.equals("point-0") && !getPointBoardById("point-3").isOccupied())
            return false;
        // 0 <-> 5, 2 must be occupied
        if (originId.equals("point-0") && destinyId.equals("point-5") && !getPointBoardById("point-2").isOccupied())
            return false;
        if (originId.equals("point-5") && destinyId.equals("point-0") && !getPointBoardById("point-2").isOccupied())
            return false;
        // 1 <-> 3, 2 must be occupied
        if (originId.equals("point-1") && destinyId.equals("point-3") && !getPointBoardById("point-2").isOccupied())
            return false;
        if (originId.equals("point-3") && destinyId.equals("point-1") && !getPointBoardById("point-2").isOccupied())
            return false;
        // 4 <-> 6, 5 must be occupied
        if (originId.equals("point-4") && destinyId.equals("point-6") && !getPointBoardById("point-5").isOccupied())
            return false;
        return !originId.equals("point-6") || !destinyId.equals("point-4") || getPointBoardById("point-5").isOccupied();
    }

    /**
     * Execute the move.
     *
     * @param pointBoard The point board.
     * @param pieceBoard The piece board.
     */
    private void doMove(PointBoard pointBoard, PieceBoard pieceBoard) throws RemoteException {
        addTurn();

        PointBoard pointBoardOrigin = getOccupiedPointBoard(pieceBoard);

        if (pointBoardOrigin != null)
            pointBoardOrigin.setPieceBoard(null);

        pointBoard.setPieceBoard(pieceBoard);

        if (isSecondPhase()) checkWinnerSituation();
    }

    /**
     * @param pieceId The piece's id.
     * @return The player owner of the piece.
     */
    private Player getOwnerOfPiece(String pieceId) {
        if (pieceId.contains("first")) return room.getFirstPlayer(); // The move is from the first player
        if (pieceId.contains("second")) return room.getSecondPlayer(); // The move is from the second player
        return null;
    }

    /**
     * @param pointId The point's id.
     * @return The PointBoard representation of the point.
     */
    private PointBoard getPointBoardById(String pointId) {
        for (PointBoard pb : board)
            if (pb.getId().equals(pointId))
                return pb;
        return null;
    }

    /**
     * @param player The player owner of the piece.
     * @param pieceBoardId The piece's id.
     * @return The PieceBoard representation of the piece.
     */
    private PieceBoard getPieceBoard(Player player, String pieceBoardId) {
        if (player.isFirstPlayer())
            for (PieceBoard pb : firstPlayerPieces)
                if (pb.getId().equals(pieceBoardId))
                    return pb;

        if (!player.isFirstPlayer())
            for (PieceBoard pb : secondPlayerPieces)
                if (pb.getId().equals(pieceBoardId))
                    return pb;

        return null;
    }

    /**
     * Check if the game is in the second phase.
     *
     * @return If turn > 6, then it is the second phase of the game.
     */
    private boolean isSecondPhase() {
        return turn >= 6;
    }

    /**
     * Winner cases are: 0-1-4 | 0-2-5 | 0-3-6 | 1-2-3 | 4-5-6.
     *
     * @return The winner player. If no player won, return null.
     */
    private Player getWinnerPlayer() {
        Player pt0 = getPlayerWhoOccupiesPoint(getPointBoardById("point-0"));
        Player pt1 = getPlayerWhoOccupiesPoint(getPointBoardById("point-1"));
        Player pt2 = getPlayerWhoOccupiesPoint(getPointBoardById("point-2"));
        Player pt3 = getPlayerWhoOccupiesPoint(getPointBoardById("point-3"));
        Player pt4 = getPlayerWhoOccupiesPoint(getPointBoardById("point-4"));
        Player pt5 = getPlayerWhoOccupiesPoint(getPointBoardById("point-5"));
        Player pt6 = getPlayerWhoOccupiesPoint(getPointBoardById("point-6"));

        //0-1-4
        if (pt0 == room.getFirstPlayer() && pt1 == room.getFirstPlayer() && pt4 == room.getFirstPlayer())
            return room.getFirstPlayer();
        if (pt0 == room.getSecondPlayer() && pt1 == room.getSecondPlayer() && pt4 == room.getSecondPlayer())
            return room.getSecondPlayer();
        //0-2-5
        if (pt0 == room.getFirstPlayer() && pt2 == room.getFirstPlayer() && pt5 == room.getFirstPlayer())
            return room.getFirstPlayer();
        if (pt0 == room.getSecondPlayer() && pt2 == room.getSecondPlayer() && pt5 == room.getSecondPlayer())
            return room.getSecondPlayer();
        //0-3-6
        if (pt0 == room.getFirstPlayer() && pt3 == room.getFirstPlayer() && pt6 == room.getFirstPlayer())
            return room.getFirstPlayer();
        if (pt0 == room.getSecondPlayer() && pt3 == room.getSecondPlayer() && pt6 == room.getSecondPlayer())
            return room.getSecondPlayer();
        //1-2-3
        if (pt1 == room.getFirstPlayer() && pt2 == room.getFirstPlayer() && pt3 == room.getFirstPlayer())
            return room.getFirstPlayer();
        if (pt1 == room.getSecondPlayer() && pt2 == room.getSecondPlayer() && pt3 == room.getSecondPlayer())
            return room.getSecondPlayer();
        //4-5-6
        if (pt4 == room.getFirstPlayer() && pt5 == room.getFirstPlayer() && pt6 == room.getFirstPlayer())
            return room.getFirstPlayer();
        if (pt4 == room.getSecondPlayer() && pt5 == room.getSecondPlayer() && pt6 == room.getSecondPlayer())
            return room.getSecondPlayer();

        return null;
    }

    /**
     * @param pointBoard The pointBoard.
     * @return The player who occupies the given point.
     */
    private Player getPlayerWhoOccupiesPoint(PointBoard pointBoard) {
        if (pointBoard == null) return null;
        if (pointBoard.getPieceBoard() == null) return null;
        return pointBoard.getPieceBoard().getOwnerPlayer();
    }

    /**
     * Check if there is any winner. If so, call server to send winner/loser.
     */
    private void checkWinnerSituation() throws RemoteException {
        Player winnerPlayer = getWinnerPlayer();

        if (winnerPlayer == null) return;

        isFinished = true;
        room.sendWinner(winnerPlayer);
    }

    /**
     * @param pieceBoard The pieceBoard.
     * @return The PointBoard occupied by given pieceBoard.
     */
    private PointBoard getOccupiedPointBoard(PieceBoard pieceBoard) {
        for (PointBoard pb : board)
            if (pb.getPieceBoard() == pieceBoard)
                return pb;
        return null;
    }
}

