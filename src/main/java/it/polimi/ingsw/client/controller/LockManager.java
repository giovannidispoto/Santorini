package it.polimi.ingsw.client.controller;

/** WARNING!!!
 *  The lock manager function is to synchronize the client with the server,
 *  its use is only allowed to the controller and the network.
 *  Operation is based on wait and notify of threads.
 *
 *  N.B: The client (gui or cli) will only have to call the functions of the controller,
 *  which will take care of the synchronization only on the functions where it is necessary.
 */
public class LockManager {
    public final LockObject lockGetPlayers;
    public final LockObject lockAddPlayer;
    public final LockObject lockSetPickedCards;
    public final LockObject lockGetDeck;
    public final LockObject lockGetCardsInGame;
    public final LockObject lockSetPlayerCard;
    public final LockObject lockGetWorkersID;
    public final LockObject lockGetBattlefield;

    public LockManager() {
        this.lockGetPlayers =   new LockObject();
        this.lockAddPlayer  =   new LockObject();
        this.lockSetPickedCards =   new LockObject();
        this.lockGetDeck    =   new LockObject();
        this.lockGetCardsInGame = new LockObject();
        this.lockSetPlayerCard  =   new LockObject();
        this.lockGetWorkersID  =   new LockObject();
        this.lockGetBattlefield  =   new LockObject();
    }

    /** Takes care of waiting on a specific request / operation,
     *  based on the Lock Object passed as a parameter.
     *
     * @param object    LockObject based on the activity you are waiting for
     * @return  false: if there was an error (InterruptedException), true: method performed without errors
     */
    public boolean setWait(LockObject object){
        //If the unlocking operation has already been performed
        if(object.isState()){
            object.resetState();
            return true;
        }
        //If the unlocking operation has not already been performed -> WAIT
        try {
            object.wait();
        } catch (InterruptedException e) {
            return false;
        }
        //TODO: Thinking about resetState (only in LockManager): (Gorlenah)
        object.resetState();
        return true;
    }
}
