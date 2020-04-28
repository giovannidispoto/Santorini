package it.polimi.ingsw.client.network.commandDeserializer;

import com.google.gson.JsonElement;
import it.polimi.ingsw.client.network.actions.Command;

public interface ProcessingCommand {
    Command command(JsonElement jsonElement);
}
