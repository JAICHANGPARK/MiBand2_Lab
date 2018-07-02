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
package nodomain.knu2018.bandutils.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.cketti.library.changelog.ChangeLog;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.BuildConfig;
import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.Remote.IUploadAPI;
import nodomain.knu2018.bandutils.activities.initfood.SearchFoodActivity;
import nodomain.knu2018.bandutils.activities.selectdevice.CategoryActivity;
import nodomain.knu2018.bandutils.activities.userprofile.HomeProfileActivity;
import nodomain.knu2018.bandutils.activities.writing.WriteHomesActivity;
import nodomain.knu2018.bandutils.activities.writing.WriteMealSelectActivity;
import nodomain.knu2018.bandutils.adapter.GBDeviceAdapterv2;
import nodomain.knu2018.bandutils.adapter.home.BeaconDeviceAdapter;
import nodomain.knu2018.bandutils.adapter.home.WriteHorizontalAdapter;
import nodomain.knu2018.bandutils.devices.DeviceManager;
import nodomain.knu2018.bandutils.impl.GBDevice;
import nodomain.knu2018.bandutils.model.beacon.KNUBeacon;
import nodomain.knu2018.bandutils.util.AndroidUtils;
import nodomain.knu2018.bandutils.util.GB;
import nodomain.knu2018.bandutils.util.Prefs;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static nodomain.knu2018.bandutils.Const.BeaconConst.ALTBEACON_LAYOUT;
import static nodomain.knu2018.bandutils.Const.BeaconConst.EDDYSTONE_TLM_LAYOUT;
import static nodomain.knu2018.bandutils.Const.BeaconConst.EDDYSTONE_UID_LAYOUT;
import static nodomain.knu2018.bandutils.Const.BeaconConst.EDDYSTONE_URL_LAYOUT;
import static nodomain.knu2018.bandutils.Const.BeaconConst.EDDYSTON_BEACON_PARSER;


/**
 * ____  ____  _________    __  ____       _____    __    __ __ __________
 * / __ \/ __ \/ ____/   |  /  |/  / |     / /   |  / /   / //_// ____/ __ \
 * / / / / /_/ / __/ / /| | / /|_/ /| | /| / / /| | / /   / ,<  / __/ / /_/ /
 * / /_/ / _, _/ /___/ ___ |/ /  / / | |/ |/ / ___ |/ /___/ /| |/ /___/ _, _/
 * /_____/_/ |_/_____/_/  |_/_/  /_/  |__/|__/_/  |_/_____/_/ |_/_____/_/ |_|
 * <p>
 * Created by Dreamwalker on 2018-05-21.
 */
//TODO: extend AbstractGBActivity, but it requires actionbar that is not available
public class ControlCenterv2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GBActivity,
        OnFABMenuSelectedListener,
        RatingDialogListener,
        BeaconConsumer {

    private static final String TAG = "ControlCenterv2";
    private static final String APP_VERSION_KEY = "bandutil_version";
    private static final String BASE_URL = "http://kangwonelec.com/";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    BeaconManager beaconManager;

    /**
     * The .
     */
    PackageInfo i = null;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    /**
     * The Version number.
     */
    String versionNumber;
    /**
     * The Device version.
     */
    String deviceVersion;
    /**
     * The Store version.
     */
    String storeVersion;


    //needed for KK compatibility
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /**
     * The Lottie animation view.
     */
    // TODO: 2018-06-26 연결된 장비 없을때 처리할 Animation - 박제창 (Dreamwalker)
    @BindView(R.id.lottie_animation)
    LottieAnimationView lottieAnimationView;

    private DeviceManager deviceManager;
    //private ImageView background;

    // TODO: 2018-06-26 연결된 장비 확인을 위한 리스트 뷰 - 박제창 (Dreamwalker)
    private GBDeviceAdapterv2 mGBDeviceAdapter;
    private RecyclerView deviceListView;

    // TODO: 2018-06-26 Horizontal RecyclerView 뷰 - 박제창 (Dreamwalker)
    @BindView(R.id.write_recycler_view)
    RecyclerView writeRecyclerView;

    // TODO: 2018-07-02 비콘 관련 뷰 및 객체 - 박제창 (Dreamwalker)
    @BindView(R.id.beaconListView)
    RecyclerView beaconRecyclerView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    // TODO: 2018-07-02 비콘 스캔 관련 객체 - 박제창 (Dreamwalker)
    ArrayList<KNUBeacon> beaconArrayList;
    BeaconDeviceAdapter BeaconAdapter;
    BluetoothAdapter bluetoothAdapter;
    BackgroundPowerSaver backgroundPowerSaver;


    private boolean isLanguageInvalid = false;

    /**
     * The Fab menu.
     */
    FABRevealMenu fabMenu;
    /**
     * The UserInfo name.
     */
    String userName;
    /**
     * The UserInfo uuid.
     */
    String userUUID;

    /**
     * The App open count.
     */
    int appOpenCount = 0;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (Objects.requireNonNull(action)) {
                case GBApplication.ACTION_LANGUAGE_CHANGE:
                    setLanguage(GBApplication.getLanguage(), true);
                    break;
                case GBApplication.ACTION_QUIT:
                    finish();
                    break;
                case DeviceManager.ACTION_DEVICES_CHANGED:
                    refreshPairedDevices();
                    break;
            }
        }
    };

    /**
     * The Retrofit.
     */
    Retrofit retrofit;
    /**
     * The Service.
     */
    IUploadAPI service;

    NetworkInfo networkInfo;


    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> url = new ArrayList<>();

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    private void initNetworkSetting() {
        networkInfo = getNetworkInfo();
    }


    /**
     * 기록하기 Horizontal 데이터를 넣습니다.
     * 인터넷의 연결 상태를 확인합니다.
     *
     * @author : 박제창(Dreamwalker)
     */

    private void setWriteHorizontalData() {

        if (networkInfo != null && networkInfo.isConnected()) {
            url.add("https://cdn.pixabay.com/photo/2018/04/11/11/53/the-level-of-sugar-in-the-blood-3310318_960_720.jpg");
            titleList.add(getResources().getString(R.string.home_horizontal_blood_sugar));

            url.add("https://cdn.pixabay.com/photo/2016/11/14/03/06/woman-1822459_960_720.jpg");
            titleList.add(getResources().getString(R.string.home_horizontal_fitness));

            url.add("https://cdn.pixabay.com/photo/2017/01/11/20/11/insulin-syringe-1972788_960_720.jpg");
            titleList.add(getResources().getString(R.string.home_horizontal_drug));

            url.add("https://cdn.pixabay.com/photo/2015/03/26/09/42/breakfast-690128_960_720.jpg");
            titleList.add(getResources().getString(R.string.home_horizontal_meal));

            url.add("https://cdn.pixabay.com/photo/2016/06/12/00/06/instagram-1451137_960_720.jpg");
            titleList.add(getResources().getString(R.string.home_horizontal_meal_photo));

            url.add("https://cdn.pixabay.com/photo/2016/01/20/11/11/baby-1151351_960_720.jpg");
            titleList.add(getResources().getString(R.string.home_horizontal_sleep));

            initWriteRecyclerView(true);
        } else {
            url.add("none");
            titleList.add(getResources().getString(R.string.home_horizontal_blood_sugar));

            url.add("none");
            titleList.add(getResources().getString(R.string.home_horizontal_fitness));

            url.add("none");
            titleList.add(getResources().getString(R.string.home_horizontal_drug));

            url.add("none");
            titleList.add(getResources().getString(R.string.home_horizontal_meal));

            url.add("none");
            titleList.add(getResources().getString(R.string.home_horizontal_meal_photo));

            url.add("none");
            titleList.add(getResources().getString(R.string.home_horizontal_sleep));
            initWriteRecyclerView(false);
        }

    }

    /**
     * 기록하기 Horizontal RecyclerView 객체와 Adapter를 생성합니다.
     * 인터넷 연결이 되어있는지 없는지에 따라 예외처리를 하기위해
     * 네트워크 상태를 Boolean 형의 파라미터로 입력 받습니다.
     * <p>
     * 인터넷 연결이 있으면 true
     * 인터넷 연결이 없으면 false
     *
     * @param networkState
     * @author : 박제창(Dreamwalker)
     */
    private void initWriteRecyclerView(Boolean networkState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        writeRecyclerView.setLayoutManager(layoutManager);
        WriteHorizontalAdapter adapter = new WriteHorizontalAdapter(this, titleList, url, networkState);
        writeRecyclerView.setAdapter(adapter);
    }

    /**
     * 주변의 스캔된 비콘의 객체를 표시하기 위한 리사이클러 뷰 객체의 생성과
     * 리스트 구조의 초기화를 진행하는 메소드
     *
     * @author : 박제창(Dreamwalker)
     */
    private void initScanBeacon() {
        beaconRecyclerView.setHasFixedSize(true);
        beaconRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        beaconArrayList = new ArrayList<>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AbstractGBActivity.init(this, AbstractGBActivity.NO_ACTIONBAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlcenterv2);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_navigation_bar));

        Paper.init(this);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        service = retrofit.create(IUploadAPI.class);

        initNetworkSetting();

        // TODO: 2018-06-04 애플리케이션 평가 처리를 진행합니다. -- 박제창
        if (Paper.book().read("app_open_count") == null) {
            Paper.book().write("app_open_count", appOpenCount);
        } else {

            if (Paper.book().read("app_rate_submit") != null) {
                Log.e(TAG, "onCreate: app rate" + "앱 평가 이미 완료되었음.");
            } else {
                Log.e(TAG, "onCreate:app rate " + "아직 키 값 없음.");
                appOpenCount = Paper.book().read("app_open_count");
                appOpenCount += 1;

                if (appOpenCount == 20) {
                    Log.e(TAG, "onCreate: appOpenCount call 10: " + appOpenCount);
                    popAppRating();
                    appOpenCount = 0;
                }

                Paper.book().write("app_open_count", appOpenCount);
                Log.e(TAG, "onCreate: appOpenCount : " + appOpenCount);
            }
        }

        // TODO: 2018-07-02 Firebase 토큰 생성
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "onCreate: " + token);

        FloatingActionButton fab = findViewById(R.id.fab);
        fabMenu = findViewById(R.id.fabMenu);

        try {
            if (fab != null && fabMenu != null) {
                //setFabMenu(fabMenu);
                //attach menu to fab
                fabMenu.bindAnchorView(fab);
                //set menu selection listener
                fabMenu.setOnFABMenuSelectedListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        userName = Paper.book().read("userName");
        userUUID = Paper.book().read("userUUID");

        //Log.e(TAG, "onCreate: " + userName + ", " + userUUID);


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchDiscoveryActivity();
//            }
//        });
        // TODO: 2018-07-02 네비케이션 드로우 뷰 및 객체 생성
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.controlcenter_navigation_drawer_open, R.string.controlcenter_navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: 2018-05-22 네비게이션 뷰의 해더 부분 인플레이트 - 박제창 (Dreamwalker)
        View headView = navigationView.getHeaderView(0);
        TextView textUserName = (TextView) headView.findViewById(R.id.text_name);
        TextView textUserUUID = (TextView) headView.findViewById(R.id.text_uuid);

        // TODO: 2018-05-22 등록된 유저 정보 표기 - 박제창 (Dreamwalker)
        textUserName.setText(userName);
        textUserUUID.setText(userUUID);

        headView.setOnClickListener(v -> startActivity(new Intent(ControlCenterv2.this, HomeProfileActivity.class)));

        // TODO: 2018-06-26 Horizontal Recycler 생성 - 박제창 (Dreamwalker)
        setWriteHorizontalData();

        //end of material design boilerplate
        deviceManager = ((GBApplication) getApplication()).getDeviceManager();

        deviceListView = findViewById(R.id.deviceListView);
        deviceListView.setHasFixedSize(true);
        deviceListView.setLayoutManager(new LinearLayoutManager(this));
        //background = findViewById(R.id.no_items_bg);

        List<GBDevice> deviceList = deviceManager.getDevices();
        mGBDeviceAdapter = new GBDeviceAdapterv2(this, deviceList);

        // TODO: 2018-06-20 디바이스 디버그좀 할께요 - 박제창 (Dreamwalker)
        for (GBDevice device : deviceList) {
            Log.e(TAG, "loop device" + device.getAddress());
        }

        deviceListView.setAdapter(this.mGBDeviceAdapter);

        /* uncomment to enable fixed-swipe to reveal more actions

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT , ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if(dX>50)
                    dX = 50;
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                GB.toast(getBaseContext(), "onMove", Toast.LENGTH_LONG, GB.ERROR);

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                GB.toast(getBaseContext(), "onSwiped", Toast.LENGTH_LONG, GB.ERROR);

            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
            }
        });

        swipeToDismissTouchHelper.attachToRecyclerView(deviceListView);
        */

        registerForContextMenu(deviceListView);

        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(GBApplication.ACTION_LANGUAGE_CHANGE);
        filterLocal.addAction(GBApplication.ACTION_QUIT);
        filterLocal.addAction(DeviceManager.ACTION_DEVICES_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filterLocal);

        refreshPairedDevices();


        // TODO: 2018-07-02 비콘스캔을 위한 권한 체크 및 어댑터 얻기 - 박제창
        initScanBeacon();
        checkScanPermission();
        getBluetoothAdapter();
        checkBluetoothAdapter(bluetoothAdapter);
        setBeaconManager("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        bindBeaconService(this);



        /*
         * Ask for permission to intercept notifications on first run.
         */
        // TODO: 2018-06-20 설치 시 처음 딱 한번 실행되는 부분입니다. - DREAMWALKER

        Prefs prefs = GBApplication.getPrefs();
        if (prefs.getBoolean("firstrun", true)) {
            Toast.makeText(this, "알람 설정을 켜놓으면 좋습니다. " +
                    "원하지 않으시면 뒤로가기 또는 취소버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
            prefs.getPreferences().edit().putBoolean("firstrun", false).apply();
            Intent enableIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(enableIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }

        ChangeLog cl = createChangeLog();
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
            onGuideTapTarget();
        }

        GBApplication.deviceService().start();

        if (GB.isBluetoothEnabled() && deviceList.isEmpty() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startActivity(new Intent(this, DiscoveryActivity.class));
        } else {
            GBApplication.deviceService().requestDeviceInfo();
        }

        // TODO: 2018-05-20 Firebase RemoteConfig를 사용해서 버전 정보를 갱신해야한다.
        // TODO: 2018-05-20 같은 버전이면 알람이 뜨지 않아 문제가 되지 않을것. 그러나 최신 버전을 업데이트 되었을떄 알람이 되어야함.
        checkAppVersion();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        fetchWelcome();

        printKeyHash();

    }


    /**
     * bluetoothAdapter 객체를 얻어온다.
     *
     * @author : 박제창(Dreamwalker)
     */
    private void getBluetoothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 블루투스가 핸드폰에서 가능한지 확인하는 메소드 .
     * 블르투스 Adapter를 얻어와서 사용할 수 없다면 블루투스 활성화로 진행하고
     * 즉 비활성화 되어있다면 활성화로 진행한다.
     *
     * @author : 박제창(Dreamwalker)
     */
    private void checkBluetoothAdapter(BluetoothAdapter adapter) {
        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Log.e(TAG, "onCreate: getInstanceForApplication");
            //setBeaconManager("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            // BEACON_PARSER 는 문자열인데요. iBeacon 이냐 EddyStone 이냐에 따라 다른 문자열을 필요로합니다.
            //BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        }
    }

    /**
     * 처음 권한을 요구했는데도 불구하고 블루투스 스캔을 위한 권한인
     * ACCESS_COARSE_LOCATION 를 거부했다면 다시 요청을 진행한다.
     * ACCESS_COARSE_LOCATION권한이 없다면 마시멜로우 이상부터는 BLE Scan이 불가능하다.
     *
     * @author : 박제창(Dreamwalker)
     */
    private void checkScanPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton("OK", null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }
    }

    /**
     * 비콘 스캔을 위한 메니저 객체 생성과
     * 비콘 스캔을 위한 필터를 설정한다.
     *
     * @param beaconFilter
     * @author : 박제창(Dreamwalker)
     */
    private void setBeaconManager(String beaconFilter) {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(beaconFilter));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(ALTBEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTON_BEACON_PARSER));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_URL_LAYOUT));
    }

    /**
     * 비콘 서비스를 활성화 한다.
     * Bind는 서비스 에서 사용되는 메소드중 하나이다.
     *
     * @param beaconConsumer
     * @author : 박제창(Dreamwalker)
     */
    private void bindBeaconService(BeaconConsumer beaconConsumer) {
        beaconManager.bind(beaconConsumer);
    }

    /**
     * 비콘 서비스를 비 활성화 한
     *
     * @author : 박제창(Dreamwalker)
     */
    private void unbindBeaconService() {
        beaconManager.unbind(this);
    }


    /**
     * これなら、簡単にSHA取得でしる。
     *
     * @author : Dreamwalker.
     */

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("nodomain.knu2018.bandutils", PackageManager.GET_SIGNATURES);

            for (android.content.pm.Signature s : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(s.toByteArray());
                Log.e(TAG, "printKeyHash: " + android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 가이던스 뷰
     * 시작시 한번 만 실행되도록 해야합니다.
     *
     * @author : 박제창 (Dreamwalker)
     */
    private void onGuideTapTarget() {

        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.fab),
                        getString(R.string.home_tap_target_title), getString(R.string.home_tap_target_description))
                        // All options below are optional
                        .outerCircleColor(R.color.primarydark_custom_dark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.primarydark_custom_dark)  // Specify the color of the description text
                        .textColor(R.color.white)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(getResources().getDrawable(R.drawable.ic_add))
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional

                    }
                });
    }

    private void popAppRating() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText(R.string.app_rating_positive_button)
                .setNegativeButtonText(R.string.app_rating_negative_button)
                .setNeutralButtonText(R.string.app_rating_neutral_button)
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle(R.string.app_rating_title)
                .setDescription(R.string.app_rating_description)
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.descriptionTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogSlideHorizontalAnimation)
                .setHint(R.string.app_rating_hint)
                .setHintTextColor(R.color.hintTextColor)
                .create(ControlCenterv2.this)
                .show();
    }

    /**
     * Gradle상의 버전 정보를 가져온다.
     */
    private void checkAppVersion() {
        try {
            i = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNumber = i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    // [START display_welcome_message]
    private void displayWelcomeMessage() {
        // [START get_config_values]
        String playstoreVersion = mFirebaseRemoteConfig.getString(APP_VERSION_KEY);
        Log.e(TAG, "displayWelcomeMessage: " + playstoreVersion);

        if (versionNumber.equals(playstoreVersion)) {
            Log.e(TAG, "displayWelcomeMessage: 최신버전 입니다.");

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light));
            builder.setTitle(R.string.app_update_title);
            builder.setMessage(R.string.app_update_message);
            builder.setPositiveButton(R.string.app_update_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            builder.setNegativeButton(R.string.app_update_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        }

    }
    // [END display_welcome_message]

    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     */
    private void fetchWelcome() {
        //mWelcomeTextView.setText(mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(ControlCenterv2.this, "Fetch Succeeded", Toast.LENGTH_SHORT).show();

                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    mFirebaseRemoteConfig.activateFetched();
                } else {
                    Log.e(TAG, "onComplete: " + "Version info Fetch Failed");
                    //Toast.makeText(ControlCenterv2.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                }
                displayWelcomeMessage();
            }
        });
        // [END fetch_config_with_callback]
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLanguageInvalid) {
            isLanguageInvalid = false;
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterForContextMenu(deviceListView);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Navigation Drawer 메뉴 선택 처리 메소드
     *
     * @param item
     * @return
     * @author 박제창 (Dreamwalker)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {

            case R.id.food_check_db:
                Intent foodSearchIntent = new Intent(this, SearchFoodActivity.class);
                startActivity(foodSearchIntent);
                return true;

            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_debug:
                Intent debugIntent = new Intent(this, DebugActivity.class);
                startActivity(debugIntent);
                return true;

            case R.id.action_check_db:
                Intent readDbIntent = new Intent(this, ReadDBActivity.class);
                startActivity(readDbIntent);
                return true;

            case R.id.action_storage:
                Intent checkStorage = new Intent(this, StorageCheckActivity.class);
                startActivity(checkStorage);
                return true;

            case R.id.action_db_management:
                Intent dbIntent = new Intent(this, DbManagementActivity.class);
                startActivity(dbIntent);
                return true;

            case R.id.action_user_information:
                Intent myInformationIntent = new Intent(this, HomeProfileActivity.class);
                startActivity(myInformationIntent);
                return true;

            case R.id.action_licenses:
                Intent intent = new Intent(this, OpenSourceLicenseActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_about:
                ChangeLog cl = createChangeLog();
                cl.getFullLogDialog().show();
                return true;
            case R.id.action_quit:
                GBApplication.quit();
                return true;


//            case R.id.donation_link:
//                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://liberapay.com/Gadgetbridge")); //TODO: centralize if ever used somewhere else
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                return true;
//            case R.id.external_changelog:
//                ChangeLog cl = createChangeLog();
//                cl.getFullLogDialog().show();
//                return true;
        }

        return true;
    }

    private ChangeLog createChangeLog() {
        String css = ChangeLog.DEFAULT_CSS;
        css += "body { "
                + "color: " + AndroidUtils.getTextColorHex(getBaseContext()) + "; "
                + "background-color: " + AndroidUtils.getBackgroundColorHex(getBaseContext()) + ";" +
                "}";
        return new ChangeLog(this, css);
    }

    private void launchDiscoveryActivity() {
        startActivity(new Intent(this, DiscoveryActivity.class));
    }

    private void launchSelectCategoryActivity() {
        startActivity(new Intent(this, CategoryActivity.class));
    }

    private void refreshPairedDevices() {
        List<GBDevice> deviceList = deviceManager.getDevices();
        if (deviceList.isEmpty()) {
            // TODO: 2018-05-20 등록된 디바이스가 없을 때
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
            //background.setVisibility(View.VISIBLE);
        } else {
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.cancelAnimation();
            // TODO: 2018-05-20 등록된 디바이스가 있을 때 이미지 감추기
            //background.setVisibility(View.INVISIBLE);
        }

        mGBDeviceAdapter.notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermissions() {
        List<String> wantedPermissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            createDir();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.BLUETOOTH);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.READ_CONTACTS);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.CALL_PHONE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.RECEIVE_SMS);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.READ_SMS);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.SEND_SMS);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
//            wantedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED)
            wantedPermissions.add(Manifest.permission.READ_CALENDAR);
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MEDIA_CONTENT_CONTROL) == PackageManager.PERMISSION_DENIED)
                wantedPermissions.add(Manifest.permission.MEDIA_CONTENT_CONTROL);
        } catch (Exception ignored) {
        }

        if (!wantedPermissions.isEmpty())
            ActivityCompat.requestPermissions(this, wantedPermissions.toArray(new String[wantedPermissions.size()]), 0);
    }

    public void setLanguage(Locale language, boolean invalidateLanguage) {
        if (invalidateLanguage) {
            isLanguageInvalid = true;
        }
        AndroidUtils.setLanguage(this, language);
    }

    /**
     * fab 클릭시 이밴트 처리
     *
     * @param view
     * @param id
     */
    @Override
    public void onMenuItemSelected(View view, int id) {
        if (id == R.id.menu_add_information) {
            startActivity(new Intent(this, WriteHomesActivity.class));
            //Toast.makeText(this, "Place Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_write_meal_detail) {
            startActivity(new Intent(this, WriteMealSelectActivity.class));
        }

        // TODO: 2018-06-26 식사 쓰기와 수면 쓰기를 잠시 삭제 해봅니다. - 박제창 (Dreamwalker)
//        else if (id == R.id.menu_write_meal) {
//            startActivity(new Intent(this, WriteMealActivity.class));
//            //Toast.makeText(this, " ", Toast.LENGTH_SHORT).show();
//        }
//        else if (id == R.id.menu_write_sleep) {
//            startActivity(new Intent(this, WriteSleepActivity.class));
//            //Toast.makeText(this, "장비 추가 ", Toast.LENGTH_SHORT).show();
//        }
//
        else if (id == R.id.menu_add_watch) {
            launchSelectCategoryActivity();
            //launchDiscoveryActivity();
            Toast.makeText(this, "장비 추가 ", Toast.LENGTH_SHORT).show();
        }

    }

    private void createDir() {
        boolean result = false;
        File sd = Environment.getExternalStorageDirectory();
        File file = new File(sd, "/BandUtil/data/files");

        if (!file.exists()) {
            result = file.mkdirs();
        } else {
            Log.e(TAG, "createDir: asdfds");
        }
        Log.e(TAG, "createDir: " + result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.contect:
                startActivity(new Intent(this, AboutContactActivity.class));
                return true;
            case R.id.help:
                // TODO: 2018-05-20 개발자 정보  
                startActivity(new Intent(this, AboutDeveloperActivity.class));
                return true;
            case R.id.lab:
                startActivity(new Intent(this, AppLabActivity.class));
                Toast.makeText(this, "오픈 예정입니다.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    /**
     * App Raiting Dialog Listener
     *
     * @param i
     * @param s
     */

    @Override
    public void onPositiveButtonClicked(int i, String s) {

        String rate = String.valueOf(i);

        Call<ResponseBody> comment = service.userAppRating(userName, userUUID, rate, s);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.body());
                    Paper.book().write("app_rate_submit", true);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ControlCenterv2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {
        Snackbar.make(getWindow().getDecorView().getRootView(), "다음에는 꼭 평가 부탁드려요", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNeutralButtonClicked() {
        Snackbar.make(getWindow().getDecorView().getRootView(), "다음에는 꼭 평가 부탁드려요", Snackbar.LENGTH_SHORT).show();
    }


    /**
     * 비콘 스캔을 위한 implement 메소드
     */
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier((beacons, region) -> {


            if (beaconArrayList.size() != 0) {
                runOnUiThread(() -> {
                    beaconRecyclerView.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                });

                beaconArrayList.clear();
            }
            if (beacons.size() > 0) {
                runOnUiThread(()->{
                    beaconRecyclerView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);
                    animationView.cancelAnimation();
                });

                Iterator<Beacon> iterator = beacons.iterator();

                while (iterator.hasNext()) {
                    // beaconArrayList = new ArrayList<>();
                    Beacon beacon = iterator.next();
                    String address = beacon.getBluetoothAddress();
                    Log.e(TAG, "getBluetoothAddress: " + address);
                    double rssi = beacon.getRssi();
                    int txPower = beacon.getTxPower();
                    double distance = Double.parseDouble(decimalFormat.format(beacon.getDistance()));
//                        int major = beacon.getId2().toInt();
//                        int minor = beacon.getId3().toInt();
                    String major = beacon.getId2().toString();
                    String minor = beacon.getId3().toString();
                    String uuid = String.valueOf(beacon.getId1()).toUpperCase();
                    beaconArrayList.add(new KNUBeacon(beacon.getBluetoothName(), address, uuid, major, minor, String.format("%s m", String.valueOf(distance))));
                }
                runOnUiThread(() -> {
                    BeaconAdapter = new BeaconDeviceAdapter(getApplicationContext(), beaconArrayList);
                    beaconRecyclerView.setAdapter(BeaconAdapter);
                    BeaconAdapter.notifyDataSetChanged();
                });
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myMoniter", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                //Log.e(TAG, "didEnterRegion: " + region.getBluetoothAddress());
                Log.e(TAG, "I just saw an beacon for the first time!");
                // TODO: 2018-07-02 UI의 변화가 생길때는 새로운 Thread에서 처리하는게 맞다. - 박제창 (Dreamwalker)
                runOnUiThread(() -> {
                    beaconRecyclerView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);
                    animationView.cancelAnimation();
                });
            }

            @Override
            public void didExitRegion(Region region) {
                //Log.e(TAG, "didEnterRegion: " + region.getBluetoothAddress());
                Log.e(TAG, "I no longer see an beacon");
                runOnUiThread(() -> {
                    beaconRecyclerView.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);
                });
                //animationView.playAnimation();
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.e(TAG, "I have just switched from seeing/not seeing beacons: " + i);
                //Log.e(TAG, "didEnterRegion: " + region.getBluetoothAddress());
                runOnUiThread(() -> {
                    beaconRecyclerView.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                });
                //
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "coarse location permission granted");
                } else {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton("OK", null);
                    builder.setOnDismissListener(dialog -> ActivityCompat.requestPermissions(ControlCenterv2.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION));
                    builder.show();
                }
            }
        }
    }
}
