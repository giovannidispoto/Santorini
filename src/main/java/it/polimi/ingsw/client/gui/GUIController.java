package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.SantoriniException;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * GUIController class manage all the GUI, used from the controller for interact with the GUI
 */
public class GUIController implements View {
    private static  ClientController clientController;
    private BattlefieldView battlefield;
    private static GUIController instance;

    /**
     * Create new controller
     * @param controller controller
     */
    public GUIController(ClientController controller) {
        clientController = controller;
    }

    /**
     * Start Game
     */
    @Override
    public void startGame() {
        GUIBuilder guiBuilder = new GUIBuilder();

        GUIBuilder.setGUIController(this);
        guiBuilder.launchGUI(this);

    }

    /**
     * Add Battlefield
     * @param battlefield battlefield
     */
    public void addBattlefield(BattlefieldView battlefield){
        this.battlefield = battlefield;
    }

    /**
     * Gets controller
     * @return controller
     */
    public static ClientController getController(){
        return clientController;
    }

    /**
     * Called by controller when new battlefield is ready to show
     */
    @Override
    public void printBattlefield() {
        battlefield.reloadBattlefield();
    }

    /**
     * Error message, not used
     * @param message message
     */
    @Override
    public void callErrorMessage(String message) {
        //Not used in GUI
    }

    /**
     * Shows result of the game
     * @param message message
     */
    @Override
    public void callMatchResult(String message) {
        System.out.println(message);
    }

}
