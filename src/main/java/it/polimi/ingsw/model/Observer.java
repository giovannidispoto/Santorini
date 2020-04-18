package it.polimi.ingsw.model;

import it.polimi.ingsw.server.Message;

public interface Observer {
    public void update(Message message);
}
