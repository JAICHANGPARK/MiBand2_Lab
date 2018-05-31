package nodomain.knu2018.bandutils.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import java.util.Calendar;
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
import nodomain.knu2018.bandutils.activities.initfood.SearchFoodActivity;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.database.WriteEntry;
import nodomain.knu2018.bandutils.util.ObservableScrollView;
import nodomain.knu2018.bandutils.util.ProgressRequestBody;
import nodomain.knu2018.bandutils.util.ResultHolder;
import nodomain.knu2018.bandutils.util.UploadCallBacks;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static nodomain.knu2018.bandutils.Const.MealConst.BEEF_UNIT_KCAL;
import static nodomain.knu2018.bandutils.Const.MealConst.FAT_UNIT_KCAL;
import static nodomain.knu2018.bandutils.Const.MealConst.FRUIT_UNIT_KCAL;
import static nodomain.knu2018.bandutils.Const.MealConst.GOKRYU_UNIT_KCAL;
import static nodomain.knu2018.bandutils.Const.MealConst.MILK_UNIT_KCAL;
import static nodomain.knu2018.bandutils.Const.MealConst.VEGETABLE_UNIT_KCAL;


/**
 * ____  ____  _________    __  ____       _____    __    __ __ __________
 * / __ \/ __ \/ ____/   |  /  |/  / |     / /   |  / /   / //_// ____/ __ \
 * / / / / /_/ / __/ / /| | / /|_/ /| | /| / / /| | / /   / ,<  / __/ / /_/ /
 * / /_/ / _, _/ /___/ ___ |/ /  / / | |/ |/ / ___ |/ /___/ /| |/ /___/ _, _/
 * /_____/_/ |_/_____/_/  |_/_/  /_/  |__/|__/_/  |_/_____/_/ |_/_____/_/ |_|
 * <p>
 * <p>
 * Created by Dreamwalker on 2018-05-25.
 */
public class PreviewActivity extends AppCompatActivity implements UploadCallBacks {

    private static final String TAG = "PreviewActivity";
    private static final String BASE_URL = "http://kangwonelec.com/";

    /**
     * The Image view.
     */
    @BindView(R.id.image)
    ImageView imageView;

    /**
     * The Video view.
     */
    @BindView(R.id.video)
    VideoView videoView;

    /**
     * The Actual resolution.
     */
    @BindView(R.id.actualResolution)
    TextView actualResolution;

    /**
     * The Approx uncompressed size.
     */
    @BindView(R.id.approxUncompressedSize)
    TextView approxUncompressedSize;

    /**
     * The Capture latency.
     */
    @BindView(R.id.captureLatency)
    TextView captureLatency;

    /**
     * The Save button.
     */
    @BindView(R.id.saveButton)
    Button saveButton;

    /**
     * The Bubble seek bar 1.
     */
    @BindView(R.id.seek_bar_1)
    BubbleSeekBar bubbleSeekBar1;
    /**
     * The Bubble seek bar 2.
     */
    @BindView(R.id.seek_bar_2)
    BubbleSeekBar bubbleSeekBar2;
    /**
     * The Bubble seek bar 3.
     */
    @BindView(R.id.seek_bar_3)
    BubbleSeekBar bubbleSeekBar3;
    /**
     * The Bubble seek bar 4.
     */
    @BindView(R.id.seek_bar_4)
    BubbleSeekBar bubbleSeekBar4;
    /**
     * The Bubble seek bar 5.
     */
    @BindView(R.id.seek_bar_5)
    BubbleSeekBar bubbleSeekBar5;
    /**
     * The Bubble seek bar 6.
     */
    @BindView(R.id.seek_bar_6)
    BubbleSeekBar bubbleSeekBar6;

    /**
     * The Bubble seek bar 7.
     */
    @BindView(R.id.seek_bar_7)
    BubbleSeekBar bubbleSeekBar7;

    /**
     * The Fab.
     */
    @BindView(R.id.fab)
    FloatingActionButton fab;

    /**
     * The M obs scroll view.
     */
    @BindView(R.id.demo_4_obs_scroll_view)
    ObservableScrollView mObsScrollView;

    /**
     * The Bitmap.
     */
    Bitmap bitmap;
    /**
     * The Type.
     */
    String type;

    /**
     * The Uri.
     */
    Uri uri;
    /**
     * The Tmp file.
     */
    File tmpFile;
    /**
     * The Path.
     */
    File path;
    /**
     * The Compressed image.
     */
    File compressedImage;

    /**
     * The Image index.
     */
    String imageIndex;
    /**
     * The UserInfo uuid.
     */
    String userUUID;
    /**
     * The M service.
     */
    IUploadAPI mService;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;

    /**
     * The Content values.
     */
    ContentValues contentValues;
    /**
     * The Write bsdb helper.
     */
    WriteBSDBHelper writeBSDBHelper;
    /**
     * The Sq lite database.
     */
    SQLiteDatabase sqLiteDatabase;

    /**
     * The Network info.
     */
    NetworkInfo networkInfo;

    private IUploadAPI getAPIUpload() {
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }


    /**
     * The Int gokryu.
     */
    int intGokryu = 0;
    /**
     * The Int beef.
     */
    int intBeef = 0;
    /**
     * The Int vegetable.
     */
    int intVegetable = 0;
    /**
     * The Int fat.
     */
    int intFat = 0;
    /**
     * The Int milk.
     */
    int intMilk = 0;
    /**
     * The Int fruit.
     */
    int intFruit = 0;

    /**
     * The Gokryu value.
     */
// TODO: 2018-05-05 DB 순서대로 정렬
    String gokryuValue = "0";
    /**
     * The Beef value.
     */
    String beefValue = "0";
    /**
     * The Vegetable value.
     */
    String vegetableValue = "0";
    /**
     * The Fat value.
     */
    String fatValue = "0";
    /**
     * The Milk value.
     */
    String milkValue = "0";
    /**
     * The Fruit value.
     */
    String fruitValue = "0";

    /**
     * The Exchange value.
     */
    String exchangeValue;
    /**
     * The Satisfaction.
     */
    String satisfaction;
    /**
     * The Kcal value.
     */
    String kcalValue;

    /**
     * The UserInfo name.
     */
    String userName;

    /**
     * The Init date.
     */
    String initDate;
    /**
     * The Init time.
     */
    String initTime;
    /**
     * The Start date.
     */
    String startDate;
    /**
     * The Start time.
     */
    String startTime;
    /**
     * The End date.
     */
    String endDate;
    /**
     * The End time.
     */
    String endTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ButterKnife.bind(this);
        setupToolbar();
        Paper.init(this);

        mService = getAPIUpload();

        type = Paper.book().read("mealType");
        userName = Paper.book().read("userName");
        userUUID = Paper.book().read("userUUIDV2");

        imageIndex = convertImageFileName(type);

        Log.e(TAG, "onCreate:  type " + type);
        Log.e(TAG, "onCreate: imageIndex  " + imageIndex);
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

        progressDialog = new ProgressDialog(PreviewActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Uploading");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);


        initDataBases();
        initBubbleSeekBar();
    }


    private void initDataBases() {

        writeBSDBHelper = new WriteBSDBHelper(this);
        sqLiteDatabase = writeBSDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    private void initBubbleSeekBar() {

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

        bubbleSeekBar1.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat);
                Log.e(TAG, "getProgressOnFinally: " + progress);
                intGokryu = progress;
                gokryuValue = String.valueOf(progress);
            }

        });

        bubbleSeekBar2.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat);
                Log.e(TAG, "getProgressOnFinally: " + progress);
                intBeef = progress;
                beefValue = String.valueOf(progress);
            }
        });

        bubbleSeekBar3.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intVegetable = progress;
                vegetableValue = String.valueOf(vegetableValue);

            }
        });
        bubbleSeekBar4.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intFat = progress;
                fatValue = String.valueOf(fatValue);
            }
        });
        bubbleSeekBar5.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intMilk = progress;
                milkValue = String.valueOf(progress);
            }
        });
        bubbleSeekBar6.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                intFruit = progress;
                fruitValue = String.valueOf(progress);
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

    /**
     * On click floating action button.
     */
    @OnClick(R.id.fab)
    public void onClickFloatingActionButton() {

        startActivity(new Intent(PreviewActivity.this, SearchFoodActivity.class));

    }

    /**
     * Save snap.
     */
    @OnClick(R.id.saveButton)
    public void saveSnap() {
        // TODO: 2018-05-26 네트워크 연결 상태를 가져옵니다. - Dreamwalker.
        networkInfo = getNetworkInfo();

        if (bitmap != null) {
            saveBitmaptoJpeg(bitmap, "BandUtil/images", "temp");
            compressImage("BandUtil/images", "temp");
            //uploadFile();
        } else {
            Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }

//        uri = Uri.fromFile(compressedImage);

//        if (compressedImage != null) {
//            uploadFile();
//        } else {
//            Snackbar.make(getWindow().getDecorView().getRootView(), "사진을 업로드하세요", Snackbar.LENGTH_SHORT).show();
//        }

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
     * 현재 시간을 가져오는 메소드
     *
     * @return
     * @author : 박제창 (Dreamwalker)
     */
    private String[] getNowTime() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        String initDate = simpleDateFormat.format(now.getTime());
        String initTime = simpleTimeFormat.format(now.getTime());
        return new String[]{initDate, initTime};
    }

    /**
     * 타입 값의 마지막 문자열을 받아와 전과 후 비트 처리를 하기위한 메소드
     *
     * @param type
     * @return
     * @author : 박제창 (Dreamwalker)
     */
    private boolean divideBeforeAfter(String type) {
        String s = "" + type.charAt(type.length() - 1);
        Log.e(TAG, "divideBeforeAfter: " + s);
        if (s.equals("전")) {
            return true;
        } else if (s.equals("후")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 입력된 교환단위를 섭취 칼로리로 변환해주는 메소드 입니다.
     *
     * @param intGokryu
     * @param intBeef
     * @param intVegetable
     * @param intFat
     * @param intMilk
     * @param intFruit
     * @return
     * @author : 박제창 (Dreamwalker)
     */
    private String calculateKcal(int intGokryu, int intBeef, int intVegetable, int intFat, int intMilk, int intFruit) {
        String result = null;

        int totalKcal = (intGokryu * GOKRYU_UNIT_KCAL) + (intBeef * BEEF_UNIT_KCAL) +
                (intVegetable * VEGETABLE_UNIT_KCAL) + (intFat * FAT_UNIT_KCAL) +
                (intMilk * MILK_UNIT_KCAL) + (intFruit * FRUIT_UNIT_KCAL);

        result = String.valueOf(totalKcal);
        return result;
    }

    /**
     * 입력된 교환단위를 전체 교환단위로 변환해주는 메소드 입니다.
     *
     * @param intGokryu
     * @param intBeef
     * @param intVegetable
     * @param intFat
     * @param intMilk
     * @param intFruit
     * @return
     */
    private String calculateExchange(int intGokryu, int intBeef, int intVegetable, int intFat, int intMilk, int intFruit) {
        int totalExchange = intBeef + intFat + intFruit + intGokryu + intMilk + intVegetable;
        return String.valueOf(totalExchange);
    }

    private void uploadFile() {

        progressDialog.show();

        boolean trigger = divideBeforeAfter(type);
        if (trigger) {
            String[] tmpDate = getNowTime();
            initDate = tmpDate[0];
            initTime = tmpDate[1];
            startDate = tmpDate[0];
            startTime = tmpDate[1];
            endDate = "N";
            endTime = "N";
        } else {
            String[] tmpDate = getNowTime();
            initDate = tmpDate[0];
            initTime = tmpDate[1];
            startDate = "N";
            startTime = "N";
            endDate = tmpDate[0];
            endTime = tmpDate[1];
        }

        exchangeValue = calculateExchange(intGokryu, intBeef, intVegetable, intFat, intMilk, intFruit);
        kcalValue = calculateKcal(intGokryu, intBeef, intVegetable, intFat, intMilk, intFruit);

        Log.e(TAG, "uploadFile: " + exchangeValue + ", " + kcalValue);


        if (compressedImage != null) {
            if (networkInfo != null && networkInfo.isConnected()) {

                //            progressDialog = new ProgressDialog(PreviewActivity.this);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMessage("Uploading");
//            progressDialog.setIndeterminate(false);
//            progressDialog.setMax(100);
//            progressDialog.setCancelable(false);
                //progressDialog.show();

//            File file = FileUtils.getFile(this, uri);
//            ProgressRequestBody requestBody = new ProgressRequestBody(file, this);
//            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);
                RequestBody uuidRequest = RequestBody.create(MediaType.parse("text/plain"), userUUID);
                //File file = FileUtils.getFile(this, uri);
                ProgressRequestBody requestBody = new ProgressRequestBody(compressedImage, this);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", compressedImage.getName(), requestBody);

                Call<ResponseBody> comment = mService.userMealRegiste(userName, "Y", type,
                        startDate, startTime, endDate, endTime, "N",
                        gokryuValue, beefValue, vegetableValue, fatValue, milkValue, fruitValue,
                        exchangeValue, kcalValue, satisfaction);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mService.uploadFile(body, uuidRequest).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                //progressDialog.dismiss();
                                //Log.e(TAG, "onResponse: " + response.body());
                                //Toast.makeText(PreviewActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                // TODO: 2018-05-28 사진 업로드 하고 다시 데이터 입력 요청  - 박제창
                                comment.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        progressDialog.dismiss();
                                        if (response.isSuccessful()){
                                            Log.e(TAG, "onResponse: " + response.body().toString());

                                            // TODO: 2018-05-28 내부 데이터 베이스에 저장하는 것/
                                            // TODO: 2018-05-14 base information
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_DATE, initDate );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_TIME, initTime );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_START_DATE, startDate );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_START_TIME, startTime );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_END_DATE, endDate );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_END_TIME, endTime );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_FOOD_TIME, "N" );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_TYPE, type );
                                            // TODO: 2018-05-14 value information
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_GOKRYU, gokryuValue );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_BEEF, beefValue );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_VEGETABLE, vegetableValue );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_FAT, fatValue );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_MILK, milkValue );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_FRUIT, fruitValue );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_EXCHANGE, exchangeValue );
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_KCAL, kcalValue );
                                            // TODO: 2018-05-14 satisfaction information.
                                            contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_SATISFACTION, satisfaction );

                                            long insertDB = sqLiteDatabase.insert(WriteEntry.MealEntry.TABLE_NAME, null, contentValues);
                                            Log.e(TAG, "onResponse:  " + insertDB);

                                            Toast.makeText(PreviewActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PreviewActivity.this, ControlCenterv2.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(PreviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(PreviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            } else {
                Toast.makeText(this, "네트워크를 연결해주세요", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "uri 가 null 값입니다. ", Toast.LENGTH_SHORT).show();
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
                            // TODO: 2018-05-25 주석을 이동해야 할듯.. -----
//                            Intent intent = new Intent(PreviewActivity.this, ControlCenterv2.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
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

        // TODO: 2018-05-25 업로드 전용  -----
        compressedImage = backupDB;
        uri = Uri.fromFile(backupDB);
        Log.e(TAG, "fileCopy: compressedImage = " + compressedImage);
        Log.e(TAG, "fileCopy: uri = " + uri.toString());
        // TODO: 2018-05-25 -----------

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

        uploadFile();
    }


    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }
}
