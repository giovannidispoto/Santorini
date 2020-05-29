package it.polimi.ingsw.client.gui;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class GameEndRunnable implements Runnable{
    private Callable<Void> handler;
    @Override
    public void run() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                try {
                    handler.call();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public void setInterruptHandler(Callable<Void> handler){
        this.handler = handler;
    }
}
