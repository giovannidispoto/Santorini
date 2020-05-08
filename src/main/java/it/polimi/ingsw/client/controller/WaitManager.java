package it.polimi.ingsw.client.controller;

/** WARNING!!!
 *  The wait manager function is to synchronize the client with the server,
 *  its use is only allowed to the controller and the network.
 *  Operation is based on wait and notify of threads.
 *
 *  N.B: The client (gui or cli) will only have to call the functions of the controller,
 *  which will take care of the synchronization only on the functions where it is necessary.
 */
public class WaitManager {
    public final WaitObject waitAddPlayer;
    public final WaitObject waitSetPickedCards;
    public final WaitObject waitGetDeck;
    public final WaitObject waitSetPlayerCard;
    public final WaitObject waitSetWorkersPosition;
    public final WaitObject waitGetPlayers;
    public final WaitObject waitGetBattlefield;
    public final WaitObject waitActualPlayer;
    public final WaitObject waitStartTurn;
    public final WaitObject waitWorkerViewUpdate;
    public final WaitObject waitPlayStepResponse;
    public final WaitObject waitSkipStepResponse;

    public WaitManager() {
        this.waitAddPlayer      =   new WaitObject();
        this.waitSetPickedCards =   new WaitObject();
        this.waitGetDeck        =   new WaitObject();
        this.waitSetPlayerCard  =   new WaitObject();
        this.waitSetWorkersPosition =   new WaitObject();
        this.waitGetPlayers     =   new WaitObject();
        this.waitGetBattlefield =   new WaitObject();
        this.waitActualPlayer   =   new WaitObject();
        this.waitStartTurn      =   new WaitObject();
        this.waitWorkerViewUpdate   =   new WaitObject();
        this.waitPlayStepResponse   =   new WaitObject();
        this.waitSkipStepResponse   =   new WaitObject();
    }

    /** Takes care of waiting on a specific request / operation,
     *  based on the Wait Object passed as a parameter.
     *
     *  1)  This function check if the wait has already been used,
     *  this means that the data is already available and there is no need to wait,
     *  therefore the wait status is reset and the function ends.
     *
     *  2)  If the wait has not already been used,
     *  this means that the data has not yet arrived and the calling thread must be put on hold with wait.
     *  When the data arrives it will be unlocked and the wait reset.
     *
     *  N.B:    This function can generate an error only if the thread flag (Interrupted) is set true.
     *
     * @param object    WaitObject based on the activity you are waiting for
     * @param clientController  ClientController
     * @throws SantoriniException: if there was an error (InterruptedException)
     */
    public void setWait(WaitObject object, ClientController clientController) throws SantoriniException {
        //If the unlocking operation has already been performed
        if(object.isUsed()){
            object.resetState();
            return;
        }
        //If the unlocking operation has not already been performed -> WAIT
        try {
            object.wait();
        } catch (InterruptedException e) {
            //re-set interrupted flag
            Thread.currentThread().interrupt();
            throw clientController.getGameException();
        }

        object.resetState();
    }
}
