package nodomain.knu2018.bandutils.devices.isens;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Collection;
import java.util.Collections;

import nodomain.knu2018.bandutils.GBException;
import nodomain.knu2018.bandutils.devices.AbstractDeviceCoordinator;
import nodomain.knu2018.bandutils.devices.InstallHandler;
import nodomain.knu2018.bandutils.devices.SampleProvider;
import nodomain.knu2018.bandutils.entities.DaoSession;
import nodomain.knu2018.bandutils.entities.Device;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.impl.GBDeviceCandidate;
import nodomain.knu2018.bandutils.model.ActivitySample;
import nodomain.knu2018.bandutils.model.DeviceType;


public class CareSenseCoordinator extends AbstractDeviceCoordinator {

    private static final String TAG = "CareSenseCoordinator";
    @Override
    protected void deleteDevice(@NonNull GBDevice gbDevice, @NonNull Device device, @NonNull DaoSession session) throws GBException {

    }

    @NonNull
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Collection<? extends ScanFilter> createBLEScanFilters() {
        ParcelUuid mi2Service = new ParcelUuid(CareSensConst.BLE_SERVICE_CUSTOM);
        ScanFilter filter = new ScanFilter.Builder().setServiceUuid(mi2Service).build();
        return Collections.singletonList(filter);
    }

    @NonNull
    @Override
    public DeviceType getSupportedType(GBDeviceCandidate candidate) {
        try {
            BluetoothDevice device = candidate.getDevice();
            String name = device.getName();
            if (name != null && name.equalsIgnoreCase(CareSensConst.CARE_SENS_N_NAME)){
                return DeviceType.CARESENSN;
            }
        }catch (Exception e){
            Log.e(TAG, "unable to check device support" + e);

        }
        return DeviceType.UNKNOWN;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.CARESENSN;
    }

    @Nullable
    @Override
    public Class<? extends Activity> getPairingActivity() {
        return null;
    }

    @Override
    public boolean supportsActivityDataFetching() {
        return true;
    }

    @Override
    public boolean supportsActivityTracking() {
        return true;
    }

    @Override
    public SampleProvider<? extends ActivitySample> getSampleProvider(GBDevice device, DaoSession session) {
        return null;
    }

    @Override
    public InstallHandler findInstallHandler(Uri uri, Context context) {
        return null;
    }

    @Override
    public boolean supportsScreenshots() {
        return false;
    }

    @Override
    public boolean supportsAlarmConfiguration() {
        return false;
    }

    @Override
    public boolean supportsSmartWakeup(GBDevice device) {
        return false;
    }

    @Override
    public boolean supportsHeartRateMeasurement(GBDevice device) {
        return false;
    }

    @Override
    public String getManufacturer() {
        return null;
    }

    @Override
    public boolean supportsAppsManagement() {
        return false;
    }

    @Override
    public Class<? extends Activity> getAppsManagementActivity() {
        return null;
    }

    @Override
    public boolean supportsCalendarEvents() {
        return false;
    }

    @Override
    public boolean supportsRealtimeData() {
        return false;
    }

    @Override
    public boolean supportsWeather() {
        return false;
    }

    @Override
    public boolean supportsFindDevice() {
        return false;
    }

//    @Override
//    public int getBondingStyle(GBDevice device) {
//        return super.getBondingStyle(device);
//    }
}
