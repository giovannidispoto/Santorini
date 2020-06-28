package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.model.Player;

/**
 * Observer interface used for Players
 * */
public interface SubjectPlayers {
        /**
         * Attach observer to subject
         * @param o observer
         */
        public void attach(ObserverPlayers o);

        /**
         * Attach observer to subject
         * @param o observer
         */
        public void detach(ObserverPlayers o);

        /**
         * Detach all the obserber from the subject
         */
        public void detachAll();

        /**
         * Notify observer
         * @param removedPlayer removed Player
         */
        public void notify(Player removedPlayer);
}
