package it.polimi.ingsw.model.network.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Factory class for Command
 */
public class CommandFactory {

    /**
     *
     * @param message
     * @return
     */
    public static Command from(String message){
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Command.class ,new CommandDeserializer());

        Gson gson = gsonBuilder.create();

        return gson.fromJson(message, Command.class);
    }
}
