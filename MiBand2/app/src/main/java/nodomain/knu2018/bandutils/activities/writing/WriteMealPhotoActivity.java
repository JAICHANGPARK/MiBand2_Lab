package nodomain.knu2018.bandutils.activities.writing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
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
import nodomain.knu2018.bandutils.remote.IUploadAPI;
import nodomain.knu2018.bandutils.remote.RetrofitClient;
import nodomain.knu2018.bandutils.activities.ControlCenterv2;
import nodomain.knu2018.bandutils.activities.initfood.SearchFoodActivity;
import nodomain.knu2018.bandutils.util.ObservableScrollView;
import nodomain.knu2018.bandutils.util.ProgressRequestBody;
import nodomain.knu2018.bandutils.util.UploadCallBacks;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ____  ____  _________    __  ____       _____    __    __ __ __________
 * / __ \/ __ \/ ____/   |  /  |/  / |     / /   |  / /   / //_// ____/ __ \
 * / / / / /_/ / __/ / /| | / /|_/ /| | /| / / /| | / /   / ,<  / __/ / /_/ /
 * / /_/ / _, _/ /___/ ___ |/ /  / / | |/ |/ / ___ |/ /___/ /| |/ /___/ _, _/
 * /_____/_/ |_/_____/_/  |_/_/  /_/  |__/|__/_/  |_/_____/_/ |_/_____/_/ |_|
 * <p>
 * Created by Dreamwalker on 2018-05-25.
 */
public class WriteMealPhotoActivity extends AppCompatActivity implements UploadCallBacks {

    private static final String TAG = "WriteMealPhotoActivity";
    private static final String BASE_URL = "http://kangwonelec.com/";

    /**
     * The Image view.
     */
// TODO: 2018-05-26 이미지 처리 부분.
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
     * The Bubble seek bar 1.
     */
// TODO: 2018-05-26 SeekBar 부분
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
     * The Save button.
     */
// TODO: 2018-05-26 버튼 부
    @BindView(R.id.saveButton)
    Button saveButton;
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
     * The Compressed image.
     */
    File compressedImage;
    /**
     * The Uri.
     */
    Uri uri;

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
     * The Int total exchange.
     */
    int intTotalExchange = 0;
    /**
     * The Int total kcal.
     */
    int intTotalKcal = 0;
    /**
     * The Input type.
     */
// TODO: 2018-05-05 DB 순서대로 정렬
    String inputType;
    /**
     * The Type value.
     */
    String typeValue;
    /**
     * The Gokryu value.
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_meal_photo);
        setupToolbar();
        ButterKnife.bind(this);
        Paper.init(this);

        mService = getAPIUpload();

        userUUID = Paper.book().read("userUUIDV2");
        type = Paper.book().read("mealType");
        imageIndex = convertImageFileName(type);

        // TODO: 2018-05-26 WriteMealSelectActivity에서 선택된 파일의 uri 값을 가져옵니다. - Dreamwalker.
        String getString = getIntent().getStringExtra("uri");
        uri = Uri.parse(getString);

        Log.e(TAG, "onCreate: uri" + uri.toString());
        Log.e(TAG, "onCreate: uri.getPath()" + uri.getPath());

        if (uri != null){
            imageView.setVisibility(View.VISIBLE);
            // TODO: 2018-05-26 Glide 를 사용하여 이미지 처리 (메모리에 안정적입니다.)
            Glide.with(this).load(uri).into(imageView);
            bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("path"));

            if (bitmap == null) {
                finish();
                return;
            }

            Log.e(TAG, "onCreate: "  + bitmap.getWidth() +  ", " + bitmap.getHeight());
            Log.e(TAG, "onCreate: " + type );

            actualResolution.setText(bitmap.getWidth() + " x " + bitmap.getHeight());
            approxUncompressedSize.setText(getApproximateFileMegabytes(bitmap) + "MB");
            //captureLatency.setText(ResultHolder.getTimeToCallback() + " milliseconds");
        }

//
//        byte[] jpeg = ResultHolder.getImage();
//        File video = ResultHolder.getVideo();

        initBubbleSeekBar();

    }


    /**
     * On click floating action button.
     */
    @OnClick(R.id.fab)
    public void onClickFloatingActionButton(){

        startActivity(new Intent(WriteMealPhotoActivity.this, SearchFoodActivity.class));

    }

    /**
     * Save snap.
     */
    @OnClick(R.id.saveButton)
    public void saveSnap() {
        // TODO: 2018-05-26 네트워크 연결 상태를 가져옵니다. - Dreamwalker.
        networkInfo = getNetworkInfo();

        if (bitmap != null) {
            saveBitmaptoJpeg(bitmap, "BandUtil/images", "up_temp");
            compressImage("BandUtil/images", "up_temp");
            //uploadFile();
        } else {
            Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 파일 크기를 계산 하는 메소드
     * 비트맵의 크기를 Mbytes로 출력해주는 메소오오드.
     * @author : 박제창 (Dreamwalker)
     * @param bitmap
     * @return
     */
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

    /**
     * 네트워크 메니저 객체를 얻어오고 네트워크 메니저에서 네트워크 정보를 가져오는 메소드
     * @author : 박제창 (Dreamwalker)
     * @return
     */
    private NetworkInfo getNetworkInfo(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  networkInfo;
    }

    /**
     *  툴바 커스텀 하는 메소드
     *  @author : 박제창 (Dreamwalker)
     *
     */
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

    /**
     *  이전 엑티비티에서 전달 받은 타입 문자열 값을 적절한 파일명 인덱스로 변경해주는 메소드 입니다.
     *  @author : 박제창(Dreamwalker)
     *  @date 2018-05-26
     * @param types
     * @return
     */
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
     *  각 식퓸 교환 식품군의 입력 뷰 리스너정의
     *  @author : 박제창(Dreamwalker)
     */
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
            }

        });

        bubbleSeekBar2.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat);
                Log.e(TAG, "getProgressOnFinally: " + progress);
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
                intFat = progress;
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


    /**
     * 그래요..사진 압축하고 복사하는 메소드에요..
     * @author : 박제창(Dreamwalker)
     * @param folder
     * @param name
     */
    private void compressImage(String folder, String name) {

        String file = uri.getPath().toString();

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
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(WriteMealPhotoActivity.this, "파일 압축 실패", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IllegalAccessError error) {
            error.printStackTrace();
        }
    }

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

    private void uploadFile() {

        if (compressedImage != null) {
            if (networkInfo != null && networkInfo.isConnected()){

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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mService.uploadFile(body, uuidRequest).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                //progressDialog.dismiss();
                                //Log.e(TAG, "onResponse: " + response.body());
                                //Toast.makeText(PreviewActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                Toast.makeText(WriteMealPhotoActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteMealPhotoActivity.this, ControlCenterv2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(WriteMealPhotoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }else {
                Toast.makeText(this, "네트워크를 연결해주세요", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "uri 가 null 값입니다. ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }
}
