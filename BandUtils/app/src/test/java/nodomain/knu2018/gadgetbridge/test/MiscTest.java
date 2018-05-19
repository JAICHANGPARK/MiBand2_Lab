package nodomain.knu2018.gadgetbridge.test;

import org.junit.Assert;
import org.junit.Test;

import nodomain.knu2018.gadgetbridge.service.btle.GattCharacteristic;

public class MiscTest extends TestBase {
    @Test
    public void testGattCharacteristic() {
        String desc = GattCharacteristic.lookup(GattCharacteristic.UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT, "xxx");
        Assert.assertEquals("heart rate control point", desc);
    }
}
