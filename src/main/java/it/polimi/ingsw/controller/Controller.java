package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.cards.DivinityCard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Player> players;
    private Match match;
    private PlayerTurn turn;

    public Controller(Match match){
        players = new ArrayList<>();
    }

    public void addNewPlayer(String playerNickName, LocalDate date, Color color, DivinityCard card){
        Player p = new Player(playerNickName, date, color);
        p.setPlayerCard(card);
        players.add(p);
    }

    public List<Player> getPlayers(){
        return List.copyOf(players);
    }

    public void addWorkers(Player player){
        Worker w1 = new Worker(player);
        Worker w2 = new Worker(player);
        List<Worker> workerList = new ArrayList<>();
        workerList.add(w1);
        workerList.add(w2);
        player.setPlayerWorkers(workerList);
    }

    public void startMatch(){
        match = new Match(players, new ArrayList<>());
    }

    public void selectWorker(Worker w){
        match.setSelectedWorker(w);
    }

    public void selectTurn(boolean basic){
        PlayerTurn turn = new PlayerTurn(match.generateTurn(basic));
    }




}
