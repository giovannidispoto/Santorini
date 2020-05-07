package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;


public class AddPlayerInterface {
        private String playerNickname;
        private int lobbySize;

        /**
         * Create command
         *
         * @param playerNickname player NickName
         * @param lobbySize lobby type es:(2-3 players)
         */
        public AddPlayerInterface(String playerNickname, int lobbySize) {
            this.playerNickname = playerNickname;
            this.lobbySize = lobbySize;
        }
}
