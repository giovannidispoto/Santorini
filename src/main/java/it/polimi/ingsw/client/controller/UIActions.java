package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.network.ClientSocketConnection;

public interface UIActions {
    //UI Actions implemented by CLI and GUI
    void pickCards(ClientController clientController);
    void chooseCard(ClientController clientController);
    void placeWorkers(ClientController clientController);
    void selectWorker(ClientController clientController);
    void moveWorker(ClientController clientController);
    void buildBlock(ClientController clientController);
    void removeBlock(ClientController clientController);
    void skipAction(ClientController clientController);
    void showCards(ClientController clientController);
    void setupConnection(ClientController clientController);

}
