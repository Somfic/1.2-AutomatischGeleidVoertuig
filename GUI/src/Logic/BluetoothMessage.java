package Logic;

public class BluetoothMessage {
    private String type;

    private String value;

    public BluetoothMessage(String raw) {
        int index = raw.indexOf(':');
        if (index == -1) {
            type = raw;
            value = "";
        } else {
            type = raw.substring(0, index);
            value = raw.substring(index + 1);
        }
    }

    public BluetoothMessage(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }
}
