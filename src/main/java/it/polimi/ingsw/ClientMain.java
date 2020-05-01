package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLIBuilder;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.gui.GUIBuilder;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.WorkerPositionInterface;

import java.util.ArrayList;
import java.util.List;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        String cliColor="dark";
        ClientController clientController = new ClientController();
        clientController.startNetwork();
        //Launch CLI -> Santorini.jar cli dark || Santorini.jar cli white
            if(args[0].equals("cli")){
                if(args.length==2)
                    if(args[1].equals("light"))
                        cliColor="light";
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

                clientController.waitSetWorkersID();
                //Woke up by: SetWorkersID
                List<WorkerPositionInterface> workersPosition= new ArrayList<>();

                clientController.getBattlefieldRequest();

                for(int i : clientController.getWorkersID()){
                    //commandLine.stampBattlefield;
                    //workersPosition.add(placeWorker(clientController, clientController.getWorkersID().get(i)));
                }
                //commandLine.stampBattlefield;
                clientController.setInitialWorkersPositionRequest(clientController.getPlayerNickname(), workersPosition);

                clientController.waitSetBattlefield();
                clientController.waitSetPlayers();
                //Woke up by: SetBattlefield & SetPlayers
            }
            //Launch GUI -> Santorini.jar gui
            else{
                GUIBuilder gui = new GUIBuilder();
            }
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
