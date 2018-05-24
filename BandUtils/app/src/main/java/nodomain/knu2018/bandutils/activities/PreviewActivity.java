package nodomain.knu2018.bandutils.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import com.xw.repo.BubbleSeekBar;

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
import nodomain.knu2018.bandutils.Remote.IUploadAPI;
import nodomain.knu2018.bandutils.Remote.RetrofitClient;
import nodomain.knu2018.bandutils.util.ObservableScrollView;
import nodomain.knu2018.bandutils.util.ProgressRequestBody;
import nodomain.knu2018.bandutils.util.ResultHolder;
import nodomain.knu2018.bandutils.util.UploadCallBacks;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewActivity extends AppCompatActivity implements UploadCallBacks {

    private static final String TAG = "PreviewActivity";
    private static final String BASE_URL = "http://kangwonelec.com/";
    private static final int REQUEST_PERMISSION = 1000;
    private static final int PICK_FILE_REQUEST = 1001;

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

    @BindView(R.id.seek_bar_1)
    BubbleSeekBar bubbleSeekBar1;
    @BindView(R.id.seek_bar_2)
    BubbleSeekBar bubbleSeekBar2;
    @BindView(R.id.seek_bar_3)
    BubbleSeekBar bubbleSeekBar3;
    @BindView(R.id.seek_bar_4)
    BubbleSeekBar bubbleSeekBar4;
    @BindView(R.id.seek_bar_5)
    BubbleSeekBar bubbleSeekBar5;
    @BindView(R.id.seek_bar_6)
    BubbleSeekBar bubbleSeekBar6;

    @BindView(R.id.seek_bar_7)
    BubbleSeekBar bubbleSeekBar7;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.demo_4_obs_scroll_view)
    ObservableScrollView mObsScrollView;


    Bitmap bitmap;
    String type;

    Uri uri;
    File tmpFile;
    File path;

    File compressedImage;

    String imageIndex;

    IUploadAPI mService;
    ProgressDialog progressDialog;

    private IUploadAPI getAPIUpload(){
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }


    int intGokryu = 0;
    int intBeef = 0;
    int intVegetable = 0;
    int intFat = 0;
    int intMilk = 0;
    int intFruit = 0;
    int intTotalExchange = 0;
    int intTotalKcal = 0;
    // TODO: 2018-05-05 DB 순서대로 정렬
    String inputType;
    String typeValue;
    String gokryuValue = "0";
    String beefValue = "0";
    String vegetableValue = "0";
    String fatValue = "0";
    String milkValue = "0";
    String fruitValue = "0";

    String exchangeValue;
    String satisfaction;
    String kcalValue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        setupToolbar();
        Paper.init(this);
        mService = getAPIUpload();

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


        mObsScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                bubbleSeekBar1.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBar2.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBar3.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBar4.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBar5.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBar6.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBar7.correctOffsetWhenContainerOnScrolling();
            }
        });

        initBubbleSeekBar();
    }

    private void initBubbleSeekBar(){

        bubbleSeekBar1.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat);
                Log.e(TAG, "getProgressOnFinally: " + progress);
                intGokryu = progress;
            }

        });

        bubbleSeekBar2.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat);
                Log.e(TAG, "getProgressOnFinally: " + progress );
                intBeef = progress;
            }
        });

        bubbleSeekBar3.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intVegetable = progress;

            }
        });
        bubbleSeekBar4.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intFat  = progress;
            }
        });
        bubbleSeekBar5.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intMilk = progress;
            }
        });
        bubbleSeekBar6.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intFruit = progress;
            }
        });


        satisfaction = "5";

        bubbleSeekBar7.setCustomSectionTextArray((sectionCount, array) -> {
            array.clear();
            array.put(2, "bad");
            array.put(5, "ok");
            array.put(7, "good");
            array.put(9, "great");

            return array;
        });

        bubbleSeekBar7.setProgress(5);

        bubbleSeekBar7.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //super.onProgressChanged(bubbleSeekBar, progress, progressFloat);
                int color;
                if (progress <= 2) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.color_red);
                } else if (progress <= 4) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.color_red_light);
                } else if (progress <= 7) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
                } else if (progress <= 9) {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.color_blue);
                } else {
                    color = ContextCompat.getColor(getApplicationContext(), R.color.color_green);
                }

                bubbleSeekBar.setSecondTrackColor(color);
                bubbleSeekBar.setThumbColor(color);
                bubbleSeekBar.setBubbleColor(color);

                satisfaction = String.valueOf(progress); // TODO: 2018-05-04 정수형 변수를 문자열로 변환
                //bus.post(new TWDataEvent(dateValue, timeValue, inputType, exchangeValue, startTime, endTime, satisfaction, PAGE_NUM));
            }
        });

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

        if (uri != null) {
            uploadFile();
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "사진을 업로드하세요", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void uploadFile() {
        if (uri != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.show();

//            File file = FileUtils.getFile(this, uri);
//            ProgressRequestBody requestBody = new ProgressRequestBody(file, this);
//            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);

            //File file = FileUtils.getFile(this, uri);
            ProgressRequestBody requestBody = new ProgressRequestBody(compressedImage, this);
            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", compressedImage.getName(), requestBody);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.uploadFile(body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            progressDialog.dismiss();
                            Toast.makeText(PreviewActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(PreviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).start();
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


    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }
}
