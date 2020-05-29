package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.ClientController;

public class GioThread extends Thread {
    private final ClientController clientController;

    public GioThread(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public synchronized void run() {
        try {
            clientController.registerControllerThread(this);
            wait();
        } catch (InterruptedException e) {
            System.out.println(clientController.getGameException().getMessage());
        }
    }
}
