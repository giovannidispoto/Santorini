package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.WorkerPositionInterface;

import java.util.ArrayList;
import java.util.List;

public class CliController implements View {
    private final String cliColor;
    private final ClientController clientController;

    public CliController(String cliColor, ClientController clientController) {
        this.cliColor = cliColor;
        this.clientController = clientController;
    }

    @Override
    public void startGame() {
        CLIBuilder commandLine = new CLIBuilder(cliColor,clientController);

        commandLine.setupConnection(clientController);
        //Connection with server is UP
        clientController.waitSetPickedCards();
        //Woke up by: setPickedCards
        clientController.getDeckRequest();
        //Woke up by: getDeckResponse

        if(clientController.getGodPlayer().equals(clientController.getPlayerNickname())){
            commandLine.pickCards(clientController);
        }
        else{
            commandLine.printGodPlayerActivity(clientController);
        }
        clientController.waitSetPlayerCard();
        //Woke up by: setPlayerCard

        //Player choose his card used in game
        commandLine.chooseCard(clientController);

        clientController.waitSetWorkersPosition();
        //Woke up by: SetWorkersPosition
        clientController.getPlayersRequest();
        clientController.getBattlefieldRequest();

        List<WorkerPositionInterface> workersPosition= new ArrayList<>();

        for(int i : clientController.getWorkersID()){
            //commandLine.stampBattlefield;
            workersPosition.add(placeWorker(clientController, clientController.getWorkersID().get(i)));
        }
        //commandLine.stampBattlefield;
        clientController.setWorkersPositionRequest(clientController.getPlayerNickname(), workersPosition);

        clientController.waitBattlefieldUpdate();
        //Woke up by: SetBattlefield
    }

    public WorkerPositionInterface placeWorker(ClientController clientController, int workerID){
        int x=0,y=0;
        do {
            //Prendi dall'utente x e y
        }while(BattlefieldClient.getBattlefieldInstance().isCellOccupied(x,y));
        BattlefieldClient.getBattlefieldInstance().getCell(x,y).setPlayer(clientController.getPlayerNickname());
        BattlefieldClient.getBattlefieldInstance().getCell(x,y).setWorkerColor(clientController.getPlayerColor());
        return new WorkerPositionInterface(workerID, x, y);
    }
}
