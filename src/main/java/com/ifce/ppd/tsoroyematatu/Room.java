package com.ifce.ppd.tsoroyematatu;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the room where two players play.
 * Every Room has a Game, which contains the game's logic.
 */
public class Room {
    private final String id;
    private final Set<Player> players = new HashSet<>();
    private Game game;

    public Room(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public boolean isFull() {
        return players.size() == 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Create a new game.
     */
    public void createGame() {
        game = new Game(this);
    }

    /**
     * Add player to the room. If full thrown exception.
     *
     * @param player Player to be added.
     * @throws MaximumNumberPlayersInTheRoomException If room is full, throw exception.
     */
    public void addPlayer(Player player) throws MaximumNumberPlayersInTheRoomException {
        if (isFull())
            throw new MaximumNumberPlayersInTheRoomException();
        players.add(player);
    }

    public Player getRivalPlayer(Player player) {
        for (Player player1 : players)
            if (player != player1)
                return player1;
        return null;
    }

    /**
     * Remove the given player of the room.
     *
     * @param player Player to be removed.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Send move to players.
     *
     * @param pieceId The piece's id.
     * @param pointId The point's id.
     */
    public void sendMoveToPlayers(String pieceId, String pointId) throws RemoteException {
        for (Player player : players)
            player.getClientCallback().move(pieceId, pointId, game.getTurn());
    }

    /**
     * @return The first player.
     */
    public Player getFirstPlayer() {
        for (Player player : players)
            if (player.isFirstPlayer())
                return player;
        return null;
    }

    /**
     * @return The second player.
     */
    public Player getSecondPlayer() {
        for (Player player : players)
            if (!player.isFirstPlayer())
                return player;
        return null;
    }

    /**
     * Send winner to players and reset the game.
     *
     * @param winnerPlayer The winner player.
     */
    public void sendWinner(Player winnerPlayer) throws RemoteException {
        winnerPlayer.getClientCallback().winner();
        winnerPlayer.getRivalClientCallback().loser();
        resetGame();
    }

    /**
     * Send draw to players and reset the game.
     */
    public void sendDraw() throws RemoteException {
        for (Player player : players)
            player.getClientCallback().drawAccepted();
        resetGame();
    }

    /**
     * Reset the game and change playerOne to playerTwo and vice-versa.
     */
    public void resetGame() throws RemoteException {
        Player player1 = getFirstPlayer();
        Player player2 = getSecondPlayer();
        player1.setFirstPlayer(false);
        player2.setFirstPlayer(true);
        createGame();
        sendResetFlagToPlayers();
    }

    /**
     * Send reset flag to players in the room.
     */
    public void sendResetFlagToPlayers() throws RemoteException {
        for (Player player : players)
            player.getClientCallback().resetGame();
    }
}
