package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

import it.polimi.ingsw.client.clientModel.basic.Color;

public class AddPlayerInterface {
        private String playerNickname;
        private Color color;

        /**
         * Create command
         *
         * @param playerNickname player
         * @param color          color
         */
        public AddPlayerInterface(String playerNickname, Color color) {
            this.playerNickname = playerNickname;
            this.color = color;
        }
}
