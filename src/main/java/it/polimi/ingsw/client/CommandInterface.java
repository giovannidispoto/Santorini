package it.polimi.ingsw.client;

public interface CommandInterface {
    //Web Actions
    public void moveWorker();
    public void buildBlock();
    public void removeBlock();
    public void selectCard();
    public void selectWorker();
    public void placeWorkers();
    public void skipAction();

    //Only Client side actions
    public void showCards();

}
