package com.ifce.ppd.tsoroyematatu;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Starts the server.
 */
public class Server implements RMIInterface {
    private final Set<Room> rooms = new HashSet<>();
    private final Set<Player> players = new HashSet<>();

    public static void main(String[] args) {
        try {
            Server obj = new Server();
            RMIInterface stub = (RMIInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(2002);
            registry.rebind("RMIInterface", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }

    /**
     * If a room with given roomId doesn't exist, create a new one. If it does, return it.
     *
     * @param roomId The room's id
     * @return The created room or the one with given roomId
     */
    public Room createRoom(String roomId) {
        // If room already exist, return it
        for (Room room : rooms)
            if (room.getId().equals(roomId))
                return room;

        // Create a new room if it doesn't exist
        Room room = new Room(roomId);
        rooms.add(room);
        System.out.println("Sala " + room.getId() + " foi criada.");
        return room;
    }

    private Player getPlayerByClient(Client client) {
        for (Player player : players)
            if (player.getClient() == client)
                return player;
        return null;
    }

    @Override
    public MESSAGE_TYPES createClient(Client client, String roomId, ClientCallback clientCallback) throws RemoteException {
        System.out.println("Cliente " + client.getName() + " inicializado no servidor");
        Room room = createRoom(roomId); // the reference of the room of the player
        Player newPlayer = new Player(this, client, room, clientCallback);
        players.add(newPlayer);

        try {
            room.addPlayer(newPlayer); // add player to the room
        } catch (MaximumNumberPlayersInTheRoomException e) {
            System.out.println("Sala " + room.getId() + " está lotada!");
            newPlayer.setRoom(null); // remove the reference of room from the player
            return MESSAGE_TYPES.ROOM_IS_FULL;
        }

        System.out.println("Cliente " + client.getName() + " entrou na sala " + room.getId());

        // If room isn't full, player should wait until another player enter.
        // If full, create game and send playable flag.
        if (!room.isFull()) {
            newPlayer.setFirstPlayer(true);
            return MESSAGE_TYPES.WAIT_RIVAL_CONNECT;
        } else {
            room.createGame();
            try {
                room.getRival(newPlayer).sendPlayable();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MESSAGE_TYPES.PLAYABLE;
        }
    }

    @Override
    public void messageToRival(String message, Client client) throws RemoteException {
        Player player = getPlayerByClient(client);
        if (player == null) return;
        player.getRivalClientCallback().receiveMessage(client.getName(), message);
    }

    @Override
    public void move(String pieceId, String pointId, Client client) throws RemoteException {
        Player player = getPlayerByClient(client);
        if (player == null) return;
        Room room = player.getRoom();

        // If the move is valid, send move
        if (room.getGame().isValidMove(pieceId, pointId)) {
            room.sendMoveToPlayers(pieceId, pointId);
            player.getClientCallback().waitRivalMakeMove();
            room.getRivalPlayer(player).getClientCallback().canMakeMove();
        }
    }

    @Override
    public void exit(Client client) throws RemoteException {
        Player player = getPlayerByClient(client);
        if (player == null) return;
        Room room = player.getRoom();
        if (!room.isFull()) {
            player.getClientCallback().exit();
            room.removePlayer(player);
        } else {
            player.getClientCallback().exit();
            player.getRivalClientCallback().exit();
            room.removePlayer(player);
            room.removePlayer(room.getRivalPlayer(player));
        }
    }

    @Override
    public void withdrawal(Client client) throws RemoteException {
        Player player = getPlayerByClient(client);
        if (player == null) return;
        Room room = player.getRoom();
        player.getClientCallback().loser();
        player.getRivalClientCallback().winner();
        room.resetGame();
    }

    @Override
    public void draw(Client client) throws RemoteException {
        Player player = getPlayerByClient(client);
        if (player == null) return;
        player.getRivalClientCallback().drawConfirmation();
    }

    @Override
    public void drawDenied(Client client) throws RemoteException {
        Player player = getPlayerByClient(client);
        if (player == null) return;
        player.getRivalClientCallback().drawDenied();
    }

    @Override
    public void drawAccepted(Client client) throws RemoteException {
        Player player = getPlayerByClient(client);
        if (player == null) return;
        player.getRoom().sendDraw();
    }
}

