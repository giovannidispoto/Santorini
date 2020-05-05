package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.network.ClientSocketConnection;
import it.polimi.ingsw.client.network.data.dataInterfaces.WorkerPositionInterface;

public interface UIActions {
    //UI Actions implemented by CLI and GUI
    void pickCards(ClientController clientController);
    void chooseCard(ClientController clientController);
    WorkerPositionInterface placeWorkers(ClientController clientController, int workerID);
    void selectWorker(ClientController clientController);
    void moveWorker(ClientController clientController);
    void buildBlock(ClientController clientController);
    void removeBlock(ClientController clientController);
    void setupConnection(ClientController clientController);

}
