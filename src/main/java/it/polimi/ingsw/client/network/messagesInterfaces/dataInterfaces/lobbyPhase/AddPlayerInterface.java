package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

/**
 * Allows the request to add player to be sent to the server, with chosen nickName and desired lobby size
 */
public class AddPlayerInterface {
        String playerNickname;
        int lobbySize;

        /**
         * Create new data interface
         *
         * @param playerNickname player NickName
         * @param lobbySize lobby type es:(2-3 players)
         */
        public AddPlayerInterface(String playerNickname, int lobbySize) {
            this.playerNickname = playerNickname;
            this.lobbySize = lobbySize;
        }
}
