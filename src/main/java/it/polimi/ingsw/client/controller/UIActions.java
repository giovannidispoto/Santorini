package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.network.ClientSocketConnection;

public interface UIActions {
    //Web Actions
    void moveWorker(ClientController clientController);
    void buildBlock(ClientController clientController);
    void removeBlock(ClientController clientController);
    void selectCard(ClientController clientController);
    void selectWorker(ClientController clientController);
    void placeWorkers(ClientController clientController);
    void skipAction(ClientController clientController);

    //Only Client side actions
    void showCards(ClientController clientController);
    void setServerInformations(ClientSocketConnection clientSocket, ClientController clientController);

}
