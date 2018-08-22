/*  Copyright (C) 2016-2018 Alberto, Andreas Shimokawa, Carsten Pfeiffer,
    Daniele Gobbetti

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import ir.mahdi.mzip.zip.ZipArchive;
import nodomain.knu2018.bandutils.GBApplication;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.remote.IUploadAPI;
import nodomain.knu2018.bandutils.remote.RetrofitClient;
import nodomain.knu2018.bandutils.database.DBHandler;
import nodomain.knu2018.bandutils.database.DBHelper;
import nodomain.knu2018.bandutils.util.FileUtils;
import nodomain.knu2018.bandutils.util.GB;
import nodomain.knu2018.bandutils.util.ImportExportSharedPreferences;
import nodomain.knu2018.bandutils.util.ProgressRequestBodyV2;
import nodomain.knu2018.bandutils.util.UploadCallBacks;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;


/**
 * The type Db management activity.
 */
public class DbManagementActivity extends AbstractGBActivity implements UploadCallBacks {
    private static final String TAG = "DbManagementActivity";
    private static final Logger LOG = LoggerFactory.getLogger(DbManagementActivity.class);
    private static SharedPreferences sharedPrefs;
    private ImportExportSharedPreferences shared_file = new ImportExportSharedPreferences();

    /**
     * The Export db button.
     */
    @BindView(R.id.exportDBButton)
    Button exportDBButton;
    /**
     * The Import db button.
     */
    @BindView(R.id.importDBButton)
    Button importDBButton;

    /**
     * The Compress db button.
     */
    @BindView(R.id.compressDBButton)
    Button compressDBButton;

    @BindView(R.id.backupDBButton)
    Button backupDBButton;

    @BindView(R.id.date_time_textview)
    TextView datetimeTextview;

    /**
     * The Zip archive.
     */
    ZipArchive zipArchive;

    //private Button exportDBButton;
    //private Button importDBButton;
    private Button deleteOldActivityDBButton;
    private Button deleteDBButton;
    private TextView dbPath;


    IUploadAPI mService;

    private IUploadAPI getAPIUpload() {
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }

    ProgressDialog progressDialog;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_management);
        ButterKnife.bind(this);

        dbPath = (TextView) findViewById(R.id.activity_db_management_path);
        dbPath.setText("컴퓨터와 연결해 데이터를 추출하세요.");
        zipArchive = new ZipArchive();

        int oldDBVisibility = hasOldActivityDatabase() ? View.VISIBLE : View.GONE;

        deleteOldActivityDBButton = (Button) findViewById(R.id.deleteOldActivityDB);
        deleteOldActivityDBButton.setVisibility(oldDBVisibility);
        deleteOldActivityDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOldActivityDbFile();
            }
        });

        deleteDBButton = (Button) findViewById(R.id.emptyDBButton);
        deleteDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActivityDatabase();
            }
        });

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * On export db button clicked.
     */
    @OnClick(R.id.exportDBButton)
    public void onExportDBButtonClicked() {
        Crashlytics.log(Log.INFO, TAG, "ExportDBButtonClicked");
        exportDB();
        sqliteExport("lifedata.db", "lifedata.sqlite");
        Snackbar.make(getWindow().getDecorView().getRootView(), "추출완료", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * On import db button clicked.
     */
    @OnClick(R.id.importDBButton)
    public void onImportDBButtonClicked() {
        importDB();
    }

    /**
     * On compress db button clicked.
     * 일부 휴대폰에서 압축 예외 처리가 되는 경우가 발생하고 있습니다. - 박제창
     */
    @OnClick(R.id.compressDBButton)
    public void onCompressDBButtonClicked() {
        Crashlytics.log(Log.INFO, TAG, "CompressDBButtonClicked button clicked.");
        zipFile();
    }

    @OnClick(R.id.backupDBButton)
    public void onBackupDBButtonClicked() {
        Crashlytics.log(Log.INFO, TAG, "onBackupDBButtonClicked button clicked.");
        backupDB();

    }


    /**
     * Sqlite export.
     *
     * @param dbName         the db name
     * @param exportFileName the export file name
     */
// TODO: 2018-05-06 데이터베이스 추출하는 코드
    public void sqliteExport(String dbName, String exportFileName) {
        //Context ctx = this; // for Activity, or Service. Otherwise simply get the context.
        //String dbname = "mydb.db";
        // dbpath = ctx.getDatabasePath(dbname);
        try {
            File data = Environment.getDataDirectory();
            File sd = Environment.getExternalStorageDirectory();

            Log.e(TAG, "getDataDirectory:  - " + data.toString());
            Log.e(TAG, "getExternalStorageDirectory:  - " + sd.toString());

            if (sd.canWrite()) {
                String currentDBPath = "/data/nodomain.knu2018.bandutils/databases/" + dbName;
                // TODO: 2018-05-06   "/Android/data/com.dreamwalker.knu2018.dteacher/files/" + exportFileName;
                String backupDBPath = "/BandUtil/data/files/" + exportFileName;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                if (backupDB.exists()) {
                    //Toast.makeText(this, "DB Export Complete!!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Crashlytics.log(Log.ERROR, TAG, "sqliteExport Exception");
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    /**
     * 네트워크 시스템 연결 객체를 가져오는 메소드
     *
     * @return
     * @author : 박제창 (Dreamwalker)
     */
    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;

    }

    /**
     * zip 압축 파일을 업로드하는 메소드 입니다.
     *
     * @author : 박제창(Dreamwalker)
     */
    private void backupDB() {

        progressDialog = new ProgressDialog(DbManagementActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);

        String userUUID = Paper.book().read("userUUIDV2");

        File data = Environment.getExternalStorageDirectory();
        String lifeDBName = "lifedata.sqlite";
        String zipFileName = "band_util_db.zip";
        String backupDBPath = "/BandUtil/data/files/";
        String zipPath = "/BandUtil/data/files/" + zipFileName;

        File file = new File(data, zipPath);
        File backupPath = new File(data, backupDBPath);

        mService = getAPIUpload();
        networkInfo = getNetworkInfo();

        if (!backupPath.exists()) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "DB 추출을 먼저 진행해야합니다.", Snackbar.LENGTH_SHORT).show();
        }
        if (!file.exists()) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "DB 압축을 먼저 진행해야합니다.", Snackbar.LENGTH_SHORT).show();
        } else {

            Log.e(TAG, "backupDB: " + file.getName() + ", " + file.getPath());
            Snackbar.make(getWindow().getDecorView().getRootView(), "압축 파일 확인 .", Snackbar.LENGTH_SHORT).show();
            RequestBody uuidRequest = RequestBody.create(MediaType.parse("text/plain"), userUUID);
            ProgressRequestBodyV2 requestBody = new ProgressRequestBodyV2(file, this);
            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);

            if (networkInfo != null && networkInfo.isConnected()) {
                progressDialog.show();
                mService.backupFile(body, uuidRequest).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
                            String now = "데이터 백업 일자 : " + simpleDateFormat.format(calendar.getTime());
                            datetimeTextview.setText(now);
                            Snackbar.make(getWindow().getDecorView().getRootView(), "서버 백업 성공", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<String> call, Throwable t) {
                        progressDialog.dismiss();
                        Crashlytics.log(Log.INFO, TAG, t.getMessage());
                        Toast.makeText(DbManagementActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), "인터넷을 연결해주세요.", Snackbar.LENGTH_SHORT).show();
            }

        }

    }

    /**
     * 파일 압축하는 메소드
     */
    private void zipFile() {

        File data = Environment.getExternalStorageDirectory();
        String zipFileName = "band_util_db.zip";
        String backupDBPath = "/BandUtil/data/files/";
        String zipPath = "/BandUtil/data/files/" + zipFileName;
        String fileCheckPath = "/BandUtil/data/files/" + "lifedata.sqlite";

        File file = new File(data, zipPath);
        File fileCheck = new File(data, fileCheckPath);

        if (!fileCheck.exists()) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "DB 추출을 먼저 진행해야합니다.", Snackbar.LENGTH_SHORT).show();
        }

        if (file.exists()) {
            file.delete();
        }


        zipArchive.zip(new File(data, backupDBPath).toString(), new File(data, zipPath).toString(), "");

        File zipFile = new File(data, zipPath);
        Uri path = Uri.fromFile(zipFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/zip");
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(shareIntent, "데이터베이스공유"));

        //ZipUtil.pack(new File(data, backupDBPath), new File(data, zipPath));
//        Snackbar.make(getWindow().getDecorView().getRootView(), "압축완료", Snackbar.LENGTH_SHORT)
//                .setAction("공유", v -> {
//                    String fileName = getIntent().getStringExtra("filename");
//
//                }).show();
    }


    private boolean hasOldActivityDatabase() {
        return new DBHelper(this).existsDB("ActivityDatabase");
    }

    private String getExternalPath() {
        try {
            return FileUtils.getExternalFilesDir().getAbsolutePath();
        } catch (Exception ex) {
            LOG.warn("Unable to get external files dir", ex);
        }
        return getString(R.string.dbmanagementactivvity_cannot_access_export_path);
    }

    private void exportShared() {
        // BEGIN EXAMPLE
        File myPath = null;
        try {
            myPath = FileUtils.getExternalFilesDir();
            File myFile = new File(myPath, "Export_preference");
            shared_file.exportToFile(sharedPrefs, myFile, null);
        } catch (IOException ex) {
            GB.toast(this, getString(R.string.dbmanagementactivity_error_exporting_shared, ex.getMessage()), Toast.LENGTH_LONG, GB.ERROR, ex);
        }
    }

    private void importShared() {
        // BEGIN EXAMPLE
        File myPath = null;
        try {
            myPath = FileUtils.getExternalFilesDir();
            File myFile = new File(myPath, "Export_preference");
            shared_file.importFromFile(sharedPrefs, myFile);
        } catch (Exception ex) {
            GB.toast(DbManagementActivity.this, getString(R.string.dbmanagementactivity_error_importing_db, ex.getMessage()), Toast.LENGTH_LONG, GB.ERROR, ex);
        }
    }

    private void exportDB() {
        try (DBHandler dbHandler = GBApplication.acquireDB()) {
            exportShared();
            DBHelper helper = new DBHelper(this);
            File dir = FileUtils.getExternalFilesDir();
            File destFile = helper.exportDB(dbHandler, dir);
            Toast.makeText(this, "Export Completes", Toast.LENGTH_SHORT).show();
            //GB.toast(this, getString(R.string.dbmanagementactivity_exported_to, destFile.getAbsolutePath()), Toast.LENGTH_LONG, GB.INFO);
        } catch (Exception ex) {
            GB.toast(this, getString(R.string.dbmanagementactivity_error_exporting_db, ex.getMessage()), Toast.LENGTH_LONG, GB.ERROR, ex);
        }
    }

    private void importDB() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(R.string.dbmanagementactivity_import_data_title)
                .setMessage(R.string.dbmanagementactivity_overwrite_database_confirmation)
                .setPositiveButton(R.string.dbmanagementactivity_overwrite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try (DBHandler dbHandler = GBApplication.acquireDB()) {
                            importShared();
                            DBHelper helper = new DBHelper(DbManagementActivity.this);
                            File dir = FileUtils.getExternalFilesDir();
                            SQLiteOpenHelper sqLiteOpenHelper = dbHandler.getHelper();
                            File sourceFile = new File(dir, sqLiteOpenHelper.getDatabaseName());
                            helper.importDB(dbHandler, sourceFile);
                            helper.validateDB(sqLiteOpenHelper);
                            GB.toast(DbManagementActivity.this, getString(R.string.dbmanagementactivity_import_successful), Toast.LENGTH_LONG, GB.INFO);
                        } catch (Exception ex) {
                            GB.toast(DbManagementActivity.this, getString(R.string.dbmanagementactivity_error_importing_db, ex.getMessage()), Toast.LENGTH_LONG, GB.ERROR, ex);
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void deleteActivityDatabase() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(R.string.dbmanagementactivity_delete_activity_data_title)
                .setMessage(R.string.dbmanagementactivity_really_delete_entire_db)
                .setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (GBApplication.deleteActivityDatabase(DbManagementActivity.this)) {
                            GB.toast(DbManagementActivity.this, getString(R.string.dbmanagementactivity_database_successfully_deleted), Toast.LENGTH_SHORT, GB.INFO);
                        } else {
                            GB.toast(DbManagementActivity.this, getString(R.string.dbmanagementactivity_db_deletion_failed), Toast.LENGTH_SHORT, GB.INFO);
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void deleteOldActivityDbFile() {
        new AlertDialog.Builder(this).setCancelable(true);
        new AlertDialog.Builder(this).setTitle(R.string.dbmanagementactivity_delete_old_activity_db);
        new AlertDialog.Builder(this).setMessage(R.string.dbmanagementactivity_delete_old_activitydb_confirmation);
        new AlertDialog.Builder(this).setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (GBApplication.deleteOldActivityDatabase(DbManagementActivity.this)) {
                    GB.toast(DbManagementActivity.this, getString(R.string.dbmanagementactivity_old_activity_db_successfully_deleted), Toast.LENGTH_SHORT, GB.INFO);
                } else {
                    GB.toast(DbManagementActivity.this, getString(R.string.dbmanagementactivity_old_activity_db_deletion_failed), Toast.LENGTH_SHORT, GB.INFO);
                }
            }
        });
        new AlertDialog.Builder(this).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        new AlertDialog.Builder(this).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }
}
