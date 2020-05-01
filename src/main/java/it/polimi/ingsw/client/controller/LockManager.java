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
    public final LockObject lockAddPlayer;
    public final LockObject lockSetPickedCards;
    public final LockObject lockGetDeck;
    public final LockObject lockSetPlayerCard;
    public final LockObject lockSetWorkersPosition;
    public final LockObject lockGetPlayers;
    public final LockObject lockGetBattlefield;
    public final LockObject lockSetBattlefield;

    public LockManager() {
        this.lockAddPlayer      =   new LockObject();
        this.lockSetPickedCards =   new LockObject();
        this.lockGetDeck        =   new LockObject();
        this.lockSetPlayerCard  =   new LockObject();
        this.lockSetWorkersPosition =   new LockObject();
        this.lockGetPlayers     =   new LockObject();
        this.lockGetBattlefield =   new LockObject();
        this.lockSetBattlefield =   new LockObject();
    }

    /** Takes care of waiting on a specific request / operation,
     *  based on the Lock Object passed as a parameter.
     *
     *  1)  This function check if the lock has already been used,
     *  this means that the data is already available and there is no need to wait,
     *  therefore the lock status is reset and the function ends.
     *
     *  2)  If the lock has not already been used,
     *  this means that the data has not yet arrived and the calling thread must be put on hold with wait.
     *  When the data arrives it will be unlocked and the lock reset.
     *
     *  N.B:    This function can generate an error only if the thread flag (Interrupted) is set true.
     *
     * @param object    LockObject based on the activity you are waiting for
     * @return  false: if there was an error (InterruptedException), true: method performed without errors
     */
    public boolean setWait(LockObject object){
        //If the unlocking operation has already been performed
        if(object.isUsed()){
            object.resetState();
            return true;
        }
        //If the unlocking operation has not already been performed -> WAIT
        try {
            object.wait();
        } catch (InterruptedException e) {
            //TODO: Manage Well Interruption
            return false;
        }

        object.resetState();
        return true;
    }
}
