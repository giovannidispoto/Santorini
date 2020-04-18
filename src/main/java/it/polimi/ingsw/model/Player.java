package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DivinityCard;

import java.time.LocalDate;
import java.util.List;

/**
 * Player class represents a player which takes part to the game
* */
public class Player {

    private final LocalDate playerBirthday;
    private final String playerNickname;
    private DivinityCard playerCard;
    private List<Worker> playerWorkers;
    private boolean ready;
    private final Color playerColor;

    /**
     * Class constructor
     * @param playerNickname of the player such as SteveJobs97🍏
     * @param playerBirthday of the player, used to identify the youngest one
     *                       A simple integer is not enough because maybe there could be more players with the same age
     * @param playerColor color assigned to the player and his workers
     */
    public Player(String playerNickname, LocalDate playerBirthday, Color playerColor ){
        this.playerNickname = playerNickname;
        this.playerBirthday = playerBirthday;
        this.playerColor = playerColor;
    }

    /**
     * Gets player card
     * @return player card
     */
    public DivinityCard getPlayerCard() {
        return playerCard;
    }

    /**
     * Gets player Nickname
     * @return player nickname
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * Gets player worker
     * @return player worker
     */
    public List<Worker> getPlayerWorkers() {
        return List.copyOf(playerWorkers);
    }

    /**
     * Assigns a divinity card to the player
     * @param card that has to be given to the player
     */
    public void setPlayerCard(DivinityCard card){
        this.playerCard = card;
    }

    /**
     * Assigns workers to the player
     * @param playerWorkers that has to be given to the player
     */
    public void setPlayerWorkers(List<Worker> playerWorkers){
        this.playerWorkers = playerWorkers;
    }

    /**
     * Gets player color
     * @return Color object
    * */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Get another player who has not been moved
     * @return stationaryWorker
     * */
    public Worker getStationaryWorker(Worker selectedWorker){
        Worker stationaryWorker = null;
        for(Worker playerWorker : playerWorkers)
            if(!playerWorker.equals(selectedWorker))
                stationaryWorker=playerWorker;
        return stationaryWorker;
    }

    /**
     *
     * @return
     */
    public LocalDate getPlayerBirthday(){
        return this.playerBirthday;
    }
    /**
     *
     * @param ready
     */
    public void setReady(boolean ready){
        this.ready = ready;
    }

    /**
     *
     * @return
     */
    public boolean isReady(){
        return ready;
    }
}
