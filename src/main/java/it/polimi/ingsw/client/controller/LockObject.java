package it.polimi.ingsw.client.controller;

/** Objects used by LockManager
 *  -default state = false (Lock not yet unlocked)
 *  -status after notify = true (Lock unlocked)
 *
 */
public class LockObject {
    private boolean used = false;

    //Getter
    public boolean isUsed() {
        return used;
    }

    /** Set Status = true
     *  status after notify = true (Lock unlocked)
     */
    public void setUsed() {
        this.used = true;
    }

    /** Set Status = false
     *  status after wait = false
     *  (Lock reset because the caller has been awakened)
     */
    public void resetState() {
        this.used = false;
    }
}
