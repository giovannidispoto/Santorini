package it.polimi.ingsw.model.parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.Deck;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DivinityEffectReader {

    /**
     * Return map association between card name and effect
     * @param reader file to read
     * @return association between card and relative effect
     */
    public Map<String, Turn> load(Reader reader){
        BufferedReader buff = new BufferedReader(reader);
        GsonBuilder gsonBuilder = new GsonBuilder();

        Type type = new TypeToken<HashMap<String, Turn>>(){}.getType();
        gsonBuilder.registerTypeAdapter(type,new DivinityEffectDeserializer());

        Gson gson = gsonBuilder.create();

        return gson.fromJson(buff, type);
    }
}
