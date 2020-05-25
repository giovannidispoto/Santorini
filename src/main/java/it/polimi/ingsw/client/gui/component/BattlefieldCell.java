package it.polimi.ingsw.client.gui.component;

import javafx.scene.layout.GridPane;

public class BattlefieldCell {
    private AvailableCell ac;
    private Level1 lvl1;
    private Level2 lvl2;
    private Level3 lvl3;
    private Dome dome;
    private WorkerViewCellAvailable wvcl;
    private WorkerComponent wc;

    public BattlefieldCell(){
        ac = null;
        lvl1 = null;
        lvl2 = null;
        lvl3 = null;
        dome = null;
        wvcl = null;
        wc = null;
    }

    public void setAc(AvailableCell ac){
        this.ac = ac;
    }

    public void setLvl1(Level1 lvl1) {
        this.lvl1 = lvl1;
    }

    public void setLvl2(Level2 lvl2) {
        this.lvl2 = lvl2;
    }

    public void setLvl3(Level3 lvl3) {
        this.lvl3 = lvl3;
    }

    public void setDome(Dome dome) {
        this.dome = dome;
    }

    public void setWvcl(WorkerViewCellAvailable wvcl) {
        this.wvcl = wvcl;
    }

    public void setWc(WorkerComponent wc) {
        this.wc = wc;
    }

    public AvailableCell getAc() {
        return ac;
    }

    public Level1 getLvl1() {
        return lvl1;
    }

    public Level2 getLvl2() {
        return lvl2;
    }

    public Level3 getLvl3() {
        return lvl3;
    }

    public Dome getDome() {
        return dome;
    }

    public WorkerViewCellAvailable getWvcl() {
        return wvcl;
    }

    public WorkerComponent getWc() {
        return wc;
    }
}
