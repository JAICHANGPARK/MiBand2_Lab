/*  Copyright (C) 2017-2018 Andreas Shimokawa

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
package nodomain.knu2018.bandutils.service.devices.huami.amazfitbip;

import android.support.annotation.NonNull;

import nodomain.knu2018.bandutils.service.btle.BtLEAction;
import nodomain.knu2018.bandutils.service.btle.TransactionBuilder;
import nodomain.knu2018.bandutils.devices.miband.VibrationProfile;
import nodomain.knu2018.bandutils.service.devices.common.SimpleNotification;
import nodomain.knu2018.bandutils.service.devices.huami.HuamiSupport;
import nodomain.knu2018.bandutils.service.devices.huami.miband2.Mi2TextNotificationStrategy;


// This class in no longer in use except for incoming calls
class AmazfitBipTextNotificationStrategy extends Mi2TextNotificationStrategy {

    AmazfitBipTextNotificationStrategy(HuamiSupport support) {
        super(support);
    }

    @Override
    protected void sendCustomNotification(VibrationProfile vibrationProfile, SimpleNotification simpleNotification, BtLEAction extraAction, TransactionBuilder builder) {
        if (simpleNotification != null) {
            sendAlert(simpleNotification, builder);
        }
    }

    @Override
    protected void sendAlert(@NonNull SimpleNotification simpleNotification, TransactionBuilder builder) {
        nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertNotificationProfile<?> profile = new nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertNotificationProfile<>(getSupport());
        profile.setMaxLength(255); // TODO: find out real limit, certainly it is more than 18 which is default

        nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory category = simpleNotification.getAlertCategory();
        switch (simpleNotification.getAlertCategory()) {
            // only these are confirmed working so far on Amazfit Bip
            case Email:
            case IncomingCall:
            case SMS:
                break;
            // default to SMS for non working categories
            default:
                category = nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.AlertCategory.SMS;
        }
        nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.NewAlert alert = new nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.NewAlert(category, 1, simpleNotification.getMessage());
        profile.newAlert(builder, alert, nodomain.knu2018.bandutils.service.btle.profiles.alertnotification.OverflowStrategy.TRUNCATE);
    }
}
