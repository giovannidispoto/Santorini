package it.polimi.ingsw.server.fileUtilities.regex;

import it.polimi.ingsw.server.fileUtilities.FileManager;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.ansiBLUE;
import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.ansiRESET;

public class ServerPortRegex implements Regex {


    public String getRegexString() {
        return "^serverPort:\\d{1,5}$";
    }

    public void getData(String line, FileManager fileManager){
        String[] split = line.split(":");

        int serverPort = Integer.parseInt(split[1]);

        if(serverPort >= 1024 && serverPort <= 65535)
        {
            System.out.println(ansiBLUE+"Syntax Correct - serverPort - Loaded"+ansiRESET);
            fileManager.setServerPort(serverPort);
        }else {
            System.out.println(ansiBLUE+"NOT Valid - serverPort"+ansiRESET);
        }

    }
}
