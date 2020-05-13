package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.ClientController;

public class GUIController implements View {
    private static  ClientController clientController;

    public GUIController(ClientController controller) {
        clientController = controller;
    }

    @Override
    public void startGame() {
        GUIBuilder guiBuilder = new GUIBuilder();
        guiBuilder.launchGUI();

        //test

    }

    public static ClientController getController(){
        return clientController;
    }

    @Override
    public void printBattlefield() {

    }

    @Override
    public void callErrorMessage(String message) {

    }

    @Override
    public void callMatchResult(String message) {

    }

}
