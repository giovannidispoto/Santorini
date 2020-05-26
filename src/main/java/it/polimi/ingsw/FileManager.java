package it.polimi.ingsw;

import static it.polimi.ingsw.PrinterClass.*;

public final class FileManager {
    public static String cardsEffectPath = "json/CardsEffect.json";
    public static String divinitiesCardsPath = "json/Divinities.json";

    public synchronized void testFileReading(){
        System.out.print(INITIALIZE_SCREEN);
        System.out.print(CLEAN);
        System.out.println(ansiBLUE+"Testing-File-Reading:");
        System.out.println(ServerMain.class.getClassLoader().getResource(cardsEffectPath));
        System.out.println(ServerMain.class.getClassLoader().getResource(divinitiesCardsPath)+ansiRESET+nextLine);
    }
}
