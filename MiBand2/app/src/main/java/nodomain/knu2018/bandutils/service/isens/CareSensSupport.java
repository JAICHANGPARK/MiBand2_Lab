package nodomain.knu2018.bandutils.service.isens;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import nodomain.knu2018.bandutils.devices.isens.CareSensConst;
import nodomain.knu2018.bandutils.devices.isens.GlucoseRecord;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.model.Alarm;
import nodomain.knu2018.bandutils.model.CalendarEventSpec;
import nodomain.knu2018.bandutils.model.CallSpec;
import nodomain.knu2018.bandutils.model.CannedMessagesSpec;
import nodomain.knu2018.bandutils.model.MusicSpec;
import nodomain.knu2018.bandutils.model.MusicStateSpec;
import nodomain.knu2018.bandutils.model.NotificationSpec;
import nodomain.knu2018.bandutils.model.WeatherSpec;
import nodomain.knu2018.bandutils.service.btle.AbstractBTLEDeviceSupport;
import nodomain.knu2018.bandutils.service.btle.GattService;
import nodomain.knu2018.bandutils.service.btle.TransactionBuilder;
import nodomain.knu2018.bandutils.service.btle.actions.SetDeviceStateAction;
import nodomain.knu2018.bandutils.service.btle.profiles.deviceinfo.DeviceInfoProfile;
import nodomain.knu2018.bandutils.util.GB;

public class CareSensSupport extends AbstractBTLEDeviceSupport {
    private static final Logger LOG = LoggerFactory.getLogger(CareSensSupport.class);
    private static final String TAG = "CareSensSupport";
    private final DeviceInfoProfile<CareSensSupport> deviceInfoProfile;

    private final static int OP_CODE_REPORT_STORED_RECORDS = 1;
    private final static int OP_CODE_DELETE_STORED_RECORDS = 2;
    private final static int OP_CODE_REPORT_NUMBER_OF_RECORDS = 4;
    private final static int OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE = 5;
    private final static int OP_CODE_RESPONSE_CODE = 6;
    private final static int COMPLETE_RESULT_FROM_METER = 192;

    private final static int OPERATOR_ALL_RECORDS = 1;
    private final static int OPERATOR_GREATER_THAN_EQUAL = 3;

    private final static int FILTER_TYPE_SEQUENCE_NUMBER = 1;

    private final static int RESPONSE_SUCCESS = 1;
    private final static int RESPONSE_OP_CODE_NOT_SUPPORTED = 2;
    private final static int RESPONSE_NO_RECORDS_FOUND = 6;
    private final static int RESPONSE_ABORT_UNSUCCESSFUL = 7;
    private final static int RESPONSE_PROCEDURE_NOT_COMPLETED = 8;

    public BluetoothGattCharacteristic mGlucoseMeasurementCharacteristic = null;
    public BluetoothGattCharacteristic mGlucoseMeasurementContextCharacteristic = null;
    public BluetoothGattCharacteristic mRACPCharacteristic = null;
    public BluetoothGattCharacteristic mDeviceSoftwareRevisionCharacteristic = null;
    public BluetoothGattCharacteristic mCustomTimeCharacteristic = null;
    public BluetoothGattCharacteristic mDeviceSerialCharacteristic = null;

    private final SparseArray<GlucoseRecord> mRecords = new SparseArray<GlucoseRecord>();

    public CareSensSupport() {
        super(LOG);
        addSupportedService(GattService.UUID_SERVICE_GENERIC_ACCESS);
        addSupportedService(GattService.UUID_SERVICE_GENERIC_ATTRIBUTE);
        addSupportedService(CareSensConst.BLE_SERVICE_GLUCOSE);
        addSupportedService(CareSensConst.BLE_SERVICE_CUSTOM_TIME_MC);
        addSupportedService(CareSensConst.BLE_SERVICE_DEVICE_INFO);
        addSupportedService(CareSensConst.BLE_SERVICE_CUSTOM);

        deviceInfoProfile = new DeviceInfoProfile<>(this);
        addSupportedProfile(deviceInfoProfile);
    }

    @Override
    protected TransactionBuilder initializeDevice(TransactionBuilder builder) {

        Log.e(TAG, "initializeDevice: init");
        builder.add(new SetDeviceStateAction(getDevice(), GBDevice.State.INITIALIZING, getContext()));
        mGlucoseMeasurementCharacteristic = getCharacteristic(CareSensConst.BLE_CHAR_GLUCOSE_MEASUREMENT);
        mGlucoseMeasurementContextCharacteristic = getCharacteristic(CareSensConst.BLE_CHAR_GLUCOSE_CONTEXT);
        mRACPCharacteristic = getCharacteristic(CareSensConst.BLE_CHAR_GLUCOSE_RACP);
        mDeviceSerialCharacteristic = getCharacteristic(CareSensConst.BLE_CHAR_GLUCOSE_SERIALNUM);
        mDeviceSoftwareRevisionCharacteristic = getCharacteristic(CareSensConst.BLE_CHAR_SOFTWARE_REVISION);
        mCustomTimeCharacteristic = getCharacteristic(CareSensConst.BLE_CHAR_CUSTOM_TIME);
        Log.e(TAG, "initializeDevice: " + mCustomTimeCharacteristic.getUuid().toString());
        //builder.notify(mCustomTimeCharacteristic, true);

        // enableRecordAccessControlPointIndication(builder);

        GB.toast(getContext(), "혈당 데이터 가져오는 중", Toast.LENGTH_LONG, GB.INFO);

        builder.notify(mGlucoseMeasurementCharacteristic, true);
        builder.notify(mGlucoseMeasurementContextCharacteristic, true);
        builder.notify(mRACPCharacteristic, true);
        builder.notify(mCustomTimeCharacteristic, true);

        requestAllRecord(builder);
        builder.queue(getQueue());

        builder.add(new SetDeviceStateAction(getDevice(), GBDevice.State.INITIALIZED, getContext()));
        LOG.info("Initialization Done");

        return builder;
    }

    @Override
    public boolean useAutoConnect() {
        return false;
    }

    @Override
    public void onNotification(NotificationSpec notificationSpec) {

    }

    @Override
    public void onDeleteNotification(int id) {

    }

    @Override
    public void onSetTime() {

    }

    @Override
    public void onSetAlarms(ArrayList<? extends Alarm> alarms) {

    }

    @Override
    public void onSetCallState(CallSpec callSpec) {

    }

    @Override
    public void onSetCannedMessages(CannedMessagesSpec cannedMessagesSpec) {

    }

    @Override
    public void onSetMusicState(MusicStateSpec stateSpec) {

    }

    @Override
    public void onSetMusicInfo(MusicSpec musicSpec) {

    }

    @Override
    public void onEnableRealtimeSteps(boolean enable) {

    }

    @Override
    public void onInstallApp(Uri uri) {

    }

    @Override
    public void onAppInfoReq() {

    }

    @Override
    public void onAppStart(UUID uuid, boolean start) {

    }

    @Override
    public void onAppDelete(UUID uuid) {

    }

    @Override
    public void onAppConfiguration(UUID appUuid, String config, Integer id) {

    }

    @Override
    public void onAppReorder(UUID[] uuids) {

    }

    @Override
    public void onFetchRecordedData(int dataTypes) {
        try {
            TransactionBuilder builder = performInitialized("hello");
            Log.e(TAG, "onFetchRecordedData: " + "fetch ");
            builder.notify(mGlucoseMeasurementCharacteristic, true);
            builder.notify(mGlucoseMeasurementContextCharacteristic, true);
            builder.notify(mRACPCharacteristic, true);
            builder.notify(mCustomTimeCharacteristic, true);

            requestAllRecord(builder);
            builder.queue(getQueue());

        } catch (IOException e) {
            GB.toast(getContext(), "Error on fetching activity data: " + e.getLocalizedMessage(), Toast.LENGTH_LONG, GB.ERROR);
            e.printStackTrace();
        }

    }

    @Override
    public void onReboot() {

    }

    @Override
    public void onHeartRateTest() {

    }

    @Override
    public void onEnableRealtimeHeartRateMeasurement(boolean enable) {

    }

    @Override
    public void onFindDevice(boolean start) {

    }

    @Override
    public void onSetConstantVibration(int integer) {

    }

    @Override
    public void onScreenshotReq() {

    }

    @Override
    public void onEnableHeartRateSleepSupport(boolean enable) {

    }

    @Override
    public void onSetHeartRateMeasurementInterval(int seconds) {

    }

    @Override
    public void onAddCalendarEvent(CalendarEventSpec calendarEventSpec) {

    }

    @Override
    public void onDeleteCalendarEvent(byte type, long id) {

    }

    @Override
    public void onSendConfiguration(String config) {

    }

    @Override
    public void onTestNewFunction() {

    }

    @Override
    public void onSendWeather(WeatherSpec weatherSpec) {

    }

    private void handleGlucoseMeasurement(byte[] msg) {

        int offset = 0;

        //Log.e(TAG, "onCharacteristicChanged: Row :  " +  characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,i));
        final int flags = msg[0];
        Log.e(TAG, "handleGlucoseMeasurement: flag -->  " + flags);
        offset += 1;    // 1

        final boolean timeOffsetPresent = (flags & 0x01) > 0;
        final boolean typeAndLocationPresent = (flags & 0x02) > 0;
        final boolean sensorStatusAnnunciationPresent = (flags & 0x08) > 0;
        final boolean contextInfoFollows = (flags & 0x10) > 0;

        final GlucoseRecord record = new GlucoseRecord();
        record.sequenceNumber = (int) ((msg[offset] & 0xff) | (msg[offset + 1] << 8) & 0xff00);
        Log.e(TAG, " sequenceNumber --> " + record.sequenceNumber);
        record.flag_context = 0;
        offset += 2; // 1+ 2 = 3

        final int year = (int) ((msg[offset] & 0xff) | (msg[offset + 1] << 8) & 0xff00);
        final int month = (int) ((msg[offset + 2] & 0xff));
        final int day = (int) ((msg[offset + 3] & 0xff));
        final int hours = (int) ((msg[offset + 4] & 0xff));
        final int minutes = (int) ((msg[offset + 5] & 0xff));
        final int seconds = (int) ((msg[offset + 6] & 0xff));
        Log.e(TAG, "date time -->: " + year + " | " + month + " | " + day + " | " + hours + " | " + minutes + " | " + seconds);
        offset += 7;


        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hours, minutes, seconds);
        record.time = calendar.getTimeInMillis() / 1000;
        Log.e(TAG, "record.time: --> " + record.time);

        if (timeOffsetPresent) {

            record.timeoffset = unsignedToSigned(unsignedBytesToInt(msg[offset], msg[offset + 1]), 16);
            //record.timeoffset = (int) ((msg[offset] & 0xff) | (msg[offset + 1] << 8) & 0xff00);
            record.time = record.time + (record.timeoffset * 60);
            offset += 2;
        }

        if (typeAndLocationPresent) {
            //byte[] value = characteristic.getValue();
            record.glucoseData = bytesToFloat(msg[offset], msg[offset + 1]);
            final int typeAndLocation = msg[offset + 2];
            int type = (typeAndLocation & 0xF0) >> 4;
            record.flag_cs = type == 10 ? 1 : 0;
            offset += 3;
            Log.e(TAG, "record.glucoseData --> : " + record.glucoseData);
        }

        if (sensorStatusAnnunciationPresent) {
            int hilow = msg[offset];
            if (hilow == 64) record.flag_hilow = -1;//lo
            if (hilow == 32) record.flag_hilow = 1;//hi

            offset += 2;
        }
        if (contextInfoFollows == false) {
            record.flag_context = 1;
        }

        mRecords.put(record.sequenceNumber, record);
    }

    private void handleGlucoseContext(byte[] msg) {

        Log.e(TAG, "BLE_CHAR_GLUCOSE_CONTEXT : 들어옴");
        int offset = 0;
        final int flags = msg[offset] & 0xff;
        offset += 1;

        final boolean carbohydratePresent = (flags & 0x01) > 0;
        final boolean mealPresent = (flags & 0x02) > 0;
        final boolean moreFlagsPresent = (flags & 0x80) > 0;
        final int sequenceNumber = (int) ((msg[offset] & 0xff) | (msg[offset + 1] << 8) & 0xff00);
        //final int sequenceNumber = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
        offset += 2;

        if (moreFlagsPresent) {
            offset += 1;
        }

        if (carbohydratePresent) {
            offset += 3;
        }
        Log.e(TAG, "sequenceNumber : " + sequenceNumber);
        Log.e(TAG, "mealPresent value : " + mealPresent);
        final int meal = mealPresent ? msg[offset] : -1;
        Log.e(TAG, "meal value : " + meal);
        Log.e(TAG, "meal value Hex: " + String.format("0x%02x", meal));

        final GlucoseRecord record = mRecords.get(sequenceNumber);
        if (record == null || mealPresent == false) {
            return;
        }
        record.flag_context = 1;


        String value = String.valueOf(record.glucoseData);
        Log.e(TAG, "handleGlucoseContext: " + value + getDateTime(record.time) + meal);


    }

    private void handleResponseCode(byte[] msg) {

        int offset = 0;
        final int opCode = msg[offset];
        offset += 2; // skip the operator

        if (opCode == OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE) { // 05
            if (mRACPCharacteristic == null) {
                //broadcastUpdate(PremierNConst.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE));
                return;
            }

            //clear();
            //broadcastUpdate(PremierNConst.INTENT_BLE_OPERATESTARTED, "");

            final int number =  (int) ((msg[offset] & 0xff) | (msg[offset + 1] << 8) & 0xff00);;
            offset += 2;

            // TODO: 2018-01-29  number 개수가져오는 곳?
            // number를 문자열로 변환하여 던진다.M

            //setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_ALL_RECORDS);
            //setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THAN_EQUAL,60);
            //setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THAN_EQUAL,0);
            //gatt.writeCharacteristic(mRACPCharacteristic);
            //broadcastUpdate(CareSensConst.INTENT_BLE_SEQUENCECOMPLETED, Integer.toString(number));

//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //_count.setText(Integer.toString(number));
//                    }
//                });

            //Log.e("-- request all data", characteristic.getUuid().toString());
        } else if (opCode == OP_CODE_RESPONSE_CODE) { // 06

            final int subResponseCode = msg[offset] & 0xff;
            final int responseCode = msg[offset + 1] & 0xff;
            offset += 2;

            switch (responseCode) {
                case RESPONSE_SUCCESS:
                    break;
                case RESPONSE_NO_RECORDS_FOUND: //06000106
                    // android 6.0.1 issue - app disconnect send
                    //mBluetoothGatt.writeCharacteristic(characteristic);
                    //broadcastUpdate(PremierNConst.INTENT_BLE_READCOMPLETED, "");
                    GB.toast(getContext(), "전송 완료", Toast.LENGTH_LONG, GB.INFO);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Thread.sleep(100);
                            //mBluetoothGatt.disconnect();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Thread.sleep(100);
                            //BluetoothGatt.writeCharacteristic(characteristic);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case RESPONSE_OP_CODE_NOT_SUPPORTED:
                    //broadcastUpdate(PremierNConst.INTENT_BLE_OPERATENOTSUPPORTED, "");
                    break;
                case RESPONSE_PROCEDURE_NOT_COMPLETED:
                case RESPONSE_ABORT_UNSUCCESSFUL:
                default:
                    //broadcastUpdate(PremierNConst.INTENT_BLE_OPERATEFAILED, "");
                    break;

                //handleResponseCode(data);
            }

        }


    }

    @Override
    public boolean onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

//        return super.onCharacteristicChanged(gatt, characteristic);
        final UUID uuid = characteristic.getUuid();

        Log.e("--  char", characteristic.getUuid().toString());
        Log.e("--  value", characteristic.getValue().toString());
        if (CareSensConst.BLE_CHAR_CUSTOM_TIME.equals(uuid)) { //FFF1
            int offset = 0;
            final int opCode = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
            offset += 2; // skip the operator

            if (opCode == OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE) { // 05: time result
                //broadcastUpdate(PremierNConst.INTENT_BLE_REQUEST_COUNT, "");
                Log.e("-- request sequece", characteristic.getUuid().toString());
            }
        } else if (CareSensConst.BLE_CHAR_GLUCOSE_MEASUREMENT.equals(uuid)) { //2A18

            byte[] data = characteristic.getValue();
            handleGlucoseMeasurement(data);


//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    mRecords.put(record.sequenceNumber, record);
//                    if (!contextInfoFollows) {
//                        String value = String.valueOf(record.glucoseData);
//                        _result.setText("동기화 중");
//                        broadcastUpdate(PremierNConst.INTENT_BLE_DATASETCHANGED, "");
//                        //_result.append(record.sequenceNumber + " : " + record.glucoseData + " , " + getDateTime(record.time) + "\n");
//                        mBSList.add(new BloodSugar(value, getDateTime(record.time), 0));
//                    }
//                }
//            });
            //adapter.notifyDataSetChanged();
        } else if (CareSensConst.BLE_CHAR_GLUCOSE_CONTEXT.equals(uuid)) { //2A34

            byte[] data = characteristic.getValue();
            handleGlucoseContext(data);

            // data set modifications must be done in UI thread
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    final GlucoseRecord record = mRecords.get(sequenceNumber);
//                    if (record == null || mealPresent == false) return;
//                    record.flag_context = 1;
//                    String value = String.valueOf(record.glucoseData);
//                    _result.setText("동기화 중");
//                    //_result.append(sequenceNumber + " : " + record.glucoseData + " , " + getDateTime(record.time) + ", " + meal + "\n");
//                    //_result.append(sequenceNumber + " : " + record.glucoseData + " , " + getDateTime(record.time) + "\n");
//                    mBSList.add(new BloodSugar(value, getDateTime(record.time), meal));
//                    switch (meal) {
//                        case 0:
//                            if (record.flag_cs == 0)
//                                record.flag_nomark = 1;
//                            break;
//                        case 1:
//                            record.flag_meal = -1;
//                            break;
//                        case 2:
//                            record.flag_meal = 1;
//                            break;
//                        case 3:
//                            record.flag_fasting = 1;
//                            break;
//                        case 6:
//                            record.flag_ketone = 1;
//                            break;
//                    }
//                    broadcastUpdate(PremierNConst.INTENT_BLE_DATASETCHANGED, "");
//                }
//            });
            //adapter.notifyDataSetChanged();
        } else if (CareSensConst.BLE_CHAR_GLUCOSE_RACP.equals(uuid)) { // Record Access Control Point characteristic 2A52
            byte[] data = characteristic.getValue();
            handleResponseCode(data);
        }
        return false;
    }


    private final static int SOFTWARE_REVISION_BASE = 1, SOFTWARE_REVISION_1 = 1, SOFTWARE_REVISION_2 = 0; //base: custom profile version

    @Override
    public boolean onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (CareSensConst.BLE_CHAR_SOFTWARE_REVISION.equals(characteristic.getUuid())) { // 2A28
                String[] revisions = characteristic.getStringValue(0).split("\\.");
                Log.e("onCharacteristicRead", "revisions: " + revisions);
                int version_1 = Integer.parseInt(revisions[0]);
                if (version_1 > SOFTWARE_REVISION_1) { //지원하는 버전보다 큰 경우 예외 처리 후 완료
                    //broadcastUpdate(PremierNConst.INTENT_BLE_READ_SOFTWARE_REV, "");
                    Log.e("-- disconnect", " -- revision");
                    gatt.disconnect();
                    return false;
                } else if ((version_1 >= SOFTWARE_REVISION_BASE) && (version_1 == SOFTWARE_REVISION_1)) {// meter software version check: current version - 1.0.00
                    if (mCustomTimeCharacteristic == null) {
                        //broadcastUpdate(PremierNConst.INTENT_BLE_READ_SOFTWARE_REV, characteristic.getStringValue(0)); //version 일치하는 경우는 custom이 반드시 있어야 함. 없으면 종료
                        Log.e("-- disconnect", " -- custom null");
                        gatt.disconnect();
                        return false;
                    }
//                        else if(Integer.parseInt(revisions[1]) > SOFTWARE_REVISION_2){
//                            showToast(R.string.error_software_revision);
//                        }
                }

                if (mDeviceSerialCharacteristic != null) {
                    //readDeviceSerial(gatt);
                }
            } else if (CareSensConst.BLE_CHAR_GLUCOSE_SERIALNUM.equals(characteristic.getUuid())) { //2A25
                //_serial_text = characteristic.getStringValue(0);
                Log.e(TAG, " characteristic.getStringValue(0): " + characteristic.getStringValue(0));

                enableRecordAccessControlPointIndication(gatt);
                Log.e(TAG, "enableRecordAccessControlPointIndication: ");
            }
        }

//        return super.onCharacteristicRead(gatt, characteristic, status);
        return false;
    }

    private void enableRecordAccessControlPointIndication(final BluetoothGatt gatt) {
        if (mRACPCharacteristic == null) return;

        gatt.setCharacteristicNotification(mRACPCharacteristic, true);
        final BluetoothGattDescriptor descriptor = mRACPCharacteristic.getDescriptor(CareSensConst.BLE_DESCRIPTOR_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    private void enableRecordAccessControlPointIndication(TransactionBuilder builder) {
        if (mRACPCharacteristic == null) {
            return;
        }
        builder.notify(mRACPCharacteristic, true);

    }


//    private boolean getAllRecords() {
////        if (mBluetoothGatt == null || mRACPCharacteristic == null) {
////            broadcastUpdate(PremierNConst.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE) + "null");
////            return false;
////        }
////
////        clear();
////        broadcastUpdate(PremierNConst.INTENT_BLE_OPERATESTARTED, "");
//
//        Log.e("-- all records", "data");
//        // TODO: 2018-01-29 opcode = 1,  operator = 1
//        setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_ALL_RECORDS);
//        //setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THAN_EQUAL,60);
//        //setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THAN_EQUAL,0);
//        return mBluetoothGatt.writeCharacteristic(mRACPCharacteristic);
//
//    }

    private CareSensSupport requestAllRecord(TransactionBuilder builder) {
        if (mRACPCharacteristic == null) {
//            broadcastUpdate(PremierNConst.INTENT_BLE_ERROR, getResources().getString(R.string.ERROR_CONNECTION_STATE_CHANGE) + "null");
            return null;
        }

        setOpCode(mRACPCharacteristic, OP_CODE_REPORT_STORED_RECORDS, OPERATOR_ALL_RECORDS);
        builder.write(mRACPCharacteristic, new byte[]{
                OP_CODE_REPORT_STORED_RECORDS,
                OPERATOR_ALL_RECORDS
        });
        return this;

    }

    private void setOpCode(final BluetoothGattCharacteristic characteristic, final int opCode, final int operator, final Integer... params) {
        if (characteristic == null) return;

        final int size = 2 + ((params.length > 0) ? 1 : 0) + params.length * 2; // 1 byte for opCode, 1 for operator, 1 for filter type (if parameters exists) and 2 for each parameter
        characteristic.setValue(new byte[size]);

        int offset = 0;
        characteristic.setValue(opCode, BluetoothGattCharacteristic.FORMAT_UINT8, offset);
        offset += 1;

        characteristic.setValue(operator, BluetoothGattCharacteristic.FORMAT_UINT8, offset);
        offset += 1;

        if (params.length > 0) {
            characteristic.setValue(FILTER_TYPE_SEQUENCE_NUMBER, BluetoothGattCharacteristic.FORMAT_UINT8, offset);
            offset += 1;

            for (final Integer i : params) {
                characteristic.setValue(i, BluetoothGattCharacteristic.FORMAT_UINT16, offset);
                offset += 2;
            }
        }
    }

    public String getDateTime(long t) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        Log.e(TAG, "simpleDateFormat: t - - > " + simpleDateFormat.format(t * 1000));
        String strDate = simpleDateFormat.format(t * 1000);
        strDate += "," + simpleTimeFormat.format(t * 1000);
        //simpleDateFormat.format(t * 1000);
        //simpleTimeFormat.format(t * 1000);

        //java.text.DateFormat df = DateFormat.getDateFormat(this);// getDateFormat(context);
        //Log.e(TAG, "getDateTime: t - - > " + t );
        //String strDate = df.format(t * 1000);

        //Log.e(TAG, "getDateTime: " + strDate);
        //df = DateFormat.getTimeFormat(this);
        // TODO: 2018-02-27 기존코드는 아래
        //strDate += " " + df.format(t * 1000);
        // TODO: 2018-02-27 trim하기위한것
        //strDate += "," + df.format(t * 1000);

        Log.e(TAG, "getDateTime2: " + strDate);

        return strDate;
    }


    private float bytesToFloat(byte b0, byte b1) {
//        Log.e(TAG, "b0: " + b0 + " b1: " +  b1 );
//        Log.e(TAG, "unsignedByteToInt(b0) : " + unsignedByteToInt(b0) );
//        Log.e(TAG, "(unsignedByteToInt(b1) & 0x0F) : " + (unsignedByteToInt(b1) & 0x0F) );
//        Log.e(TAG, "((unsignedByteToInt(b1) & 0x0F) << 8) : " + ((unsignedByteToInt(b1) & 0x0F) << 8) );
//        Log.e(TAG, "bytesToFloat: " + (float)unsignedByteToInt(b0) + ((unsignedByteToInt(b1) & 0x0F) << 8) );
        return (float) unsignedByteToInt(b0) + ((unsignedByteToInt(b1) & 0x0F) << 8);
    }

    private int unsignedByteToInt(byte b) {
        //Log.e(TAG, "unsignedByteToInt: " + (b & 0xFF) );
        return b & 0xFF;
    }

    /**
     * Convert an unsigned integer value to a two's-complement encoded
     * signed value.
     */
    private int unsignedToSigned(int unsigned, int size) {
        if ((unsigned & (1 << size - 1)) != 0) {
            unsigned = -1 * ((1 << size - 1) - (unsigned & ((1 << size - 1) - 1)));
        }
        return unsigned;
    }

    /**
     * Convert signed bytes to a 16-bit unsigned int.
     */
    private int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }


}
