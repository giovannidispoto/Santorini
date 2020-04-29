package it.polimi.ingsw.client.controller;

/** Objects used by LockManager
 *  -default state = false (Lock not yet unlocked)
 *  -status after notify = true (Lock unlocked)
 *
 */
public class LockObject {
    private boolean state = false;

    //Getter
    public boolean isState() {
        return state;
    }

    /** Set Status = true
     *  status after notify = true (Lock unlocked)
     */
    public void setUsed() {
        this.state = true;
    }

    /** Set Status = false
     *  status after wait = false
     *  (Lock reset because the caller has been awakened)
     */
    public void resetState() {
        this.state = false;
    }
}
