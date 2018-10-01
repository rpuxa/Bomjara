package Game.Serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

import Game.Player;

public class SerializationProcess {

    private static final int VERSION = 0;


    public static SerializablePlayer serialization(Player player){
        Field[] playerClassFields = player.getClass().getFields();
        ArrayList<Object> playerFieldsValues = new ArrayList<>();
        for (Field playerClassField : playerClassFields) {
            if (playerClassField.getAnnotation(PlayerField.class) != null)
                try {
                    playerFieldsValues.add(playerClassField.get(player));
                } catch (IllegalAccessException ignored) {
                }
        }
        return new SerializablePlayer(VERSION,playerFieldsValues.toArray());
    }

    public static Player deserialization(SerializablePlayer player) throws VersionException {
        int version = player.getVersion();
        for (Method method : SerializationProcess.class.getMethods()) {
            if (Objects.equals(method.getName(), "deserializationVersion" + version)){
                try {
                    return (Player) method.invoke(null,(Object) player.getFields());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new VersionException("Your save was done in a later version. Please update");
    }

    public static Player deserializationVersion0(Object[] fields){
        return new Player(fields);
    }
}
