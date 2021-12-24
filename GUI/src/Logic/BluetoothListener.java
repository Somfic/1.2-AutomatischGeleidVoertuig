package Logic;

public interface BluetoothListener {
    void onBluetoothMessage(BluetoothMessage message);

    void onBluetoothOpened(boolean isOpen);
}