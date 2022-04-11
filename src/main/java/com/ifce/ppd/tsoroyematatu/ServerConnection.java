package com.ifce.ppd.tsoroyematatu;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Start the client, connects to a server.
 */
public class ServerConnection implements ClientCallback {
    private Controller currentController;
    private Client client;
    private String roomId;
    private String hostname;
    private RMIInterface rmiInterfaceStub;
    private boolean isConnected = false;
    private boolean isFirstPlayer = false;
    private int turn = 0;
    ClientCallback clientCallback;

    public ServerConnection() {}

    public boolean isConnected() {
        return isConnected;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client clientModel) {
        this.client = clientModel;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
        currentController.updateTurn();
    }

    public void setCurrentController(Controller currentController) {
        this.currentController = currentController;
    }

    public void startConnection() {
        try {
            clientCallback = this;
            UnicastRemoteObject.exportObject(clientCallback, 0);
            Registry registry = LocateRegistry.getRegistry(null, 2002);
            rmiInterfaceStub = (RMIInterface) registry.lookup("RMIInterface");

            isConnected = true;
        } catch (Exception e) {
            isConnected = false;
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

    public void createClientOnServer(String roomId) throws NullClientException, RemoteException {
        if (client == null) throw new NullClientException();

        MESSAGE_TYPES messageTypes = rmiInterfaceStub.createClient(getClient(), roomId, clientCallback);

        // The room is full and the game isn't possible
        if (messageTypes == MESSAGE_TYPES.ROOM_IS_FULL) {
            roomIsFull();
        }

        // Is the first player and must wait rival to connect
        if (messageTypes == MESSAGE_TYPES.WAIT_RIVAL_CONNECT) {
            setFirstPlayer(true);
        }

        // Isn't the first player and the game is playable
        if (messageTypes == MESSAGE_TYPES.PLAYABLE) {
            goToGame();
        }
    }

    /**
     * Use the receiveThread to get the move valitation, and pass it to the current controller.
     *
     * @param pieceId The piece's id
     * @param pointId The point's id
     */
    public void receiveMove(String pieceId, String pointId) {
        currentController.receiveMove(pieceId, pointId);
    }

    /**
     * Execute goToGame in controller.
     */
    public void goToGame() {
        currentController.goToGame();
    }

    /**
     * Send message to server.
     * @param inputText The content of the message.
     */
    public void sendMessage(String inputText) {
        try {
            rmiInterfaceStub.messageToRival(inputText, client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use the sendThread to send the move to the server.
     * @param pieceId The pieceId
     * @param pointId The pointId
     */
    public void sendMove(String pieceId, String pointId) {
        try {
            rmiInterfaceStub.move(pieceId, pointId, client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call exit function on server.
     */
    public void sendExit() {
        try {
            rmiInterfaceStub.exit(client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call withdrawal function on server.
     */
    public void sendWithdrawalFlag() {
        try {
            rmiInterfaceStub.withdrawal(client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call draw function on server.
     */
    public void sendDrawFlag() {
        try {
            rmiInterfaceStub.draw(client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call draw denied function on server.
     */
    public void sendDrawDeniedFlag() {
        try {
            rmiInterfaceStub.drawDenied(client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call draw accepted on server.
     */
    public void sendDrawAcceptedFlag() {
        try {
            rmiInterfaceStub.drawAccepted(client);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawAccepted() throws RemoteException {
        currentController.drawAccepted();
    }

    @Override
    public void drawDenied() throws RemoteException {
        currentController.drawDenied();
    }

    @Override
    public void exit() throws RemoteException {
        currentController.exit();
    }

    @Override
    public void sendPlayable() throws RemoteException {
        goToGame();
    }

    @Override
    public void roomIsFull() throws RemoteException {
        currentController.roomIsFull();
    }

    @Override
    public void move(String pieceId, String pointId, int turn) throws RemoteException {
        setTurn(turn);
        receiveMove(pieceId, pointId);
    }

    @Override
    public void drawConfirmation() {
        currentController.drawConfirmation();
    }

    @Override
    public void resetGame() {
        turn = 0;
        isFirstPlayer = !isFirstPlayer;
        currentController.resetGame();
    }

    @Override
    public void waitRivalMakeMove() {
        currentController.waitRivalMakeMove();
    }

    @Override
    public void canMakeMove() {
        currentController.canMakeMove();
    }

    @Override
    public void winner() {
        System.out.println("Venceu");
        currentController.winner();
    }

    @Override
    public void loser() {
        System.out.println("Perdeu");
        currentController.loser();
    }

    @Override
    public void receiveMessage(String author, String message) {
        currentController.addMessageToChat(author, message);
    }
}
