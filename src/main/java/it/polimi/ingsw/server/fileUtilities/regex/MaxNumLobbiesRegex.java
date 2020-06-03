package it.polimi.ingsw.server.fileUtilities.regex;

import it.polimi.ingsw.server.fileUtilities.FileManager;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.ansiBLUE;
import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.ansiRESET;

public class MaxNumLobbiesRegex implements Regex{

    @Override
    public String getRegexString() {
        return "^maxNumLobbies:\\d{1,3}$";
    }

    @Override
    public boolean getData(String line, FileManager fileManager) {
        if(null == line){
            return false;
        }

        String[] split = line.split(":");

        int maxNumLobbies = Integer.parseInt(split[1]);

        if(maxNumLobbies >= 1 && maxNumLobbies <= 999)
        {
            System.out.println(ansiBLUE+"Syntax Correct - maxNumLobbies - Loaded: "+maxNumLobbies+ansiRESET);
            fileManager.setMaxNumLobbiesManaged(maxNumLobbies);
            return true;

        }else {
            System.out.println(ansiBLUE+"NOT Valid - maxNumLobbies"+ansiRESET);
            return false;
        }

    }
}
