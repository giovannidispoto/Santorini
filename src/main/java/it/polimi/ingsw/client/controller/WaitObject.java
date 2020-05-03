package it.polimi.ingsw.client.controller;

/** Objects used by WaitManager
 *  -default state = false (Wait not yet unlocked)
 *  -status after notify = true (Wait unlocked)
 *
 */
public class WaitObject {
    private boolean used = false;

    //Getter
    public boolean isUsed() {
        return used;
    }

    /** Set Status = true
     *  status after notify = true (Wait unlocked)
     */
    public void setUsed() {
        this.used = true;
    }

    /** Set Status = false
     *  status after wait = false
     *  (Wait reset because the caller has been awakened)
     */
    public void resetState() {
        this.used = false;
    }
}
