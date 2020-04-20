package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Block;
import it.polimi.ingsw.model.Color;

import java.util.HashMap;

public class CLIDataObject {

    //ANSI Colors
    private static final String CODE_BLUE ="33";
    private static final String CODE_LIGHTBLUE ="75";
    private static final String CODE_BROWN ="130";
    private static final String CODE_GRAY ="252";
    private static final String CODE_WHITE ="255";
    private static final String CODE_GREEN = "41";
    private static final String ANSI_PRFX ="\u001b[38;5;";
    protected static final String ANSI_RST = "\u001b[0m";
    protected static final String ANSI_BLUE = ANSI_PRFX+CODE_BLUE+"m";
    protected static final String ANSI_LIGHTBLUE = ANSI_PRFX+CODE_LIGHTBLUE+"m";
    protected static final String ANSI_BROWN = ANSI_PRFX+CODE_BROWN+"m";
    protected static final String ANSI_GRAY = ANSI_PRFX+CODE_GRAY+"m";
    protected static final String ANSI_WHITE = ANSI_PRFX+CODE_WHITE+"m";
    protected static final String ANSI_GREEN = ANSI_PRFX+CODE_GREEN+"m";

    //Game Objects
    protected static final String WORKER = "ᳵ";
    protected static final String DOME = ANSI_LIGHTBLUE+"◉"+ANSI_RST;
    protected static final String GRASS = ANSI_GREEN+"᭟"+ANSI_RST;

    //Data
    private static final String cellDataTemplate = "%s %s";
    private static final String freeCellTemplate = "  "+GRASS;
    private String[] cellsData; // ┃col1┃col2┃col3┃col4┃col5┃
    private HashMap<Block,String> towersInformations;
    private HashMap<Color,String> colorsInformations;

    /**
     * Class Constructor : generates an empty row
     */
    public CLIDataObject() {
        this.cellsData = new String[5];
        this.towersInformations = new HashMap<>();
        this.colorsInformations = new HashMap<>();
        for(String current : cellsData){
            current=freeCellTemplate;
        }
        towersInformations.put(Block.GROUND,GRASS);
        towersInformations.put(Block.LEVEL_1,ANSI_WHITE+"1"+ANSI_RST);
        towersInformations.put(Block.LEVEL_2,ANSI_WHITE+"2"+ANSI_RST);
        towersInformations.put(Block.LEVEL_3,ANSI_WHITE+"3"+ANSI_RST);
        towersInformations.put(Block.DOME,DOME);
        colorsInformations.put(Color.BLUE,ANSI_BLUE+WORKER+ANSI_RST);
        colorsInformations.put(Color.BROWN,ANSI_BROWN+WORKER+ANSI_RST);
        colorsInformations.put(Color.GREY,ANSI_GRAY+WORKER+ANSI_RST);
    }

    /**
     * Writes data inside the row
     */
    public void writeData(){

    }
    /**
     * Resets the row to initial state
     */
    public void resetData(){
        for(String current : cellsData){
            current=freeCellTemplate;
        }

    }
}
