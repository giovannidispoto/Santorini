package it.polimi.ingsw.client.controller.fileUtilities.regex;

import it.polimi.ingsw.client.controller.fileUtilities.FileManager;

import java.util.List;

public class UniqueLoggerRegex implements Regex {

    @Override
    public String getRegexString() {
        return "^uniqueLogger:((true)|(false))$";
    }

    @Override
    public boolean getData(String line, FileManager fileManager, List<String> fileReadingResults){
        if(null == line){
            return false;
        }

        String[] split = line.split(":");

        boolean uniqueLogger = Boolean.parseBoolean(split[1]);

        fileReadingResults.add("Syntax Correct - uniqueLogger - Loaded: "+uniqueLogger);
        fileManager.setUniqueLogger(uniqueLogger);
        return true;

    }
}
