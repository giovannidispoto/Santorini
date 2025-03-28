package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.SantoriniException;

public interface View {
    void startGame() throws SantoriniException;
    void printBattlefield();
    void callErrorMessage(String message);
    void callMatchResult(String result);

}
