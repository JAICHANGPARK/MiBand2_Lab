package nodomain.knu2018.bandutils.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.util.StorageCheck;
import tyrantgit.explosionfield.ExplosionField;

public class StorageCheckActivity extends AppCompatActivity {
    private static final String TAG = "StorageCheckActivity";

    private static final String EX_DATA_PATH = "/BandUtil/data/files";
    private static final String EX_IMAGE_PATH = "/BandUtil/images";

    private static final int REQUEST_PERMISSION = 1000;
    private static final int PICK_FILE_REQUEST = 1001;

    StorageCheck storageCheck;
    ExplosionField explosionField;

    @BindView(R.id.actualCache)
    TextView actualCache;

    @BindView(R.id.cacheClearButton)
    Button cacheClearButton;

    @BindView(R.id.actualDB)
    TextView actualDB;
    @BindView(R.id.DBClearButton)
    Button DBClearButton;
    @BindView(R.id.actualImage)
    TextView actualImage;
    @BindView(R.id.imageClearButton)
    Button imageClearButton;

    long dbFileSize = 0;
    long imageFileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_check);
        ButterKnife.bind(this);
        explosionField = ExplosionField.attach2Window(this);
        storageCheck = new StorageCheck(this);
        setupToolbar();

        File external = Environment.getExternalStorageDirectory();
        File dataPath = new File(external, EX_DATA_PATH);
        File imagePath = new File(external, EX_IMAGE_PATH);

        dbFileSize = StorageCheck.checkSize(dbFileSize, dataPath);
        imageFileSize = StorageCheck.checkSize(imageFileSize, imagePath);


        String cacheSize = storageCheck.getCachesSize(this);
//        String dbSize = storageCheck.getExternalFileSize(EX_DATA_PATH);
        String dbSize = storageCheck.getReadableFileSize(dbFileSize);
        String imageSize = storageCheck.getReadableFileSize(imageFileSize);

        actualCache.setText(cacheSize);
        actualDB.setText(dbSize);
        actualImage.setText(imageSize);


        Log.e(TAG, "onCreate: tmps " + dbFileSize  + "," + imageFileSize);

        //Log.e(TAG, "onCreate: " + folderMemoryCheck("/BandUtil/data"));
    }

    @OnClick(R.id.cacheClearButton)
    public void onClickCacheClear(View v) {
        // TODO: 2018-05-19 정적 매소드를 호출하여 캐시 메모리 데이터 삭제
        StorageCheck.clearApplicationData(this);
        actualCache.setText(storageCheck.getCachesSize(this) + "Bytes");
        explosionField.explode(v);
        cacheClearButton.setVisibility(View.GONE);
        Snackbar.make(getWindow().getDecorView().getRootView(), "캐시 비우기 완료", Snackbar.LENGTH_SHORT).show();

    }

    @OnClick(R.id.DBClearButton)
    public void onClickDBClear(View v) {
        StorageCheck.clearExternalFolder(EX_DATA_PATH);
        explosionField.explode(v);
        DBClearButton.setVisibility(View.GONE);
        actualDB.setText(storageCheck.getExternalFileSize(EX_DATA_PATH));
    }

    @OnClick(R.id.imageClearButton)
    public void onClickImageClear(View v) {

        StorageCheck.clearExternalFolder(EX_IMAGE_PATH);
        explosionField.explode(v);
        imageClearButton.setVisibility(View.GONE);
        actualImage.setText(storageCheck.getExternalFileSize(EX_IMAGE_PATH));

//        boolean result = StorageCheck.clearExternalFolder(EX_IMAGE_PATH);
//        if (result){
//            explosionField.explode(v);
//            imageClearButton.setVisibility(View.GONE);
//        }else {
//            Snackbar.make(v,"삭제 실패", Snackbar.LENGTH_SHORT).show();
//        }
    }



    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            View toolbarView = getLayoutInflater().inflate(R.layout.action_bar_storage, null, false);
            TextView titleView = toolbarView.findViewById(R.id.toolbar_title);
            titleView.setText(Html.fromHtml("<b>Storage</b>Check"));

            getSupportActionBar().setCustomView(toolbarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }




}
