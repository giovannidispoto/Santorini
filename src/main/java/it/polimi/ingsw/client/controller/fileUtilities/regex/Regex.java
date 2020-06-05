package it.polimi.ingsw.client.controller.fileUtilities.regex;

import it.polimi.ingsw.client.controller.fileUtilities.FileManager;

import java.util.List;

public interface Regex {
    /**
     * Returns a string containing a regex properly written based on the data to be read
     * @return  String containing a regex
     */
    String getRegexString();

    /**
     * Called only if you are sure the line passed corresponds to the regex present in the object called,
     * it takes care of reading the data from the line and loading it into the FileManager
     * @param line  String representing a line read from a file
     * @param fileManager   FileManager, where to save the data with setter
     * @param fileReadingResults list of strings that will be written in the client logger, add and they will be printed
     * @return true if the data has been written correctly, false if the data has not been loaded
     */
    boolean getData(String line, FileManager fileManager, List<String> fileReadingResults);
}
