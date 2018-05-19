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
package nodomain.knu2018.gadgetbridge.service.devices.amazfitbip.operations;

import android.support.annotation.NonNull;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nodomain.knu2018.gadgetbridge.devices.huami.amazfitbip.AmazfitBipService;
import nodomain.knu2018.gadgetbridge.devices.miband.MiBand2Service;
import nodomain.knu2018.gadgetbridge.service.btle.BLETypeConversions;
import nodomain.knu2018.gadgetbridge.service.btle.TransactionBuilder;
import nodomain.knu2018.gadgetbridge.service.btle.actions.WaitAction;
import nodomain.knu2018.gadgetbridge.service.devices.amazfitbip.AmazfitBipSupport;
import nodomain.knu2018.gadgetbridge.service.devices.miband2.operations.AbstractFetchOperation;
import nodomain.knu2018.gadgetbridge.util.FileUtils;
import nodomain.knu2018.gadgetbridge.util.GB;

public class AmazfitBipFetchLogsOperation extends AbstractFetchOperation {
    private static final Logger LOG = LoggerFactory.getLogger(AmazfitBipFetchLogsOperation.class);

    private FileOutputStream logOutputStream;

    public AmazfitBipFetchLogsOperation(AmazfitBipSupport support) {
        super(support);
        setName("fetch logs");
    }

    @Override
    protected void startFetching(TransactionBuilder builder) {
        File dir;
        try {
            dir = FileUtils.getExternalFilesDir();
        } catch (IOException e) {
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
        String filename = "amazfitbip_" + dateFormat.format(new Date()) + ".log";

        File outputFile = new File(dir, filename );
        try {
            logOutputStream = new FileOutputStream(outputFile);
        } catch (IOException e) {
            LOG.warn("could not create file " + outputFile, e);
            return;
        }

        GregorianCalendar sinceWhen = BLETypeConversions.createCalendar();
        sinceWhen.add(Calendar.DAY_OF_MONTH, -10);
        builder.write(characteristicFetch, BLETypeConversions.join(new byte[]{
                        MiBand2Service.COMMAND_ACTIVITY_DATA_START_DATE,
                        AmazfitBipService.COMMAND_ACTIVITY_DATA_TYPE_DEBUGLOGS},
                getSupport().getTimeBytes(sinceWhen, TimeUnit.MINUTES)));
        builder.add(new WaitAction(1000)); // TODO: actually wait for the success-reply
        builder.notify(characteristicActivityData, true);
        builder.write(characteristicFetch, new byte[]{MiBand2Service.COMMAND_FETCH_DATA});
    }

    @Override
    protected String getLastSyncTimeKey() {
        return null;
    }

    @Override
    protected void handleActivityFetchFinish(boolean success) {
        LOG.info(getName() +" data has finished");
        try {
            logOutputStream.close();
            logOutputStream = null;
        } catch (IOException e) {
            LOG.warn("could not close output stream", e);
            return;
        }
        super.handleActivityFetchFinish(success);
    }

    @Override
    protected void handleActivityNotif(byte[] value) {
        if (!isOperationRunning()) {
            LOG.error("ignoring notification because operation is not running. Data length: " + value.length);
            getSupport().logMessageContent(value);
            return;
        }

        if ((byte) (lastPacketCounter + 1) == value[0]) {
            lastPacketCounter++;
            bufferActivityData(value);
        } else {
            GB.toast("Error " + getName() + " invalid package counter: " + value[0], Toast.LENGTH_LONG, GB.ERROR);
            handleActivityFetchFinish(false);
        }
    }

    @Override
    protected void bufferActivityData(@NonNull byte[] value) {
        try {
            logOutputStream.write(value, 1, value.length - 1);
        } catch (IOException e) {
            LOG.warn("could not write to output stream", e);
            handleActivityFetchFinish(false);
        }
    }
}