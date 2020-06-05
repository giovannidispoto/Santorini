package it.polimi.ingsw.server.fileUtilities.regex;

import it.polimi.ingsw.server.fileUtilities.FileManager;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.ansiBLUE;
import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.ansiRESET;

public class DebugMessagesRegex implements Regex {
    @Override
    public String getRegexString() {
        return "^debugMessages:((true)|(false))$";
    }

    @Override
    public boolean getData(String line, FileManager fileManager) {
        if(null == line){
            return false;
        }

        String[] split = line.split(":");

        boolean debugMessages = Boolean.parseBoolean(split[1]);
        System.out.println(ansiBLUE+"Syntax Correct - debugMessages - Loaded: "+debugMessages+ansiRESET);
        fileManager.setDebugMessages(debugMessages);
        return true;
    }
}
