package Game.Serialization;

import java.io.Serializable;

public class SerializablePlayer implements Serializable {
    private int version;
    private Object[] fields;

    public SerializablePlayer(int version, Object[] fields) {
        this.version = version;
        this.fields = fields;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Object[] getFields() {
        return fields;
    }

    public void setFields(Object[] fields) {
        this.fields = fields;
    }
}
