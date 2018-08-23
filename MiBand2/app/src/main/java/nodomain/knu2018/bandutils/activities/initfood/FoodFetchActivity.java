package nodomain.knu2018.bandutils.activities.initfood;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.ControlCenterv2;
import nodomain.knu2018.bandutils.database.fooddb.DBHelper;
import nodomain.knu2018.bandutils.database.fooddb.Entry;
import nodomain.knu2018.bandutils.model.foodmodel.AppVersion;
import nodomain.knu2018.bandutils.model.foodmodel.Food;
import nodomain.knu2018.bandutils.model.foodmodel.Foods;
import nodomain.knu2018.bandutils.model.foodmodel.MixedFood;
import nodomain.knu2018.bandutils.remote.Common;
import nodomain.knu2018.bandutils.remote.FoodDataFetch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Food fetch activity.
 */
public class FoodFetchActivity extends AppCompatActivity {

    private static final String TAG = "FoodFetchActivity";
    private static final String APP_VERSION_KEY = "food_db_version";  // Firebase 버전 처리 -- 사용하지 않음

    private static final String APP_VERSION_CHECK_PATH = "bandutil_version.php"; // 버전 체크용 php 파일

    private static final String FETCH_DB_SERVER = "food_total.php"; // 데이터 베이스 버전 1 php 파일 이름
    private static final String FOOD_FETCH_V3_URL = "food_db_fetch.php"; // 데이터베이스 버전 2 php 파일 이름
    /**
     * The constant API_URL.
     */
    public static final String API_URL = "http://www.kangwonelec.com/";
    private static final int REQUEST_PERMISSION = 1000;

    /**
     * The M firebase remote config.
     */
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    /**
     * The Shared preferences.
     */
    SharedPreferences sharedPreferences;

    /**
     * The File length.
     */
    long fileLength;
    /**
     * The Count.
     */
    long count = 0;

    /**
     * The Db version.
     */
    String dbVersion;
    /**
     * The Retrofit.
     */
    Retrofit retrofit;
    /**
     * The M service.
     */
    FoodDataFetch mService;

    /**
     * The Dialog.
     */
    AlertDialog dialog;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;
    /**
     * The Db helper.
     */
    DBHelper dbHelper;

    /**
     * The Result list.
     */
    ArrayList<Foods> resultList;
    /**
     * The Array list.
     */
    ArrayList<Food> arrayList;


    // TODO: 2018-08-22 추가 - 박제창
    FoodDataFetch versionCheckService;
    FoodDataFetch dbService;
    String versionFoodCode;
    String fetchVersionCode;
    ArrayList<MixedFood> foodsList;

    @BindView(R.id.textView10)
    TextView headTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_fetch);

        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, REQUEST_PERMISSION);
        }

        initSetting();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("서버로부터 필수 데이터 다운로드 중...");
        progressDialog.setCancelable(false);

        versionCheckService = Common.getFoodDatabaseVersionCheck();
        dbService = Common.getFoodDatabaseRequest();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        textView.setText("데이터베이스 버전 확인 중..");

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.e(TAG, "onCreate: " + "Wi-FI 연결됨");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                Log.e(TAG, "onCreate: " + "모바일 네트워크 연결됨");
            }

            headTextView.setText("잠시만 기다려주세요 \n 버전 정보 확인 중... ");

            versionCheckService.checkFoodDatabaseVersion(APP_VERSION_CHECK_PATH).enqueue(new Callback<AppVersion>() {
                @Override
                public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                    AppVersion appVersion = response.body();
                    Log.e(TAG, "onResponse: " + appVersion.getSuccess());
                    Log.e(TAG, "onResponse: " + appVersion.getDbVersion());
                    fetchVersionCode = appVersion.getDbVersion();
                    versionFoodCode = Paper.book().read("food_version_code");
                    if (versionFoodCode != null) {
                        // TODO: 2018-08-22 기존 사용자 처리 - 박제창
                        if (versionFoodCode.equals(appVersion.getDbVersion())) {
                            Log.e(TAG, "onResponse: 이전 버전 과 같은경우   ");
                            Intent intent = new Intent(FoodFetchActivity.this, ControlCenterv2.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "onResponse: 버전 업그레이드 된 경우   ");
                            dbService.fetchFoodDatabaseVersion2(FOOD_FETCH_V3_URL).enqueue(new Callback<ArrayList<MixedFood>>() {
                                @Override
                                public void onResponse(Call<ArrayList<MixedFood>> call, Response<ArrayList<MixedFood>> response) {
                                    foodsList.clear();
                                    foodsList.addAll(response.body());
                                }
                                @Override
                                public void onFailure(Call<ArrayList<MixedFood>> call, Throwable t) {
                                    Toast.makeText(FoodFetchActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        headTextView.setText("잠시만 기다려주세요 \n 일정 시간이 소요됩니다. ");
                        Log.e(TAG, "onResponse: 처음 사용자  처리 합니다 ");
                        // TODO: 2018-08-01 처음 사용자
                        long start = System.currentTimeMillis();
                        dbService.fetchFoodDatabaseVersion2(FOOD_FETCH_V3_URL).enqueue(new Callback<ArrayList<MixedFood>>() {
                            @Override
                            public void onResponse(Call<ArrayList<MixedFood>> call, Response<ArrayList<MixedFood>> response) {
                                foodsList.clear();
                                foodsList.addAll(response.body());
                                Log.e(TAG, "onResponse: " + foodsList.size());
                                long end = System.currentTimeMillis();
                                new BackgroundTaskVersion2().execute();
                            }
                            @Override
                            public void onFailure(Call<ArrayList<MixedFood>> call, Throwable t) {
                                Toast.makeText(FoodFetchActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<AppVersion> call, Throwable t) {

                }
            });


        } else {

            Toast.makeText(this, "네트워크 연결 없음", Toast.LENGTH_SHORT).show();
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("알림");
            builder.setMessage("데이터 베이스 다운로드를 위해서 인터넷 연결이 필요합니다.");
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            builder.setCancelable(false);
            builder.show();

        }

//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
//        dbVersion = mFirebaseRemoteConfig.getString("food_db_version");
//        Log.e(TAG, "onCreate: dbVersion  - " + dbVersion );

//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setDeveloperModeEnabled(BuildConfig.DEBUG).build();
//        mFirebaseRemoteConfig.setConfigSettings(configSettings);
//
//        fetchWelcome();

//        sharedPreferences = getSharedPreferences("ActivityFoodDatabase", Context.MODE_PRIVATE);
//        if (sharedPreferences.getBoolean("activity_executed", false)) {
//            Intent intent = new Intent(this, ControlCenterv2.class);
//            startActivity(intent);
//            finish();
//        } else {
////            SharedPreferences.Editor ed = pref.edit();
////            ed.putBoolean("activity_executed", true);
////            ed.commit();
//        }


    }

    private void initSetting(){
        Paper.init(this);
        dialog = new SpotsDialog(this);
        dbHelper = new DBHelper(this);
        arrayList = new ArrayList<>();
        resultList = new ArrayList<>();

        foodsList = new ArrayList<>(); //데이터베이스 버전 2 수용 자료구조 리스트 - 박제창
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(FoodDataFetch.class);

    }

    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    // [START display_welcome_message]
    private void displayWelcomeMessage() {
        // [START get_config_values]
        String databaseVersion = mFirebaseRemoteConfig.getString(APP_VERSION_KEY);
        Log.e(TAG, "displayWelcomeMessage: " + databaseVersion);

        if (dbVersion.equals(databaseVersion)) {
            Log.e(TAG, "displayWelcomeMessage: 최신버전 입니다.");
            startActivity(new Intent(FoodFetchActivity.this, ControlCenterv2.class));
            finish();
        } else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light));
//            builder.setTitle("New version available");
//            builder.setMessage("Please, update app to new version");
//            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                    } catch (android.content.ActivityNotFoundException anfe) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                    }
//                }
//            });
//            builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.setCanceledOnTouchOutside(true);
//            alertDialog.show();
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
                    Log.e(TAG, "onComplete: " + "Database Version info Fetch Failed");
                    //Toast.makeText(ControlCenterv2.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                }

                displayWelcomeMessage();
            }
        });
        // [END fetch_config_with_callback]
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    // TODO: 2018-08-22 퍼미션이 허가되면 처리

//                    fetchJson();
                } else {

                    Toast.makeText(this, "Permission denine", Toast.LENGTH_SHORT).show();
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("알림");
                    builder.setMessage("데이터 베이스 저장하기 위해서는 권한 허가가 필요합니다.");
                    builder.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
                    builder.setCancelable(false);
                    builder.show();

                }
        }
    }

    private void fetchJson() {

        progressDialog.show();
        Call<Foods> comment = mService.fetchTotalFood(FETCH_DB_SERVER);

        comment.enqueue(new Callback<Foods>() {
            @Override
            public void onResponse(Call<Foods> call, Response<Foods> response) {

                if (response.isSuccessful()) {
                    Foods repo = response.body();
                    fileLength = repo.getResponse().size();

                    for (int i = 0; i < repo.getResponse().size(); i++) {
                        arrayList.add(repo.getResponse().get(i));
                    }

                    Paper.book().write("db", arrayList);
                    if (arrayList.size() == fileLength) {
                        progressDialog.dismiss();
                        new BackgroundTask().execute();
                    }
                }

            }

            @Override
            public void onFailure(Call<Foods> call, Throwable t) {

                AlertDialog.Builder builder = new AlertDialog.Builder(FoodFetchActivity.this);
                builder.setTitle("Error");
                builder.setMessage("에러가 발생 하였습니다." + t.getMessage());
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();
            }


        });

    }

    class BackgroundTaskVersion2 extends AsyncTask<Void, Void, String>{

        /**
         * The Progress dialog.
         */
        ProgressDialog progressDialog;
        /**
         * The Db helper.
         */
        DBHelper dbHelper;

        int index = 0;

        public BackgroundTaskVersion2() {

            dbHelper = new DBHelper(FoodFetchActivity.this);
            progressDialog = new ProgressDialog(FoodFetchActivity.this);
            Log.e(TAG, "BackgroundTask: " + fileLength + count);
        }



        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(FoodFetchActivity.this);
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("식품 데이터 저장중...");
            progressDialog.setCancelable(false);
            progressDialog.setMax(100);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            boolean deleteTable = dbHelper.truncateTable(2);
            if (deleteTable){
                Log.e(TAG, "doInBackground: " + "데이터베이스 삭제 완료 " );
            }

            ContentValues values = new ContentValues();

//            for (MixedFood mf : foodsList) {
//                count++;
//
////                dbHelper.addFood(mf);
//                progressDialog.setProgress((int) (100 * count / fileLength));
//            }
            for (int i = 0; i < foodsList.size(); i++) {
                index = i;
                values.put(Entry.FoodEntryV2.COLUNM_DB_CLASS, foodsList.get(i).getDbClass());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_CLASS, foodsList.get(i).getFoodClass());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_NAME, foodsList.get(i).getFoodName());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_AMOUNT, foodsList.get(i).getFoodAmount());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_1, foodsList.get(i).getFoodGroup1());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_2, foodsList.get(i).getFoodGroup2());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_3, foodsList.get(i).getFoodGroup3());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_4, foodsList.get(i).getFoodGroup4());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_5, foodsList.get(i).getFoodGroup5());
                values.put(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_6, foodsList.get(i).getFoodGroup6());
                values.put(Entry.FoodEntryV2.COLUNM_TOTAL_EXCHANGE, foodsList.get(i).getTotalExchange());
                values.put(Entry.FoodEntryV2.COLUNM_KCAL, foodsList.get(i).getKcal());
                values.put(Entry.FoodEntryV2.COLUNM_CARBO, foodsList.get(i).getCarbo());
                values.put(Entry.FoodEntryV2.COLUNM_FATT, foodsList.get(i).getFatt());
                values.put(Entry.FoodEntryV2.COLUNM_PROTEIN, foodsList.get(i).getProt());
                values.put(Entry.FoodEntryV2.COLUNM_FIBER, foodsList.get(i).getFiber());

                dbHelper.addFood(values, 2);
                progressDialog.setProgress((int) (100 * i / foodsList.size()));
            }

            Log.e(TAG, "onPostExecute: " + dbHelper.countDatabaseSize());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();
//            SharedPreferences.Editor ed = sharedPreferences.edit();
//            ed.putBoolean("activity_executed", true);
//            ed.apply();

            Paper.book().write("food_version_code", fetchVersionCode);
            Log.e(TAG, "onSuccess: " + "저장 완료 ");
            startActivity(new Intent(FoodFetchActivity.this, ControlCenterv2.class));
            finish();

            super.onPostExecute(s);
        }
    }

    /**
     * The type Background task.
     */
    class BackgroundTask extends AsyncTask<Void, Void, String> {
        /**
         * The Progress dialog.
         */
        ProgressDialog progressDialog;
        /**
         * The Db helper.
         */
        DBHelper dbHelper;

        /**
         * Instantiates a new Background task.
         */
        public BackgroundTask() {

            dbHelper = new DBHelper(FoodFetchActivity.this);
            progressDialog = new ProgressDialog(FoodFetchActivity.this);

            Log.e(TAG, "BackgroundTask: " + fileLength + count);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("식품 데이터 다운로드 중... 3만개 이상의 데이터를 가져오고 있어요");
            progressDialog.setCancelable(false);
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            for (Food f : arrayList) {
                count++;
                dbHelper.addFood(f);
                progressDialog.setProgress((int) (100 * count / fileLength));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean("activity_executed", true);
            ed.apply();

            startActivity(new Intent(FoodFetchActivity.this, ControlCenterv2.class));
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}
