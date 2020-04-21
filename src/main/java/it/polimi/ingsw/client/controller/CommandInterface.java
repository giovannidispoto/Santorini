package it.polimi.ingsw.client.controller;

public interface CommandInterface {
    //Web Actions
    void moveWorker();
    void buildBlock();
    void removeBlock();
    void selectCard();
    void selectWorker();
    void placeWorkers();
    void skipAction();

    //Only Client side actions
    void showCards();

}
