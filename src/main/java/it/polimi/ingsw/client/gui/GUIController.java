package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.SantoriniException;
import javafx.scene.layout.GridPane;

public class GUIController implements View {
    private static  ClientController clientController;
    private BattlefieldView battlefield;
    private static GUIController instance;

    public GUIController(ClientController controller) {
        clientController = controller;
    }

    @Override
    public void startGame() {
        GUIBuilder guiBuilder = new GUIBuilder();
        //guiBuilder.setGUIController(this);
        GUIBuilder.setGUIController(this);
        guiBuilder.launchGUI(this);


        //test

    }

    public void addBattlefield(BattlefieldView battlefield){
        this.battlefield = battlefield;
    }

    public static ClientController getController(){
        return clientController;
    }




    @Override
    public void printBattlefield() {
        battlefield.reloadBattlefield();
    }

    @Override
    public void callErrorMessage(String message) {

    }

    @Override
    public void callMatchResult(String message) {

    }

}
