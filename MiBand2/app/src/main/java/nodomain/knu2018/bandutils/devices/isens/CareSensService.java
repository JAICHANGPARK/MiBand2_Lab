package nodomain.knu2018.bandutils.devices.isens;

import java.util.UUID;

import static nodomain.knu2018.bandutils.service.btle.AbstractBTLEDeviceSupport.BASE_UUID;

public class CareSensService {

    public static final UUID UUID_SERVICE_CARESENS_SERVICE = UUID.fromString(String.format(BASE_UUID, "FFF0"));

}
