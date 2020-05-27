package it.polimi.ingsw.server.fileUtilities;

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
    private int serverPort = 1337;

    /**
     *  Constructor of the FileManager,
     *  takes care of initializing the Regex that will be used for reading from files
     */
    public FileManager() {
        this.regexList = new ArrayList<>();
        this.regexList.add(new ServerPortRegex());
    }

    /**
     * Function that tests the reading of the essential files for the program and shows the actual loading by printing the path
     */
    public synchronized void testFileReading(){
        System.out.print(INITIALIZE_SCREEN);
        System.out.print(CLEAN);
        System.out.println(ansiBLUE+"Testing-File-Reading:");
        System.out.println(ServerMain.class.getClassLoader().getResource(cardsEffectPath));
        System.out.println(ServerMain.class.getClassLoader().getResource(divinitiesCardsPath)+ansiRESET+nextLine+ consoleSeparator +nextLine);
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
        System.out.println();
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
                regex.getData(line,this);
                return true;
            }

        }
        return false;
    }

    //------------------------------------------    GETTERS & SETTERS
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
