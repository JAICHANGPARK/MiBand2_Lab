/*  Copyright (C) 2015-2018 Andreas Shimokawa, Carsten Pfeiffer, Daniele
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
package nodomain.knu2018.bandutils.devices;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import nodomain.knu2018.bandutils.GBException;
import nodomain.knu2018.bandutils.activities.ControlCenterv2;
import nodomain.knu2018.bandutils.entities.AbstractActivitySample;
import nodomain.knu2018.bandutils.entities.DaoSession;
import nodomain.knu2018.bandutils.entities.Device;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.impl.GBDeviceCandidate;
import nodomain.knu2018.bandutils.model.ActivityKind;
import nodomain.knu2018.bandutils.model.DeviceType;

public class UnknownDeviceCoordinator extends AbstractDeviceCoordinator {
    private final UnknownSampleProvider sampleProvider;

    private static final class UnknownSampleProvider implements SampleProvider {
        @Override
        public int normalizeType(int rawType) {
            return ActivityKind.TYPE_UNKNOWN;
        }

        @Override
        public int toRawActivityKind(int activityKind) {
            return 0;
        }

        @Override
        public float normalizeIntensity(int rawIntensity) {
            return 0;
        }

        @Override
        public List getAllActivitySamples(int timestamp_from, int timestamp_to) {
            return null;
        }

        @Override
        public List getActivitySamples(int timestamp_from, int timestamp_to) {
            return null;
        }

        @Override
        public List getSleepSamples(int timestamp_from, int timestamp_to) {
            return null;
        }

        @Override
        public void addGBActivitySample(AbstractActivitySample activitySample) {
        }

        @Override
        public void addGBActivitySamples(AbstractActivitySample[] activitySamples) {
        }

        @Override
        public AbstractActivitySample createActivitySample() {
            return null;
        }

        @Nullable
        @Override
        public AbstractActivitySample getLatestActivitySample() {
            return null;
        }
    }

    public UnknownDeviceCoordinator() {
        sampleProvider = new UnknownSampleProvider();
    }

    @Override
    public DeviceType getSupportedType(GBDeviceCandidate candidate) {
        return DeviceType.UNKNOWN;
    }

    @Override
    protected void deleteDevice(@NonNull GBDevice gbDevice, @NonNull Device device, @NonNull DaoSession session) throws GBException {
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.UNKNOWN;
    }

    @Override
    public Class<? extends Activity> getPairingActivity() {
        return ControlCenterv2.class;
    }

    @Override
    public SampleProvider<?> getSampleProvider(GBDevice device, DaoSession session) {
        return new UnknownSampleProvider();
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
        return "unknown";
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
}