/*  Copyright (C) 2015-2018 0nse, Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti, João Paulo Barraca, ladbsoft, protomors, Quallenauge, Sami Alaoui

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package nodomain.knu2018.bandutils.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.GBException;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.database.DBHandler;
import nodomain.knu2018.bandutils.database.DBHelper;
import nodomain.knu2018.bandutils.devices.DeviceCoordinator;
import nodomain.knu2018.bandutils.devices.UnknownDeviceCoordinator;
import nodomain.knu2018.bandutils.devices.hplus.EXRIZUK8Coordinator;
import nodomain.knu2018.bandutils.devices.hplus.HPlusCoordinator;
import nodomain.knu2018.bandutils.devices.hplus.MakibesF68Coordinator;
import nodomain.knu2018.bandutils.devices.huami.amazfitbip.AmazfitBipCoordinator;
import nodomain.knu2018.bandutils.devices.huami.amazfitcor.AmazfitCorCoordinator;
import nodomain.knu2018.bandutils.devices.huami.miband2.MiBand2Coordinator;
import nodomain.knu2018.bandutils.devices.huami.miband2.MiBand2HRXCoordinator;
import nodomain.knu2018.bandutils.devices.jyou.TeclastH30Coordinator;
import nodomain.knu2018.bandutils.devices.liveview.LiveviewCoordinator;
import nodomain.knu2018.bandutils.devices.miband.MiBandConst;
import nodomain.knu2018.bandutils.devices.miband.MiBandCoordinator;
import nodomain.knu2018.bandutils.devices.no1f1.No1F1Coordinator;
import nodomain.knu2018.bandutils.devices.pebble.PebbleCoordinator;
import nodomain.knu2018.bandutils.devices.vibratissimo.VibratissimoCoordinator;
import nodomain.knu2018.bandutils.devices.xwatch.XWatchCoordinator;
import nodomain.knu2018.bandutils.entities.Device;
import nodomain.knu2018.bandutils.entities.DeviceAttributes;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.impl.GBDeviceCandidate;
import nodomain.knu2018.bandutils.model.DeviceType;

public class DeviceHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceHelper.class);

    private static final DeviceHelper instance = new DeviceHelper();

    public static DeviceHelper getInstance() {
        return instance;
    }

    // lazily created
    private List<DeviceCoordinator> coordinators;

    public DeviceType getSupportedType(GBDeviceCandidate candidate) {
        for (DeviceCoordinator coordinator : getAllCoordinators()) {
            DeviceType deviceType = coordinator.getSupportedType(candidate);
            if (deviceType.isSupported()) {
                return deviceType;
            }
        }
        return DeviceType.UNKNOWN;
    }

    public boolean getSupportedType(GBDevice device) {
        for (DeviceCoordinator coordinator : getAllCoordinators()) {
            if (coordinator.supports(device)) {
                return true;
            }
        }
        return false;
    }

    public GBDevice findAvailableDevice(String deviceAddress, Context context) {
        Set<GBDevice> availableDevices = getAvailableDevices(context);
        for (GBDevice availableDevice : availableDevices) {
            if (deviceAddress.equals(availableDevice.getAddress())) {
                return availableDevice;
            }
        }
        return null;
    }

    /**
     * Returns the list of all available devices that are supported by Gadgetbridge.
     * Note that no state is known about the returned devices. Even if one of those
     * devices is connected, it will report the default not-connected state.
     *
     * Clients interested in the "live" devices being managed should use the class
     * DeviceManager.
     * @param context
     * @return
     */
    public Set<GBDevice> getAvailableDevices(Context context) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<GBDevice> availableDevices = new LinkedHashSet<GBDevice>();

        if (btAdapter == null) {
            GB.toast(context, context.getString(R.string.bluetooth_is_not_supported_), Toast.LENGTH_SHORT, GB.WARN);
        } else if (!btAdapter.isEnabled()) {
            GB.toast(context, context.getString(R.string.bluetooth_is_disabled_), Toast.LENGTH_SHORT, GB.WARN);
        }
        List<GBDevice> dbDevices = getDatabaseDevices();
        // these come first, as they have the most information already
        availableDevices.addAll(dbDevices);
        if (btAdapter != null) {
            List<GBDevice> bondedDevices = getBondedDevices(btAdapter);
            availableDevices.addAll(bondedDevices);
        }

        Prefs prefs = GBApplication.getPrefs();
        String miAddr = prefs.getString(MiBandConst.PREF_MIBAND_ADDRESS, "");
        if (miAddr.length() > 0) {
            GBDevice miDevice = new GBDevice(miAddr, "MI", DeviceType.MIBAND);
            availableDevices.add(miDevice);
        }

        String pebbleEmuAddr = prefs.getString("pebble_emu_addr", "");
        String pebbleEmuPort = prefs.getString("pebble_emu_port", "");
        if (pebbleEmuAddr.length() >= 7 && pebbleEmuPort.length() > 0) {
            GBDevice pebbleEmuDevice = new GBDevice(pebbleEmuAddr + ":" + pebbleEmuPort, "Pebble qemu", DeviceType.PEBBLE);
            availableDevices.add(pebbleEmuDevice);
        }
        return availableDevices;
    }

    public GBDevice toSupportedDevice(BluetoothDevice device) {
        GBDeviceCandidate candidate = new GBDeviceCandidate(device, GBDevice.RSSI_UNKNOWN, device.getUuids());
        return toSupportedDevice(candidate);
    }

    public GBDevice toSupportedDevice(GBDeviceCandidate candidate) {
        for (DeviceCoordinator coordinator : getAllCoordinators()) {
            if (coordinator.supports(candidate)) {
                return coordinator.createDevice(candidate);
            }
        }
        return null;
    }

    public DeviceCoordinator getCoordinator(GBDeviceCandidate device) {
        synchronized (this) {
            for (DeviceCoordinator coord : getAllCoordinators()) {
                if (coord.supports(device)) {
                    return coord;
                }
            }
        }
        return new UnknownDeviceCoordinator();
    }

    public DeviceCoordinator getCoordinator(GBDevice device) {
        synchronized (this) {
            for (DeviceCoordinator coord : getAllCoordinators()) {
                if (coord.supports(device)) {
                    return coord;
                }
            }
        }
        return new UnknownDeviceCoordinator();
    }

    public synchronized List<DeviceCoordinator> getAllCoordinators() {
        if (coordinators == null) {
            coordinators = createCoordinators();
        }
        return coordinators;
    }

    private List<DeviceCoordinator> createCoordinators() {
        List<DeviceCoordinator> result = new ArrayList<>();
        result.add(new AmazfitBipCoordinator()); // Note: must come before MiBand2 because detection is hacky, atm
        result.add(new AmazfitCorCoordinator()); // Note: must come before MiBand2 because detection is hacky, atm
        result.add(new MiBand2HRXCoordinator()); // Note: must come before MiBand2 because detection is hacky, atm
        result.add(new MiBand2Coordinator()); // Note: MiBand2 must come before MiBand because detection is hacky, atm
        result.add(new MiBandCoordinator());
        result.add(new PebbleCoordinator());
        result.add(new VibratissimoCoordinator());
        result.add(new LiveviewCoordinator());
        result.add(new HPlusCoordinator());
        result.add(new No1F1Coordinator());
        result.add(new MakibesF68Coordinator());
        result.add(new EXRIZUK8Coordinator());
        result.add(new TeclastH30Coordinator());
        result.add(new XWatchCoordinator());

        return result;
    }

    private List<GBDevice> getDatabaseDevices() {
        List<GBDevice> result = new ArrayList<>();
        try (DBHandler lockHandler = GBApplication.acquireDB()) {
            List<Device> activeDevices = DBHelper.getActiveDevices(lockHandler.getDaoSession());
            for (Device dbDevice : activeDevices) {
                GBDevice gbDevice = toGBDevice(dbDevice);
                if (gbDevice != null && DeviceHelper.getInstance().getSupportedType(gbDevice)) {
                    result.add(gbDevice);
                }
            }
            return result;

        } catch (Exception e) {
            GB.toast("Error retrieving devices from database", Toast.LENGTH_SHORT, GB.ERROR);
            return Collections.emptyList();
        }
    }

    /**
     * Converts a known device from the database to a GBDevice.
     * Note: The device might not be supported anymore, so callers should verify that.
     * @param dbDevice
     * @return
     */
    public GBDevice toGBDevice(Device dbDevice) {
        DeviceType deviceType = DeviceType.fromKey(dbDevice.getType());
        GBDevice gbDevice = new GBDevice(dbDevice.getIdentifier(), dbDevice.getName(), deviceType);
        List<DeviceAttributes> deviceAttributesList = dbDevice.getDeviceAttributesList();
        if (deviceAttributesList.size() > 0) {
            gbDevice.setModel(dbDevice.getModel());
            DeviceAttributes attrs = deviceAttributesList.get(0);
            gbDevice.setFirmwareVersion(attrs.getFirmwareVersion1());
            gbDevice.setFirmwareVersion2(attrs.getFirmwareVersion2());
            gbDevice.setVolatileAddress(attrs.getVolatileIdentifier());
        }

        return gbDevice;
    }

    private @NonNull List<GBDevice> getBondedDevices(@NonNull BluetoothAdapter btAdapter) {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices == null) {
            return Collections.emptyList();
        }

        List<GBDevice> result = new ArrayList<>(pairedDevices.size());
        DeviceHelper deviceHelper = DeviceHelper.getInstance();
        for (BluetoothDevice pairedDevice : pairedDevices) {
            if (pairedDevice == null) {
                continue; // just to be safe, see https://github.com/knu2018/Gadgetbridge/pull/1052
            }
            if (pairedDevice.getName() != null && (pairedDevice.getName().startsWith("Pebble-LE ") || pairedDevice.getName().startsWith("Pebble Time LE "))) {
                continue; // ignore LE Pebble (this is part of the main device now (volatileAddress)
            }
            GBDevice device = deviceHelper.toSupportedDevice(pairedDevice);
            if (device != null) {
                result.add(device);
            }
        }
        return result;
    }

    /**
     * Attempts to removing the bonding with the given device. Returns true
     * if bonding was supposedly successful and false if anything went wrong
     * @param device
     * @return
     */
    public boolean removeBond(GBDevice device) throws GBException {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            BluetoothDevice remoteDevice = defaultAdapter.getRemoteDevice(device.getAddress());
            if (remoteDevice != null) {
                try {
                    Method method = BluetoothDevice.class.getMethod("removeBond", (Class[]) null);
                    Object result = method.invoke(remoteDevice, (Object[]) null);
                    return Boolean.TRUE.equals(result);
                } catch (Exception e) {
                    throw new GBException("Error removing bond to device: " + device, e);
                }
            }
        }
        return false;
    }

}
