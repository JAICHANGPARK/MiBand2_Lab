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
package nodomain.knu2018.bandutils.service.devices.huami.miband2;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;

import nodomain.knu2018.bandutils.devices.miband.VibrationProfile;
import nodomain.knu2018.bandutils.service.btle.BLETypeConversions;
import nodomain.knu2018.bandutils.service.btle.BtLEAction;
import nodomain.knu2018.bandutils.service.btle.GattCharacteristic;
import nodomain.knu2018.bandutils.service.btle.TransactionBuilder;
import nodomain.knu2018.bandutils.service.devices.common.SimpleNotification;
import nodomain.knu2018.bandutils.service.devices.huami.HuamiIcon;
import nodomain.knu2018.bandutils.service.devices.huami.HuamiSupport;
import nodomain.knu2018.bandutils.util.StringUtils;


public class Mi2TextNotificationStrategy extends Mi2NotificationStrategy {
    private final BluetoothGattCharacteristic newAlertCharacteristic;

    public Mi2TextNotificationStrategy(HuamiSupport support) {
        super(support);
        newAlertCharacteristic = support.getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_NEW_ALERT);
    }

    @Override
    protected void sendCustomNotification(VibrationProfile vibrationProfile, SimpleNotification simpleNotification, BtLEAction extraAction, TransactionBuilder builder) {
        if (simpleNotification != null && simpleNotification.getAlertCategory() == nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.IncomingCall) {
            // incoming calls are notified solely via NewAlert including caller ID
            sendAlert(simpleNotification, builder);
            return;
        }

        // announce text messages with configured alerts first
        super.sendCustomNotification(vibrationProfile, simpleNotification, extraAction, builder);
        // and finally send the text message, if any
        if (simpleNotification != null && !StringUtils.isEmpty(simpleNotification.getMessage())) {
            sendAlert(simpleNotification, builder);
        }
    }

    @Override
    protected void startNotify(TransactionBuilder builder, int alertLevel, SimpleNotification simpleNotification) {
        builder.write(newAlertCharacteristic, getNotifyMessage(simpleNotification));
    }

    protected byte[] getNotifyMessage(SimpleNotification simpleNotification) {
        int numAlerts = 1;
        if (simpleNotification != null && simpleNotification.getNotificationType() != null && simpleNotification.getAlertCategory() != nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.SMS) {
            byte customIconId = HuamiIcon.mapToIconId(simpleNotification.getNotificationType());
            if (customIconId == HuamiIcon.EMAIL) {
                // unfortunately. the email icon breaks the notification, fall back to a standard AlertCategory
                return new byte[]{BLETypeConversions.fromUint8(nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.Email.getId()), BLETypeConversions.fromUint8(numAlerts)};
            }
            return new byte[]{BLETypeConversions.fromUint8(nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.CustomHuami.getId()), BLETypeConversions.fromUint8(numAlerts), customIconId};
        }
        return new byte[] { BLETypeConversions.fromUint8(nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.SMS.getId()), BLETypeConversions.fromUint8(numAlerts)};
    }

    protected void sendAlert(@NonNull SimpleNotification simpleNotification, TransactionBuilder builder) {
        nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertNotificationProfile<?> profile = new nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertNotificationProfile<>(getSupport());
        // override the alert category,  since only SMS and incoming call support text notification
        nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory category = nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.SMS;
        if (simpleNotification.getAlertCategory() == nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.IncomingCall) {
            category = simpleNotification.getAlertCategory();
        }
        nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.NewAlert alert = new nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.NewAlert(category, 1, simpleNotification.getMessage());
        profile.newAlert(builder, alert, nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.OverflowStrategy.MAKE_MULTIPLE);
    }
}
