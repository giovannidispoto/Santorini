package it.polimi.ingsw.server.fileUtilities;

import it.polimi.ingsw.server.fileUtilities.regex.DebugMessagesRegex;
import it.polimi.ingsw.server.fileUtilities.regex.MaxNumLobbiesRegex;
import it.polimi.ingsw.server.fileUtilities.regex.Regex;
import it.polimi.ingsw.ServerMain;
import it.polimi.ingsw.server.fileUtilities.regex.ServerPortRegex;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.*;

public class FileManager {
    public static final String cardsEffectPath = "json/CardsEffect.json";
    public static final String divinitiesCardsPath = "json/Divinities.json";
    private static final String serverSettingsPath = "serverSettings.txt";
    private final List<Regex> regexList;
    private static final int maxFileAdditionalLines = 10;
    //------------------------------------------    DEFAULT DATA
    /**
     * Set the ip port, from which the server accepts client connections<br>
     * DEFAULT: serverPort = 1337
     */
    private int serverPort = 1337;
    /**
     * Set the maximum number of active lobbies<br>
     * DEFAULT: maxNumLobbiesManaged = 5
     */
    private int maxNumLobbiesManaged = 5;
    /**
     *  Enable or disable debug messages on cli<br>
     *  DEFAULT: debugMessages = false
     */
    private boolean debugMessages = false;

    /**
     *  Constructor of the FileManager,
     *  takes care of initializing the Regex that will be used for reading from files<br>
     *
     *  To developers: if you want to implement a new regex just create a new class insert it in the regex package and here in the constructor add it to the list
     */
    public FileManager() {
        this.regexList = new ArrayList<>();
        this.regexList.add(new ServerPortRegex());
        this.regexList.add(new MaxNumLobbiesRegex());
        this.regexList.add(new DebugMessagesRegex());
    }

    /**
     * Function that tests the reading of the essential files for the program and shows the actual loading by printing the path
     */
    public synchronized void testFileReading(){
        System.out.println(ansiBLUE+"Testing-File-Reading:");
        System.out.println(ServerMain.class.getClassLoader().getResource(cardsEffectPath));
        System.out.println(ServerMain.class.getClassLoader().getResource(divinitiesCardsPath)+ansiRESET+nextLine+ consoleSeparator);
    }

    /**
     *  Load the data contained in serverSettingsPath, based on the regex loaded in the FileManager constructor,
     *  the file containing the settings must be placed in the same folder as the server
     *
     *  If the file is not present or there is some reading error,
     *  FileManager will still contain the default values ​​that it should overwrite when reading from file
     */
    public void readServerSettings(){
        int regexUsedCounter = 0;
        Path serverSettings = Paths.get(getSystemPath().concat("/").concat(serverSettingsPath));

        if (!Files.exists(serverSettings)) {
            System.out.println(ansiBLUE+"ServerSettings File does not exist - Using Default Settings"+ansiRESET);
        } else if (!Files.isRegularFile(serverSettings)) {
            System.out.println(ansiBLUE+"File is not a ServerSettings, maybe a directory"+ansiRESET);
        } else if (!Files.isReadable(serverSettings)) {
            System.out.println(ansiBLUE+"ServerSettings File is not readable"+ansiRESET);
        } else {
            // everything is right, process the serverSettings
            try {
                List<String> strings = Files.readAllLines(serverSettings);

                if( strings.size() >= (regexList.size()+maxFileAdditionalLines) ){
                    System.out.println(ansiBLUE + "ServerSettings contains more than: "+maxFileAdditionalLines+" Additional Lines - Using Default Settings" + ansiRESET);
                }else {

                    for (String line : strings) {
                        if (null != line && regexFinder(line))
                            regexUsedCounter++;
                    }

                    if (regexUsedCounter == 0) {
                        System.out.println(ansiBLUE + "ServerSettings does not contain valid settings - Using Default Settings" + ansiRESET);
                    }
                }

            } catch (IOException e) {
                System.out.println(ansiBLUE+"ServerSettings File is not readable"+ansiRESET);
            }
        }
    }

    /**
     * Gets the root path where the jar is located
     * -EX:  C:\Users\ProgettoFinale-IDS\IdeaProjects
     * -JAR: C:\Users\ProgettoFinale-IDS\IdeaProjects\*.jar
     *
     * @return  String representing the path of the system on which the jar was run
     */
    private String getSystemPath(){
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        return dir.toString();
    }

    /**
     * Search in the regex structure if the line passed is coded correctly, if it is,
     * load the data in the appropriate values ​​present in FileManager
     * @param line  String representing a line read from a file
     * @return  true if a regex has been executed, false if the line does not match any regex
     */
    private boolean regexFinder(String line){
        for(Regex regex : regexList){

            if(line.matches(regex.getRegexString())) {
                return regex.getData(line,this);
            }

        }
        return false;
    }

    //------------------------------------------    GETTERS & SETTERS
    //N.B:  SETTERS are for the exclusive use of Regex

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getMaxNumLobbiesManaged() {
        return maxNumLobbiesManaged;
    }

    public void setMaxNumLobbiesManaged(int maxNumLobbiesManaged) {
        this.maxNumLobbiesManaged = maxNumLobbiesManaged;
    }

    public boolean isDebugMessages() {
        return debugMessages;
    }

    public void setDebugMessages(boolean debugMessages) {
        this.debugMessages = debugMessages;
    }
}
