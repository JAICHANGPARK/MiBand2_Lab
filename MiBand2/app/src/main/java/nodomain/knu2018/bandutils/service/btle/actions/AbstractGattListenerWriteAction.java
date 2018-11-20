package nodomain.knu2018.bandutils.service.btle.actions;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Objects;

import nodomain.knu2018.bandutils.service.btle.AbstractGattCallback;
import nodomain.knu2018.bandutils.service.btle.BtLEQueue;
import nodomain.knu2018.bandutils.service.btle.GattCallback;
import nodomain.knu2018.bandutils.service.btle.GattListenerAction;

public abstract class AbstractGattListenerWriteAction extends nodomain.knu2018.bandutils.service.btle.actions.WriteAction implements GattListenerAction {
    private final BtLEQueue queue;

    public AbstractGattListenerWriteAction(BtLEQueue queue, BluetoothGattCharacteristic characteristic, byte[] value) {
        super(characteristic, value);
        this.queue = queue;
        Objects.requireNonNull(queue, "queue must not be null");
    }

    @Override
    public GattCallback getGattCallback() {
        return new AbstractGattCallback() {
            @Override
            public boolean onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                return AbstractGattListenerWriteAction.this.onCharacteristicChanged(gatt, characteristic);
            }
        };
    }

    protected abstract boolean onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
}
