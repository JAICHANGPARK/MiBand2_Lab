package nodomain.knu2018.bandutils.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.util.ResultHolder;

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";
    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.video)
    VideoView videoView;

    @BindView(R.id.actualResolution)
    TextView actualResolution;

    @BindView(R.id.approxUncompressedSize)
    TextView approxUncompressedSize;

    @BindView(R.id.captureLatency)
    TextView captureLatency;

    @BindView(R.id.saveButton)
    Button saveButton;

    Bitmap bitmap;
    String type;

    File compressedImage;

    String imageIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        setupToolbar();
        Paper.init(this);
        type = Paper.book().read("mealType");

        imageIndex = convertImageFileName(type);

        Log.e(TAG, "onCreate:  type " + type);
        Log.e(TAG, "onCreate: imageIndex  " + imageIndex );
        byte[] jpeg = ResultHolder.getImage();
        File video = ResultHolder.getVideo();

        if (jpeg != null) {
            imageView.setVisibility(View.VISIBLE);

            bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

            if (bitmap == null) {
                finish();
                return;
            }

            imageView.setImageBitmap(bitmap);

            actualResolution.setText(bitmap.getWidth() + " x " + bitmap.getHeight());
            approxUncompressedSize.setText(getApproximateFileMegabytes(bitmap) + "MB");
            captureLatency.setText(ResultHolder.getTimeToCallback() + " milliseconds");
        } else if (video != null) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(video.getAbsolutePath()));
            MediaController mediaController = new MediaController(this);
            mediaController.setVisibility(View.GONE);
            videoView.setMediaController(mediaController);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.start();

                    float multiplier = (float) videoView.getWidth() / (float) mp.getVideoWidth();
                    videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (mp.getVideoHeight() * multiplier)));
                }
            });
            //videoView.start();
        } else {
            finish();
            return;
        }

        Log.e(TAG, "onCreate: getCacheDir " + getCacheDir());
        File getCacheFile = getCacheDir();
        Log.e(TAG, "onCreate: getCacheDir listFiles " + getCacheFile.listFiles());
    }

    private String convertImageFileName(String types) {
        String fileIndex = null;
        if (types != null) {
            switch (types) {
                case "아침식사전":
                    fileIndex = "bb_";
                    break;
                case "아침식사후":
                    fileIndex = "bf_";
                    break;
                case "점심식사전":
                    fileIndex = "lb_";
                    break;
                case "점심식사후":
                    fileIndex = "lf_";
                    break;
                case "저녁식사전":
                    fileIndex = "db_";
                    break;
                case "저녁식사후":
                    fileIndex = "df_";
                    break;
                case "간식":
                    fileIndex = "snack_";
                    break;
                default:
                    break;
            }
        } else {
            fileIndex = "unknown_";
        }

        return fileIndex;
    }

    @OnClick(R.id.saveButton)
    public void saveSnap() {

        if (bitmap != null) {
            saveBitmaptoJpeg(bitmap, "BandUtil/images", "temp");
            compressImage("BandUtil/images", "temp");
        } else {
            Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void compressImage(String folder, String name) {

        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name + file_name;
        File files = new File(string_path);

        try {

            Luban.compress(this, files)
                    .putGear(Luban.FIRST_GEAR)
                    .launch(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            Log.e(TAG, "onStart: " + "start");
                        }

                        @Override
                        public void onSuccess(File file) {

                            Log.e(TAG, "onSuccess: " + file);
                            fileCopy(file);
                            file.delete();
                            //finish();

                            Intent intent = new Intent(PreviewActivity.this, ControlCenterv2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

        } catch (IllegalAccessError error) {
            error.printStackTrace();
        }


    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            View toolbarView = getLayoutInflater().inflate(R.layout.action_bar, null, false);
            TextView titleView = toolbarView.findViewById(R.id.toolbar_title);
            titleView.setText(Html.fromHtml("<b>Awesome!</b>Snap"));

            getSupportActionBar().setCustomView(toolbarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private static float getApproximateFileMegabytes(Bitmap bitmap) {
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024 / 1024;
    }


    /**
     * Image SDCard Save (input Bitmap -> saved file JPEG)
     * Writer intruder(Kwangseob Kim)
     *
     * @param bitmap : input bitmap file
     * @param folder : input folder name
     * @param name   : output file name
     */
    public static void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            Log.e(TAG, "saveBitmaptoJpeg: " + "Save Complete");
        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_preview, menu);
////        return super.onCreateOptionsMenu(menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_save:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    /**
     * 압축되어 나온 캐쉬메모리 내부의 이미지 파일을 외부 메모리에 복사하는 메소드.
     *
     * @param file
     * @Author : JAICHANGPARK.
     */
    private void fileCopy(File file) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREA);
        String fileDate = simpleDateFormat.format(date);

        Log.e(TAG, "fileCopy: called 1 ");
        File externalPath = Environment.getExternalStorageDirectory();
        String folderPath = "/BandUtil/images/";
        //String backupDBPath = "/BandUtil/images/" + file.getName();
        // TODO: 2018-05-20 복사 저장하는 이미지 파일의 이름을 변경해야한다.
        String backupDBPath = "/BandUtil/images/" + imageIndex + fileDate + ".jpg";

        File mkdirFile = new File(externalPath, folderPath); // 외부 폴더 경로
        File currentDB = new File(file.getPath()); // 복사할 파일이 저장된 폴더의 경로
        File backupDB = new File(externalPath, backupDBPath); // 파일을 저장할 백업 폴더의 경로

        if (!mkdirFile.exists()) {
            Log.e(TAG, "fileCopy: " + "create file");
            mkdirFile.mkdirs();
        } else {
            Log.e(TAG, "fileCopy: " + "exists folder");
        }

        try {
            if (externalPath.canWrite()) {
                Log.e(TAG, "fileCopy: called  2");

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                if (backupDB.exists()) {
                    Log.e(TAG, "fileCopy: File Copy Complete!!");
                    //Toast.makeText(this, "File Copy Complete!!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
