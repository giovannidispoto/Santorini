package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.WorkerPositionInterface;

public interface UIActions {
    //UI Actions implemented by CLI and GUI
    void pickCards(ClientController clientController) throws SantoriniException;
    void chooseCard(ClientController clientController);
    WorkerPositionInterface placeWorkers(ClientController clientController, int workerID);
    void selectWorker(ClientController clientController) throws SantoriniException;
    void moveWorker(ClientController clientController) throws SantoriniException;
    boolean askForSkip();
    boolean askForRepeat();
    boolean askForBasicTurn();
    void buildBlock(ClientController clientController) throws SantoriniException;
    void removeBlock(ClientController clientController) throws SantoriniException;
    void setupConnection(ClientController clientController) throws SantoriniException;
    void callError(String exceptionName);
    void callMatchResult(String result);

}
