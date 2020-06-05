package it.polimi.ingsw.server.network.commandDeserializer;

import com.google.gson.JsonElement;
import it.polimi.ingsw.server.network.commands.Command;

/**
 *  Interface required to deserialize through hashmap
 */
public interface ProcessingCommand {
    Command command(JsonElement jsonElement);
}
