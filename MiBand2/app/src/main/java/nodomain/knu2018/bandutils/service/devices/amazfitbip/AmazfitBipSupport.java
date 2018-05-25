/*  Copyright (C) 2017-2018 Andreas Shimokawa, Carsten Pfeiffer

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
package nodomain.knu2018.bandutils.service.devices.amazfitbip;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.net.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.UUID;

import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.devices.huami.HuamiFWHelper;
import nodomain.knu2018.bandutils.devices.huami.HuamiWeatherConditions;
import nodomain.knu2018.bandutils.devices.huami.amazfitbip.AmazfitBipFWHelper;
import nodomain.knu2018.bandutils.devices.huami.amazfitbip.AmazfitBipService;
import nodomain.knu2018.bandutils.devices.miband.MiBand2Service;
import nodomain.knu2018.bandutils.model.CallSpec;
import nodomain.knu2018.bandutils.model.DeviceType;
import nodomain.knu2018.bandutils.model.NotificationSpec;
import nodomain.knu2018.bandutils.model.NotificationType;
import nodomain.knu2018.bandutils.model.RecordedDataTypes;
import nodomain.knu2018.bandutils.model.Weather;
import nodomain.knu2018.bandutils.model.WeatherSpec;
import nodomain.knu2018.bandutils.service.btle.TransactionBuilder;
import nodomain.knu2018.bandutils.service.btle.actions.ConditionalWriteAction;
import nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory;
import nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertNotificationProfile;
import nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.NewAlert;
import nodomain.knu2018.bandutils.service.devices.amazfitbip.operations.AmazfitBipFetchLogsOperation;
import nodomain.knu2018.bandutils.service.devices.huami.HuamiIcon;
import nodomain.knu2018.bandutils.service.devices.miband.NotificationStrategy;
import nodomain.knu2018.bandutils.service.devices.miband2.MiBand2Support;
import nodomain.knu2018.bandutils.service.devices.miband2.operations.FetchActivityOperation;
import nodomain.knu2018.bandutils.service.devices.miband2.operations.FetchSportsSummaryOperation;
import nodomain.knu2018.bandutils.util.Prefs;
import nodomain.knu2018.bandutils.util.StringUtils;
import nodomain.knu2018.bandutils.util.Version;

public class AmazfitBipSupport extends MiBand2Support {

    private static final Logger LOG = LoggerFactory.getLogger(AmazfitBipSupport.class);

    public AmazfitBipSupport() {
        super(LOG);
    }

    @Override
    public NotificationStrategy getNotificationStrategy() {
        return new AmazfitBipTextNotificationStrategy(this);
    }

    @Override
    public void onNotification(NotificationSpec notificationSpec) {
        if (notificationSpec.type == NotificationType.GENERIC_ALARM_CLOCK) {
            onAlarmClock(notificationSpec);
            return;
        }

        String senderOrTiltle = StringUtils.getFirstOf(notificationSpec.sender, notificationSpec.title);

        String message = StringUtils.truncate(senderOrTiltle, 32) + "\0";
        if (notificationSpec.subject != null) {
            message += StringUtils.truncate(notificationSpec.subject, 128) + "\n\n";
        }
        if (notificationSpec.body != null) {
            message += StringUtils.truncate(notificationSpec.body, 128);
        }

        try {
            TransactionBuilder builder = performInitialized("new notification");
            AlertNotificationProfile<?> profile = new AlertNotificationProfile(this);
            profile.setMaxLength(230);

            byte customIconId = HuamiIcon.mapToIconId(notificationSpec.type);

            AlertCategory alertCategory = AlertCategory.CustomHuami;

            // The SMS icon for AlertCategory.SMS is unique and not available as iconId
            if (notificationSpec.type == NotificationType.GENERIC_SMS) {
                alertCategory = AlertCategory.SMS;
            }
            // EMAIL icon does not work in FW 0.0.8.74, it did in 0.0.7.90
            else if (customIconId == HuamiIcon.EMAIL) {
                alertCategory = AlertCategory.Email;
            }

            NewAlert alert = new NewAlert(alertCategory, 1, message, customIconId);
            profile.newAlert(builder, alert);
            builder.queue(getQueue());
        } catch (IOException ex) {
            LOG.error("Unable to send notification to Amazfit Bip", ex);
        }
    }

    @Override
    public void onFindDevice(boolean start) {
        CallSpec callSpec = new CallSpec();
        callSpec.command = start ? CallSpec.CALL_INCOMING : CallSpec.CALL_END;
        callSpec.name = "Gadgetbridge";
        onSetCallState(callSpec);
    }

    @Override
    public void handleButtonEvent() {
        // ignore
    }

    @Override
    protected AmazfitBipSupport setDisplayItems(TransactionBuilder builder) {
        if (gbDevice.getType() != DeviceType.AMAZFITBIP) {
            return this; // Disable for Cor for now
        }
        if (gbDevice.getFirmwareVersion() == null) {
            LOG.warn("Device not initialized yet, won't set menu items");
            return this;
        }

        Version version = new Version(gbDevice.getFirmwareVersion());
        if (version.compareTo(new Version("0.1.1.14")) < 0) {
            LOG.warn("Won't set menu items since firmware is too low to be safe");
            return this;
        }

        Prefs prefs = GBApplication.getPrefs();
        Set<String> pages = prefs.getStringSet("bip_display_items", null);
        LOG.info("Setting display items to " + (pages == null ? "none" : pages));
        byte[] command = AmazfitBipService.COMMAND_CHANGE_SCREENS.clone();

        if (pages != null) {
            if (pages.contains("status")) {
                command[1] |= 0x02;
            }
            if (pages.contains("activity")) {
                command[1] |= 0x04;
            }
            if (pages.contains("weather")) {
                command[1] |= 0x08;
            }
            if (pages.contains("alarm")) {
                command[1] |= 0x10;
            }
            if (pages.contains("timer")) {
                command[1] |= 0x20;
            }
            if (pages.contains("compass")) {
                command[1] |= 0x40;
            }
            if (pages.contains("settings")) {
                command[1] |= 0x80;
            }
            if (pages.contains("alipay")) {
                command[2] |= 0x01;
            }
        }
        builder.write(getCharacteristic(MiBand2Service.UUID_CHARACTERISTIC_3_CONFIGURATION), command);
        return this;
    }

    @Override
    public void onSendWeather(WeatherSpec weatherSpec) {
        if (gbDevice.getFirmwareVersion() == null) {
            LOG.warn("Device not initialized yet, so not sending weather info");
            return;
        }
        boolean supportsConditionString = false;

        Version version = new Version(gbDevice.getFirmwareVersion());
        if (version.compareTo(new Version("0.0.8.74")) >= 0) {
            supportsConditionString = true;
        }
        int tz_offset_hours = SimpleTimeZone.getDefault().getOffset(weatherSpec.timestamp * 1000L) / (1000 * 60 * 60);
        try {
            TransactionBuilder builder;
            builder = performInitialized("Sending current temp");

            byte condition = HuamiWeatherConditions.mapToAmazfitBipWeatherCode(weatherSpec.currentConditionCode);

            int length = 8;
            if (supportsConditionString) {
                length += weatherSpec.currentCondition.getBytes().length + 1;
            }
            ByteBuffer buf = ByteBuffer.allocate(length);
            buf.order(ByteOrder.LITTLE_ENDIAN);

            buf.put((byte) 2);
            buf.putInt(weatherSpec.timestamp);
            buf.put((byte) (tz_offset_hours * 4));
            buf.put(condition);
            buf.put((byte) (weatherSpec.currentTemp - 273));

            if (supportsConditionString) {
                buf.put(weatherSpec.currentCondition.getBytes());
                buf.put((byte) 0);
            }

            builder.write(getCharacteristic(AmazfitBipService.UUID_CHARACTERISTIC_WEATHER), buf.array());
            builder.queue(getQueue());
        } catch (Exception ex) {
            LOG.error("Error sending current weather", ex);
        }

        if (gbDevice.getType() == DeviceType.AMAZFITBIP) {
            try {
                TransactionBuilder builder;
                builder = performInitialized("Sending air quality index");
                int length = 8;
                String aqiString = "(n/a)";
                if (supportsConditionString) {
                    length += aqiString.getBytes().length + 1;
                }
                ByteBuffer buf = ByteBuffer.allocate(length);
                buf.order(ByteOrder.LITTLE_ENDIAN);
                buf.put((byte) 4);
                buf.putInt(weatherSpec.timestamp);
                buf.put((byte) (tz_offset_hours * 4));
                buf.putShort((short) 0);
                if (supportsConditionString) {
                    buf.put(aqiString.getBytes());
                    buf.put((byte) 0);
                }
                builder.write(getCharacteristic(AmazfitBipService.UUID_CHARACTERISTIC_WEATHER), buf.array());
                builder.queue(getQueue());
            } catch (IOException ex) {
                LOG.error("Error sending air quality");
            }
        }

        try {
            TransactionBuilder builder = performInitialized("Sending weather forecast");

            final byte NR_DAYS = (byte) (1 + weatherSpec.forecasts.size());
            int bytesPerDay = 4;

            int conditionsLength = 0;
            if (supportsConditionString) {
                bytesPerDay = 5;
                conditionsLength = weatherSpec.currentCondition.getBytes().length;
                for (WeatherSpec.Forecast forecast : weatherSpec.forecasts) {
                    conditionsLength += Weather.getConditionString(forecast.conditionCode).getBytes().length;
                }
            }

            int length = 7 + bytesPerDay * NR_DAYS + conditionsLength;
            ByteBuffer buf = ByteBuffer.allocate(length);

            buf.order(ByteOrder.LITTLE_ENDIAN);
            buf.put((byte) 1);
            buf.putInt(weatherSpec.timestamp);
            buf.put((byte) (tz_offset_hours * 4));

            buf.put(NR_DAYS);

            byte condition = HuamiWeatherConditions.mapToAmazfitBipWeatherCode(weatherSpec.currentConditionCode);
            buf.put(condition);
            buf.put(condition);
            buf.put((byte) (weatherSpec.todayMaxTemp - 273));
            buf.put((byte) (weatherSpec.todayMinTemp - 273));
            if (supportsConditionString) {
                buf.put(weatherSpec.currentCondition.getBytes());
                buf.put((byte) 0);
            }

            for (WeatherSpec.Forecast forecast : weatherSpec.forecasts) {
                condition = HuamiWeatherConditions.mapToAmazfitBipWeatherCode(forecast.conditionCode);

                buf.put(condition);
                buf.put(condition);
                buf.put((byte) (forecast.maxTemp - 273));
                buf.put((byte) (forecast.minTemp - 273));
                if (supportsConditionString) {
                    buf.put(Weather.getConditionString(forecast.conditionCode).getBytes());
                    buf.put((byte) 0);
                }
            }

            builder.write(getCharacteristic(AmazfitBipService.UUID_CHARACTERISTIC_WEATHER), buf.array());
            builder.queue(getQueue());
        } catch (Exception ex) {
            LOG.error("Error sending weather forecast", ex);
        }

        if (gbDevice.getType() == DeviceType.AMAZFITCOR) {
            try {
                TransactionBuilder builder;
                builder = performInitialized("Sending forecast location");

                int length = 2 + weatherSpec.location.getBytes().length;
                ByteBuffer buf = ByteBuffer.allocate(length);
                buf.order(ByteOrder.LITTLE_ENDIAN);
                buf.put((byte) 8);
                buf.put(weatherSpec.location.getBytes());
                buf.put((byte) 0);

                builder.write(getCharacteristic(AmazfitBipService.UUID_CHARACTERISTIC_WEATHER), buf.array());
                builder.queue(getQueue());
            } catch (Exception ex) {
                LOG.error("Error sending current forecast location", ex);
            }
        }
    }

    @Override
    public void onFetchRecordedData(int dataTypes) {
        try {
            // FIXME: currently only one data type supported, these are meant to be flags
            if (dataTypes == RecordedDataTypes.TYPE_ACTIVITY) {
                new FetchActivityOperation(this).perform();
            } else if (dataTypes == RecordedDataTypes.TYPE_GPS_TRACKS) {
                new FetchSportsSummaryOperation(this).perform();
            } else if (dataTypes == RecordedDataTypes.TYPE_DEBUGLOGS) {
                new AmazfitBipFetchLogsOperation(this).perform();
            }
            else {
                LOG.warn("fetching multiple data types at once is not supported yet");
            }
        } catch (IOException ex) {
            LOG.error("Unable to fetch recorded data types" + dataTypes, ex);
        }
    }

    @Override
    public boolean onCharacteristicChanged(BluetoothGatt gatt,
                                           BluetoothGattCharacteristic characteristic) {
        boolean handled = super.onCharacteristicChanged(gatt, characteristic);
        if (!handled) {
            UUID characteristicUUID = characteristic.getUuid();
            if (MiBand2Service.UUID_CHARACTERISTIC_3_CONFIGURATION.equals(characteristicUUID)) {
                return handleConfigurationInfo(characteristic.getValue());
            }
        }
        return false;
    }

    private boolean handleConfigurationInfo(byte[] value) {
        if (value == null || value.length < 4) {
            return false;
        }
        if (value[0] == 0x10 && value[1] == 0x0e && value[2] == 0x01) {
            String gpsVersion = new String(value, 3, value.length - 3);
            LOG.info("got gps version = " + gpsVersion);
            gbDevice.setFirmwareVersion2(gpsVersion);
            return true;
        }
        return false;
    }

    // this probably does more than only getting the GPS version...
    private AmazfitBipSupport requestGPSVersion(TransactionBuilder builder) {
        LOG.info("Requesting GPS version");
        builder.write(getCharacteristic(MiBand2Service.UUID_CHARACTERISTIC_3_CONFIGURATION), AmazfitBipService.COMMAND_REQUEST_GPS_VERSION);
        return this;
    }

    private AmazfitBipSupport setLanguage(TransactionBuilder builder) {

        String language = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();

        LOG.info("Setting watch language, phone language = " + language + " country = " + country);

        final byte[] command_new;
        final byte[] command_old;
        String localeString;

        switch (GBApplication.getPrefs().getInt("amazfitbip_language", -1)) {
            case 0:
                command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_SIMPLIFIED_CHINESE;
                localeString = "zh_CN";
                break;
            case 1:
                command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_TRADITIONAL_CHINESE;
                localeString = "zh_TW";
                break;
            case 2:
                command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_ENGLISH;
                localeString = "en_US";
                break;
            case 3:
                command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_SPANISH;
                localeString = "es_ES";
                break;
            default:
                switch (language) {
                    case "zh":
                        if (country.equals("TW") || country.equals("HK") || country.equals("MO")) { // Taiwan, Hong Kong,  Macao
                            command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_TRADITIONAL_CHINESE;
                            localeString = "zh_TW";
                        } else {
                            command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_SIMPLIFIED_CHINESE;
                            localeString = "zh_CN";
                        }
                        break;
                    case "es":
                        command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_SPANISH;
                        localeString = "es_ES";
                        break;
                    default:
                        command_old = AmazfitBipService.COMMAND_SET_LANGUAGE_ENGLISH;
                        localeString = "en_US";
                        break;
                }
        }
        command_new = AmazfitBipService.COMMAND_SET_LANGUAGE_NEW_TEMPLATE;
        System.arraycopy(localeString.getBytes(), 0, command_new, 3, localeString.getBytes().length);

        builder.add(new ConditionalWriteAction(getCharacteristic(MiBand2Service.UUID_CHARACTERISTIC_3_CONFIGURATION)) {
            @Override
            protected byte[] checkCondition() {
                if (gbDevice.getType() == DeviceType.AMAZFITBIP && new Version(gbDevice.getFirmwareVersion()).compareTo(new Version("0.1.0.77")) >= 0) {
                    return command_new;
                } else {
                    return command_old;
                }
            }
        });

        return this;
    }


    @Override
    public void phase2Initialize(TransactionBuilder builder) {
        super.phase2Initialize(builder);
        LOG.info("phase2Initialize...");
        setLanguage(builder);
        requestGPSVersion(builder);
    }

    @Override
    public HuamiFWHelper createFWHelper(Uri uri, Context context) throws IOException {
        return new AmazfitBipFWHelper(uri, context);
    }
}
