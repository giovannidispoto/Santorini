package it.polimi.ingsw.server.fileUtilities.regex;

import it.polimi.ingsw.server.fileUtilities.FileManager;

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
     * @param fileManager   FileManager, where to save the data
     */
    void getData(String line, FileManager fileManager);
}
