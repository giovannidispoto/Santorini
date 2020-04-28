package it.polimi.ingsw.client.network.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.network.commandDeserializer.CommandDeserializer;

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

        gsonBuilder.registerTypeAdapter(Command.class ,new CommandDeserializer());

        Gson gson = gsonBuilder.create();

        return gson.fromJson(message, Command.class);
    }
}
