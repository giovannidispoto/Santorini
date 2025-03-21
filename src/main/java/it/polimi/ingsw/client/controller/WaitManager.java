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
    public static final WaitObject waitAddPlayer            = new WaitObject();
    public static final WaitObject waitSetPickedCards       = new WaitObject();
    public static final WaitObject waitGetDeck              = new WaitObject();
    public static final WaitObject waitSetPlayerCard        = new WaitObject();
    public static final WaitObject waitSetWorkersPosition   = new WaitObject();
    public static final WaitObject waitGetPlayers           = new WaitObject();
    public static final WaitObject waitGetBattlefield       = new WaitObject();
    public static final WaitObject waitActualPlayer         = new WaitObject();
    public static final WaitObject waitStartTurn            = new WaitObject();
    public static final WaitObject waitWorkerViewUpdate     = new WaitObject();
    public static final WaitObject waitPlayStepResponse     = new WaitObject();
    public static final WaitObject waitSkipStepResponse     = new WaitObject();


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
     * @throws SantoriniException game general purpose exception
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
            clientController.interruptNormalThreadExecution();
            throw clientController.getGameException();
        }

        object.resetState();
    }
}
