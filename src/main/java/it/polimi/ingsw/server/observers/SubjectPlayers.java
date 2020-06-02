package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.model.Player;

public interface SubjectPlayers {
        public void attach(ObserverPlayers o);
        public void detach(ObserverPlayers o);
        public void detachAll();
        public void notify(Player removedPlayer);
}
