package nodomain.knu2018.bandutils.activities;

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
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.database.WriteEntry;


public class WriteSleepActivity extends AppCompatActivity {

    private static final String TAG = "WriteSleepActivity";

    @BindView(R.id.closeButton)
    ImageView closeButton;
    @BindView(R.id.doneButton)
    ImageView doneButton;

    @BindView(R.id.startTimeText)
    TextView startTimeTextView;
    @BindView(R.id.endTimeText)
    TextView endTimeTextView;
    @BindView(R.id.startDateText)
    TextView startDateTextView;
    @BindView(R.id.endDateText)
    TextView endDateTextView;

    @BindView(R.id.startTimeButton)
    ImageView startTimeButton;
    @BindView(R.id.endTimeButton)
    ImageView endTimeButton;

    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;

    @BindView(R.id.seek_bar_1)
    BubbleSeekBar bubbleSeekBar;

    SwitchDateTimeDialogFragment startDateTimeDialogFragment;
    SwitchDateTimeDialogFragment endDateTimeDialogFragment;

    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;
    Calendar now;

    String initDate, initTime;
    String inputType, satisfaction;
    String sleepTimeValue;

    String startDate;
    String startTime;
    String endDate;
    String endTime;

    long longStartTime;
    long longEndTime;

    WriteBSDBHelper mDBHelper;
    SQLiteDatabase db;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sleep);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }
        ButterKnife.bind(this);

        initDataBases();

        initDateTimeTextView();
        initStartDTPicker();
        initEndDTPicker();
        initSpinner();
        initBubbleSeekBar();
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
                Log.e(TAG, "startDateTimeDialogFragment: " + date.toString());
                Log.e(TAG, "startDateTimeDialogFragment: " + date.getTime());
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
                Log.e(TAG, "endDateTimeDialogFragment: " + date.toString());
                Log.e(TAG, "endDateTimeDialogFragment: " + date.getTime());

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

    @OnClick(R.id.startTimeButton)
    public void onStartTimeButtonListener() {
        startDateTimeDialogFragment.show(getSupportFragmentManager(), "startDateTimePicker");
    }

    @OnClick(R.id.endTimeButton)
    public void onEndTimeButtonListener() {
        endDateTimeDialogFragment.show(getSupportFragmentManager(), "endDateTimePicker");
    }


    @OnClick(R.id.closeButton)
    public void onClickCloseButton() {
        finish();
    }

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
            } else if (longStartTime == longEndTime){
                Snackbar.make(getWindow().getDecorView().getRootView(), "동일 시간은 적용 불가능합니다.", Snackbar.LENGTH_SHORT).show();
                //hour = duration / 3600;
                //minutes = duration % 60;
            }else {
                sleepTimeValue = String.valueOf(duration);
                Log.e(TAG, "duration: " + hour + "/" + minutes + "/" + duration);
                tempString = (sleepTimeValue != null) ? sleepTimeValue : "정보 없음";

                String tmp = "입력하신 정보를 확인해주세요." + "\n\n"
                        + "취침 시간 : " + startDate + " " + startTime + "\n\n"
                        + "기상 시간 : " + endDate + " " + endTime + "\n\n"
                        + "수면 구분 : " + inputType + "\n\n"
                        + "수면 시간 : " + hour + "시간" + minutes + "분 (" + sleepTimeValue + " 분)" + "\n\n"
                        + "수면 만족도 : " + satisfaction;


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
                                Toast.makeText(WriteSleepActivity.this, "소중한 데이터를 잘 저장했어요. Code : " + insertDB, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setCancelable(true).show();
            }


        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "동일 시간은 적용 불가능합니다.", Snackbar.LENGTH_SHORT).show();
        }


    }

}
