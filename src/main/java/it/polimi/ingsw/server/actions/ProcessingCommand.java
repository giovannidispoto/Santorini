package it.polimi.ingsw.server.actions;

import com.google.gson.JsonElement;
import it.polimi.ingsw.server.actions.commands.Command;

/**
 *  Interface required to deserialize through hashmap
 */
public interface ProcessingCommand {
    Command command(JsonElement jsonElement);
}
