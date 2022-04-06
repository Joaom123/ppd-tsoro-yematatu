package com.ifce.ppd.tsoroyematatu;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the room where two players play.
 */
public class Room {
    private final String id;
    private final Set<PlayerThread> playersThreads = new HashSet<>();
    private Set<Player> players = new HashSet<>();
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
        return playersThreads.size() == 2;
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
     * DEPRECATED
     * Add player to the room. If full thrown exception.
     * @param playerThread Player to be added.
     * @throws MaximumNumberPlayersInTheRoomException If room is full, throw exception.
     */
    public void addPlayer(PlayerThread playerThread) throws MaximumNumberPlayersInTheRoomException {
        if (isFull())
            throw new MaximumNumberPlayersInTheRoomException();
        playersThreads.add(playerThread);
    }

    /**
     * Add player to the room. If full thrown exception.
     * @param player Player to be added.
     * @throws MaximumNumberPlayersInTheRoomException If room is full, throw exception.
     */
    public void addPlayer(Player player) throws MaximumNumberPlayersInTheRoomException {
        if (isFull())
            throw new MaximumNumberPlayersInTheRoomException();
        players.add(player);
    }

    /**
     * @param playerThread A player.
     * @return The rival of given player.
     * @throws NoRivalException If there is no rival, thrown exception.
     */
    public PlayerThread getRivalPlayerThread(PlayerThread playerThread) throws NoRivalException {
        for (PlayerThread playerThread1 : playersThreads) {
            if (playerThread != playerThread1)
                return playerThread1;
        }
        throw new NoRivalException();
    }

    /**
     * Remove the given player of the room.
     * @param playerThread Player to be removed.
     */
    public void removePlayerThread(PlayerThread playerThread) {
        playersThreads.remove(playerThread);
    }

    /**
     * Send playable flag to players.
     */
    public void sendPlayable() throws IOException {
        for (PlayerThread playerThread : playersThreads)
            playerThread.sendPlayableFlag();
    }

    /**
     * Send move to players.
     * @param pieceId The piece's id.
     * @param pointId The point's id.
     */
    public void sendMoveToPlayers(String pieceId, String pointId) throws IOException {
        for (PlayerThread playerThread : playersThreads)
            playerThread.sendMoveToPlayer(pieceId, pointId);
    }

    /**
     * @return The first player.
     */
    public PlayerThread getFirstPlayer() {
        for (PlayerThread pt : playersThreads)
            if (pt.isFirstPlayer())
                return pt;
        return null;
    }

    /**
     * @return The second player.
     */
    public PlayerThread getSecondPlayer() {
        for (PlayerThread pt : playersThreads)
            if (!pt.isFirstPlayer())
                return pt;
        return null;
    }

    /**
     * Send winner to players and reset the game.
     * @param winnerPlayer The winner player.
     */
    public void sendWinner(PlayerThread winnerPlayer) {
        for (PlayerThread playerThread : playersThreads)
            playerThread.sendWinnerPlayerFlag(winnerPlayer);
        resetGame();
    }

    /**
     * Send draw to players and reset the game.
     */
    public void sendDraw() throws IOException {
        for (PlayerThread playerThread : playersThreads)
            playerThread.sendDrawFlag();
        resetGame();
    }

    /**
     * Reset the game and change playerOne to playerTwo and vice-versa.
     */
    public void resetGame() {
        PlayerThread pt1 = getFirstPlayer();
        PlayerThread pt2 = getSecondPlayer();
        pt1.setFirstPlayer(false);
        pt2.setFirstPlayer(true);
        createGame();
        sendResetFlagToPlayers();
    }

    /**
     * Send reset flag to players in the room.
     */
    public void sendResetFlagToPlayers() {
        for (PlayerThread playerThread : playersThreads)
            playerThread.sendResetFlag();
    }
}
