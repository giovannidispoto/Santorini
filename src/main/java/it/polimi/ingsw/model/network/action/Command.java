package it.polimi.ingsw.model.network.action;

import it.polimi.ingsw.controller.Controller;

public interface Command {
    /**
     *
     */
    public void execute(Controller c);
}
