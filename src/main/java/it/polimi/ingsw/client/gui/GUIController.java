package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.ClientController;

public class GUIController implements View {
    private final ClientController clientController;

    public GUIController(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void startGame() {
        GUIBuilder guiBuilder = new GUIBuilder();
        //test
        clientController.getSocketConnection().setServerPort(2027);
    }

    @Override
    public void printBattlefield() {

    }
}
