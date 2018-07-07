package nodomain.knu2018.bandutils.activities.writing;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.xw.repo.BubbleSeekBar;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.Remote.IPatternAPI;
import nodomain.knu2018.bandutils.Remote.RetrofitClient;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.database.WriteEntry;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;

/**
 * ____  ____  _________    __  ____       _____    __    __ __ __________
 * / __ \/ __ \/ ____/   |  /  |/  / |     / /   |  / /   / //_// ____/ __ \
 * / / / / /_/ / __/ / /| | / /|_/ /| | /| / / /| | / /   / ,<  / __/ / /_/ /
 * / /_/ / _, _/ /___/ ___ |/ /  / / | |/ |/ / ___ |/ /___/ /| |/ /___/ _, _/
 * /_____/_/ |_/_____/_/  |_/_/  /_/  |__/|__/_/  |_/_____/_/ |_/_____/_/ |_|
 * <p>
 * <p>
 * Created by Dreamwalker on 2018-05-28.
 */


public class WriteSleepActivity extends AppCompatActivity {

    private static final String TAG = "WriteSleepActivity";

    /**
     * The Close button.
     */
    @BindView(R.id.closeButton)
    ImageView closeButton;
    /**
     * The Done button.
     */
    @BindView(R.id.doneButton)
    ImageView doneButton;

    /**
     * The Start time text view.
     */
    @BindView(R.id.startTimeText)
    TextView startTimeTextView;
    /**
     * The End time text view.
     */
    @BindView(R.id.endTimeText)
    TextView endTimeTextView;
    /**
     * The Start date text view.
     */
    @BindView(R.id.startDateText)
    TextView startDateTextView;
    /**
     * The End date text view.
     */
    @BindView(R.id.endDateText)
    TextView endDateTextView;

    /**
     * The Start time button.
     */
    @BindView(R.id.startTimeButton)
    ImageView startTimeButton;
    /**
     * The End time button.
     */
    @BindView(R.id.endTimeButton)
    ImageView endTimeButton;

    /**
     * The Nice spinner.
     */
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;

    /**
     * The Bubble seek bar.
     */
    @BindView(R.id.seek_bar_1)
    BubbleSeekBar bubbleSeekBar;

    /**
     * The Start date time dialog fragment.
     */
    SwitchDateTimeDialogFragment startDateTimeDialogFragment;
    /**
     * The End date time dialog fragment.
     */
    SwitchDateTimeDialogFragment endDateTimeDialogFragment;

    /**
     * The Simple date format.
     */
    SimpleDateFormat simpleDateFormat;
    /**
     * The Simple time format.
     */
    SimpleDateFormat simpleTimeFormat;
    /**
     * The Now.
     */
    Calendar now;

    /**
     * The Init date.
     */
    String initDate, /**
     * The Init time.
     */
    initTime;
    /**
     * The Input type.
     */
    String inputType, /**
     * The Satisfaction.
     */
    satisfaction;
    /**
     * The Sleep time value.
     */
    String sleepTimeValue;

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

    /**
     * The Long start time.
     */
    long longStartTime;
    /**
     * The Long end time.
     */
    long longEndTime;

    /**
     * The M db helper.
     */
    WriteBSDBHelper mDBHelper;
    /**
     * The Db.
     */
    SQLiteDatabase db;
    /**
     * The Content values.
     */
    ContentValues contentValues;

    /**
     * The Retrofit.
     */
    Retrofit retrofit;
    /**
     * The Service.
     */
    IPatternAPI service;

    private IPatternAPI getAPIUpload() {
        return RetrofitClient.getClient(BASE_URL).create(IPatternAPI.class);
    }

    /**
     * The UserInfo name.
     */
    String userName;
    /**
     * The UserInfo uuid.
     */
    String userUUID;

    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sleep);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }
        ButterKnife.bind(this);
        Paper.init(this);

        initDataBases();

        initDateTimeTextView();
        initStartDTPicker();
        initEndDTPicker();
        initSpinner();
        initBubbleSeekBar();
        initRetrofit();
    }


    /**
     * Retrofit 객체 생성
     * @author : 박제창(Dreamwalker)
     *
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        service = retrofit.create(IPatternAPI.class);
        userName = Paper.book().read("userName");
        userUUID = Paper.book().read("userUUIDV2");

        progressDialog = new ProgressDialog(WriteSleepActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    private void initDataBases() {
        mDBHelper = new WriteBSDBHelper(this);
        db = mDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }


    private void initBubbleSeekBar() {
        satisfaction = "5";

        // OnValueChangeListener
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //super.onProgressChanged(bubbleSeekBar, progress, progressFloat);
                satisfaction = String.valueOf(progress); // TODO: 2018-05-04 정수형 변수를 문자열로 변환
            }
        });
    }

    private void initSpinner() {

        inputType = "저녁";

        List<String> dataset = new LinkedList<>(Arrays.asList("저녁", "낮잠"));
        niceSpinner.attachDataSource(dataset);

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        inputType = "저녁";
                        break;
                    case 1:
                        inputType = "낮잠";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initDateTimeTextView() {
        now = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
        initDate = simpleDateFormat.format(now.getTime());
        initTime = simpleTimeFormat.format(now.getTime());
        longStartTime = now.getTime().getTime();
        longEndTime = now.getTime().getTime();
        Log.e(TAG, "initDateTimeTextView: longStartTime = " + longStartTime);
        Log.e(TAG, "initDateTimeTextView: longEndTime = " + longEndTime);
        startDate = initDate;
        startTime = initTime;
        endDate = initDate;
        endTime = initTime;

        startDateTextView.setText(initDate);
        startTimeTextView.setText(initTime);
        endDateTextView.setText(initDate);
        endTimeTextView.setText(initTime);
    }

    private void initStartDTPicker() {

        startDateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "취침 날짜 시간 선택",
                "OK",
                "Cancel"
        );
        startDateTimeDialogFragment.startAtCalendarView();
        startDateTimeDialogFragment.set24HoursMode(true);

        startDateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        startDateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        startDateTimeDialogFragment.setDefaultDateTime(now.getTime());
        try {
            startDateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        startDateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                // Log.e(TAG, "startDateTimeDialogFragment: " + date.toString());
                // Log.e(TAG, "startDateTimeDialogFragment: " + date.getTime());
                startDate = simpleDateFormat.format(date.getTime());
                startTime = simpleTimeFormat.format(date.getTime());
                longStartTime = date.getTime();
                startDateTextView.setText(startDate);
                startTimeTextView.setText(startTime);
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });
    }

    private void initEndDTPicker() {

        endDateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "기상 날짜 시간 선택",
                "OK",
                "Cancel"
        );
        endDateTimeDialogFragment.startAtCalendarView();
        endDateTimeDialogFragment.set24HoursMode(true);

        endDateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        endDateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        endDateTimeDialogFragment.setDefaultDateTime(now.getTime());
        try {
            endDateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        endDateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                //Log.e(TAG, "endDateTimeDialogFragment: " + date.toString());
                // Log.e(TAG, "endDateTimeDialogFragment: " + date.getTime());

                endDate = simpleDateFormat.format(date.getTime());
                endTime = simpleTimeFormat.format(date.getTime());
                longEndTime = date.getTime();
                endDateTextView.setText(endDate);
                endTimeTextView.setText(endTime);
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });
    }

    /**
     * On start time button listener.
     */
    @OnClick(R.id.startTimeButton)
    public void onStartTimeButtonListener() {
        startDateTimeDialogFragment.show(getSupportFragmentManager(), "startDateTimePicker");
    }

    /**
     * On end time button listener.
     */
    @OnClick(R.id.endTimeButton)
    public void onEndTimeButtonListener() {
        endDateTimeDialogFragment.show(getSupportFragmentManager(), "endDateTimePicker");
    }


    /**
     * On click close button.
     */
    @OnClick(R.id.closeButton)
    public void onClickCloseButton() {
        finish();
    }

    /**
     * On click done button.
     */
    @OnClick(R.id.doneButton)
    public void onClickDoneButton() {
        // TODO: 2018-05-13 Save Button 데이터베이스 저장 기능  
        String tempString = null;
        Date startDates = null;
        SimpleDateFormat dataFormat = new SimpleDateFormat("kk:mm", Locale.KOREA);
        long duration = 0;
        int hour = 0;
        int minutes = 0;

        if (longStartTime != 0 && longEndTime != 0) {
            // TODO: 2018-05-14 시간을 초로 받은 문자열 값을 다시 변환하여 계산하는 코드
            duration = (longEndTime - longStartTime) / 60000; // 분으로 변환
            hour = (int) (duration / 60); // 시간으로 변환
            minutes = (int) (duration % 60); // 남은 잔여 분 으로 변환
            //duration = Long.parseLong(endTime) - Long.parseLong(startDate) / 60000;
//                startDates = dataFormat.parse(startTime);
//                Date endDate = dataFormat.parse(endTime);
//                Log.e(TAG, "endDate.getTime(): " + endDate.getTime() );
//                Log.e(TAG, "startDates.getTime(): " + startDates.getTime() );
            //duration = (endDate.getTime() - startDates.getTime()) / 60000;
            //duration = (endDate.getTime() - startDates.getTime());
            if (duration == 0 || duration < 0) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "올바른 시간을 설정해주세요", Snackbar.LENGTH_SHORT).show();
            } else if (longStartTime == longEndTime) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "동일 시간은 적용 불가능합니다.", Snackbar.LENGTH_SHORT).show();
                //hour = duration / 3600;
                //minutes = duration % 60;
            } else {


                sleepTimeValue = String.valueOf(duration);
                Log.e(TAG, "duration: " + hour + "/" + minutes + "/" + duration);
                tempString = (sleepTimeValue != null) ? sleepTimeValue : "정보 없음";

                String tmp = "입력하신 정보를 확인해주세요." + "\n\n"
                        + "취침 시간 : " + startDate + " " + startTime + "\n\n"
                        + "기상 시간 : " + endDate + " " + endTime + "\n\n"
                        + "수면 구분 : " + inputType + "\n\n"
                        + "수면 시간 : " + hour + "시간" + minutes + "분 (" + sleepTimeValue + " 분)" + "\n\n"
                        + "수면 만족도 : " + satisfaction;

                Call<ResponseBody> request = service.writeSleep(userName, inputType,startDate,startTime,endDate,endTime,sleepTimeValue,satisfaction);


                new MaterialStyledDialog.Builder(this)
                        .setTitle("저장하시겠어요?")
                        .setIcon(R.drawable.ic_info_outline_black_24dp)
                        .setDescription(tmp)
                        .setPositiveText("저장")
                        .setHeaderDrawable(R.drawable.header)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Log.e("MaterialStyledDialogs", "Do something!");
                                progressDialog.show();
                                now = Calendar.getInstance();
                                initDate = simpleDateFormat.format(now.getTime());
                                initTime = simpleTimeFormat.format(now.getTime());
                                // TODO: 2018-05-14 base information
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_DATE, initDate);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_TIME, initTime);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_START_DATE, startDate);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_START_TIME, startTime);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_END_DATE, endDate);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_END_TIME, endTime);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_SLEEP_TIME, sleepTimeValue);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_TYPE, inputType);
                                contentValues.put(WriteEntry.SleepEntry.COLUNM_NAME_VALUE_SATISFACTION, satisfaction);

                                long insertDB = db.insert(WriteEntry.SleepEntry.TABLE_NAME, null, contentValues);

                                request.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        progressDialog.dismiss();
                                        Toast.makeText(WriteSleepActivity.this, "소중한 데이터를 잘 저장했어요. Code : " + insertDB, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(WriteSleepActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setCancelable(true).show();
            }
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "동일 시간은 적용 불가능합니다.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null){
            progressDialog = null;
        } else {
            super.onBackPressed();
        }
    }

}
