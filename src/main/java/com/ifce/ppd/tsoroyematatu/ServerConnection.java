package com.ifce.ppd.tsoroyematatu;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Start the client, connects to a server.
 */
public class ServerConnection implements ClientCallback{
    private Controller currentController;
    private Client clientModel;
    private String roomId;
    private String hostname;
    private RMIInterface rmiInterfaceStub;
    private SendThread sendThread;
    private boolean isConnected = false;
    private boolean isFirstPlayer = false;
    private int turn = 0;

    public ServerConnection() {

    }

    public boolean isConnected() {
        return isConnected;
    }

    public Client getClient() {
        return clientModel;
    }

    public void setClient(Client clientModel) {
        this.clientModel = clientModel;
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
            ClientCallback client = (ClientCallback) this;

            UnicastRemoteObject.exportObject(client, 0);
            Registry registry = LocateRegistry.getRegistry(null, 2002);
            rmiInterfaceStub = (RMIInterface) registry.lookup("RMIInterface");
            rmiInterfaceStub.register(client);
            isConnected = true;
        } catch (Exception e) {
            isConnected = false;
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void createClientOnServer(String roomId) throws NullClientException, RemoteException {
        if (clientModel == null) throw new NullClientException();

        MESSAGE_TYPES messageTypes = rmiInterfaceStub.createClient(getClient(), roomId);

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

    @Override
    public void ping() throws RemoteException {
        System.out.println("Function called by the server");
    }

    /**
     * Send message to server.
     *
     * @param inputText The content of the message.
     */
    public void sendMessage(String inputText) {
        try {
            rmiInterfaceStub.receiveMessageFromClient(inputText);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        sendThread.sendMessage(inputText); //DEPRECATED
    }

    /**
     * Used by the ReceiveThread to get the message from the server and send it to the chat.
     *
     * @param author  The message's author.
     * @param message The content of the message.
     */
    public void receiveMessage(String author, String message) {
        currentController.addMessageToChat(author, message);
    }

    /**
     * Use the sendThread to send the move to the server.
     *
     * @param pieceId The pieceId
     * @param pointId The pointId
     */
    public void sendMove(String pieceId, String pointId) {
        sendThread.sendMove(pieceId, pointId);
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
     * Send exit flag.
     */
    public void sendExit() {
        sendThread.sendExit();
    }

    /**
     * Execute waitRivalMakeMove in controller.
     */
    public void waitRivalMakeMove() {
        currentController.waitRivalMakeMove();
    }

    /**
     * Execute canMakeMove in controller.
     */
    public void canMakeMove() {
        currentController.canMakeMove();
    }

    /**
     * Execute winner fuction in controller.
     */
    public void winner() {
        System.out.println("Venceu");
        currentController.winner();
    }

    /**
     * Execute loser funtion in controller.
     */
    public void loser() {
        System.out.println("Perdeu");
        currentController.loser();
    }

    /**
     * Send withdrawal flag.
     */
    public void sendWithdrawalFlag() {
        sendThread.sendWithdrawalFlag();
    }

    /**
     * Execute resetGame in controller. Set turn to 0. Change firstPlayer.
     */
    public void resetGame() {
        turn = 0;
        isFirstPlayer = !isFirstPlayer;
        currentController.resetGame();
    }

    /**
     * Send draw flag.
     */
    public void sendDrawFlag() {
        sendThread.sendDrawFlag();
    }

    /**
     * Execute drawConfirmation in controller.
     */
    public void drawConfirmation() {
        currentController.drawConfirmation();
    }

    /**
     * Send draw denied flag.
     */
    public void sendDrawDeniedFlag() {
        sendThread.sendDrawDeniedFlag();
    }

    /**
     * Send draw accepted flag.
     */
    public void sendDrawAcceptedFlag() {
        sendThread.sendDrawAcceptedFlag();
    }

    /**
     * Execute drawAccpeted in controller.
     */
    public void drawAccepted() {
        currentController.drawAccepted();
    }

    /**
     * Execute drawDenied in controller.
     */
    public void drawDenied() {
        currentController.drawDenied();
    }

    /**
     * Execute exit in controller.
     */
    public void exit() {
        // destroy connection
        currentController.exit();
    }

    /**
     * Execute roomIsFull in controller.
     */
    public void roomIsFull() {
        currentController.roomIsFull();
    }
}
