package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.actions.commands.Command;

/**
 * Factory class for Command
 */
public class CommandFactory {

    /**
     * Create command from string passed
     * @param message string command
     * @return command
     */
    public static Command from(String message){
        GsonBuilder gsonBuilder = new GsonBuilder();

        //add personalized adapter for deserializing
        gsonBuilder.registerTypeAdapter(Command.class ,new CommandDeserializer());

        Gson gson = gsonBuilder.create();

        return gson.fromJson(message, Command.class);
    }
}
