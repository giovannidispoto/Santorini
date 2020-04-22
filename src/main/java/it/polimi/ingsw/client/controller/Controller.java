package it.polimi.ingsw.client.controller;

import java.util.*;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.network.ServerHandler;
import it.polimi.ingsw.client.network.actions.data.*;


/**
 * Controller Class
 */
public class Controller{
    private Step turn;
    private List<PlayerInterface> players;
    private List<DivinityCard> cards;
    private List<Integer> workersID;
    private ServerHandler serverHandler;

    /**
     * Controller Constructor
     */
    public Controller(){
        this.players = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.workersID = new ArrayList<>();
    }

    /**
     * Associate controller to server handler
     * @param serverHandler server handler
     */
    public void registerHandler(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }

    /**
     * Gets players in match
     * @return players
     */
    public List<PlayerInterface> getPlayers(){
        return players;
    }

    /**
     * Sets players in match
     * @param players players in match
     */
    public void setPlayers(List<PlayerInterface> players){
        this.players = players;
    }

    /**
     * Gets your workersID
     * @return workersID
     */
    public List<Integer> getWorkersID() {
        return workersID;
    }

    /**
     * Sets your workersID
     * @param workersID client workersID
     */
    public void setWorkersID(List<Integer> workersID) {
        this.workersID = workersID;
    }

    //Request Area
    public void playStepRequest(int Row, int Col) {
        PlayStepDataInterface data = new PlayStepDataInterface(Row, Col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("playStep", data)));
    }

    public void selectWorkerRequest(String playerNickname, int workerID) {
        SelectWorkerDataInterface data = new SelectWorkerDataInterface(playerNickname, workerID);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("selectWorker", data)));
    }

    public void skipActionRequest() {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("skipStep")));
    }
}
