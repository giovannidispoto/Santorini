package it.polimi.ingsw.client.controller;

import java.util.*;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.network.ServerHandler;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicActionInterface;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.*;
import it.polimi.ingsw.client.clientModel.basic.Color;


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

    //Request Messages Area
    public void addNewPlayerRequest(String playerNickname, String date, Color color, String cardName){
        PlayerInterface data = new PlayerInterface(playerNickname, date, color, cardName);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("addNewPlayer", data)));
    }

    public void setPlayerReadyRequest(String playerNickname){
        SendStringInterface data = new SendStringInterface(playerNickname);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setPlayerReady", data)));
    }

    public void getWorkersIDRequest(String playerNickname){
        SendStringInterface data = new SendStringInterface(playerNickname);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("getWorkersID", data)));
    }

    public void getPlayersRequest(){
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getPlayers")));
    }

    public void setInitialWorkerPositionRequest(String playerNickname, int workerID, int row, int col){
        SetInitialWorkerPositionInterface data = new SetInitialWorkerPositionInterface(playerNickname, workerID, row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setInitialWorkerPosition", data)));
    }

    public void selectWorkerRequest(String playerNickname, int workerID) {
        SelectWorkerInterface data = new SelectWorkerInterface(playerNickname, workerID);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("selectWorker", data)));
    }

    public void playStepRequest(int row, int col) {
        PlayStepInterface data = new PlayStepInterface(row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("playStep", data)));
    }

    public void startTurnRequest(String playerNickname, Boolean basicTurn){
        StartTurnInterface data = new StartTurnInterface(playerNickname, basicTurn);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("startTurn", data)));
    }

    public void skipStepRequest() {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("skipStep")));
    }
}
