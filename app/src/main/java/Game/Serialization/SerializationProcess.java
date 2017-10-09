package Game.Serialization;

import java.lang.reflect.Field;
import java.util.ArrayList;

import Game.Player;

public class SerializationProcess {

    private static int VERSION = 0;

    public static SerializablePlayer serialization(Player player){
        Field[] playerClassFields = player.getClass().get.getFields();
        ArrayList<Object> playerFieldsValues = new ArrayList<>();
        for (Field playerClassField : playerClassFields) {
            if (playerClassField.getAnnotation(PlayerField.class) != null)
                try {
                    playerFieldsValues.add(playerClassField.get(player));
                } catch (IllegalAccessException ignored) {
                }
        }
        System.out.println();
        return new SerializablePlayer(VERSION,playerFieldsValues.toArray());
    }
}
