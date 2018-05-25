package nodomain.knu2018.bandutils.activities.initfood;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.Remote.FoodDataFetch;
import nodomain.knu2018.bandutils.activities.ControlCenterv2;
import nodomain.knu2018.bandutils.database.fooddb.DBHelper;
import nodomain.knu2018.bandutils.model.foodmodel.Food;
import nodomain.knu2018.bandutils.model.foodmodel.Foods;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodFetchActivity extends AppCompatActivity {

    private static final String TAG = "FoodFetchActivity";
    private static final String APP_VERSION_KEY = "food_db_version";
    public static final String API_URL = "http://www.kangwonelec.com/";
    private static final int REQUEST_PERMISSION = 1000;

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    SharedPreferences sharedPreferences;

    long fileLength;
    long count = 0;

    String dbVersion;
    Retrofit retrofit;
    FoodDataFetch mService;

    AlertDialog dialog;
    ProgressDialog progressDialog;
    DBHelper dbHelper;

    ArrayList<Foods> resultList;
    ArrayList<Food> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_fetch);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, REQUEST_PERMISSION);
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

        sharedPreferences = getSharedPreferences("ActivityFoodDatabase", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, ControlCenterv2.class);
            startActivity(intent);
            finish();
        } else {
//            SharedPreferences.Editor ed = pref.edit();
//            ed.putBoolean("activity_executed", true);
//            ed.commit();
        }

        Paper.init(this);
        dialog = new SpotsDialog(this);
        dbHelper = new DBHelper(this);
        arrayList = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(FoodDataFetch.class);
        resultList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("서버로부터 필수 데이터 다운로드 중...");
        progressDialog.setCancelable(false);

    }

    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    // [START display_welcome_message]
    private void displayWelcomeMessage() {
        // [START get_config_values]
        String databaseVersion = mFirebaseRemoteConfig.getString(APP_VERSION_KEY);
        Log.e(TAG, "displayWelcomeMessage: " + databaseVersion );

        if (dbVersion.equals(databaseVersion)){
            Log.e(TAG, "displayWelcomeMessage: 최신버전 입니다." );
            startActivity(new Intent(FoodFetchActivity.this, ControlCenterv2.class));
            finish();
        }else{
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
                    Log.e(TAG, "onComplete: " + "Database Version info Fetch Failed" );
                    //Toast.makeText(ControlCenterv2.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                }
                displayWelcomeMessage();
            }
        });
        // [END fetch_config_with_callback]
    }


    private void fetchJson() {

        progressDialog.show();
        Call<Foods> comment = mService.fetchTotalFood("food_total.php");

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
                    if (arrayList.size() == fileLength){
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    fetchJson();
                } else {
                    Toast.makeText(this, "Permission denine", Toast.LENGTH_SHORT).show();
                }
        }
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        DBHelper dbHelper;
        public BackgroundTask() {

            dbHelper = new DBHelper(FoodFetchActivity.this);
            progressDialog = new ProgressDialog(FoodFetchActivity.this);

            Log.e(TAG, "BackgroundTask: " + fileLength + count );
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

            for (Food f : arrayList){
                count++;
                dbHelper.addFood(f);
                progressDialog.setProgress((int)(100 * count / fileLength));
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
