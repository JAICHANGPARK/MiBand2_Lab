/*  Copyright (C) 2016-2018 Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti

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
package nodomain.knu2018.bandutils.devices.vibratissimo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import nodomain.knu2018.bandutils.GBException;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.VibrationActivity;
import nodomain.knu2018.bandutils.devices.AbstractDeviceCoordinator;
import nodomain.knu2018.bandutils.devices.InstallHandler;
import nodomain.knu2018.bandutils.devices.SampleProvider;
import nodomain.knu2018.bandutils.entities.DaoSession;
import nodomain.knu2018.bandutils.entities.Device;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.impl.GBDeviceCandidate;
import nodomain.knu2018.bandutils.model.ActivitySample;
import nodomain.knu2018.bandutils.model.DeviceType;

public class VibratissimoCoordinator extends AbstractDeviceCoordinator {
    @Override
    public DeviceType getSupportedType(GBDeviceCandidate candidate) {
        String name = candidate.getDevice().getName();
        if (name != null && name.startsWith("Vibratissimo")) {
            return DeviceType.VIBRATISSIMO;
        }
        return DeviceType.UNKNOWN;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.VIBRATISSIMO;
    }

    @Override
    public Class<? extends Activity> getPairingActivity() {
        return null;
    }

    @Override
    public InstallHandler findInstallHandler(Uri uri, Context context) {
        return null;
    }

    @Override
    public boolean supportsActivityDataFetching() {
        return false;
    }

    @Override
    public boolean supportsActivityTracking() {
        return false;
    }

    @Override
    public SampleProvider<? extends ActivitySample> getSampleProvider(GBDevice device, DaoSession session) {
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
        return "Amor AG";
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
        return false; // hmmm well, it has a temperature sensor :D
    }

    @Override
    public boolean supportsWeather() {
        return false;
    }

    @Override
    protected void deleteDevice(@NonNull GBDevice gbDevice, @NonNull Device device, @NonNull DaoSession session) throws GBException {
        // nothing to delete, yet
    }
}
