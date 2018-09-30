package nodomain.knu2018.bandutils.devices.isens;

import java.util.UUID;

public class CareSensConst {

//    public static final String CARE_SENS_N_NAME = "CareSens";
    public static final String CARE_SENS_N_NAME = "CareSens 0701";

    private static final int FLAGS_BIT = 0x01;
    private static final int SERVICES_MORE_AVAILABLE_16_BIT = 0x02;
    private static final int SERVICES_COMPLETE_LIST_16_BIT = 0x03;
    private static final int SERVICES_MORE_AVAILABLE_32_BIT = 0x04;
    private static final int SERVICES_COMPLETE_LIST_32_BIT = 0x05;
    private static final int SERVICES_MORE_AVAILABLE_128_BIT = 0x06;
    private static final int SERVICES_COMPLETE_LIST_128_BIT = 0x07;
    private static final int SHORTENED_LOCAL_NAME = 0x08;
    private static final int COMPLETE_LOCAL_NAME = 0x09;

    private static final byte LE_LIMITED_DISCOVERABLE_MODE = 0x01;
    private static final byte LE_GENERAL_DISCOVERABLE_MODE = 0x02;


    public final static UUID BLE_SERVICE_CUSTOM_TIME_MC	= UUID.fromString("11223344-5566-7788-9900-AABBCCDDEEFF");

    //Service
    public final static UUID BLE_SERVICE_GLUCOSE		= UUID.fromString("00001808-0000-1000-8000-00805f9b34fb");
    public final static UUID BLE_SERVICE_DEVICE_INFO	= UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    public final static UUID BLE_SERVICE_CUSTOM     	= UUID.fromString("0000FFF0-0000-1000-8000-00805f9b34fb");
    //Characteristic
    public final static UUID BLE_CHAR_GLUCOSE_SERIALNUM	= UUID.fromString("00002A25-0000-1000-8000-00805f9b34fb");
    public final static UUID BLE_CHAR_SOFTWARE_REVISION	= UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb");
    public final static UUID BLE_CHAR_GLUCOSE_MEASUREMENT= UUID.fromString("00002A18-0000-1000-8000-00805f9b34fb");
    public final static UUID BLE_CHAR_GLUCOSE_CONTEXT	= UUID.fromString("00002A34-0000-1000-8000-00805f9b34fb");
    public final static UUID BLE_CHAR_GLUCOSE_RACP		= UUID.fromString("00002A52-0000-1000-8000-00805f9b34fb");
    public final static UUID BLE_CHAR_CUSTOM_TIME	    = UUID.fromString("0000FFF1-0000-1000-8000-00805f9b34fb");
    //Descriptor
    public final static UUID BLE_DESCRIPTOR_DESCRIPTOR	= UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

}
