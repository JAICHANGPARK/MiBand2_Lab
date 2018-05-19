package nodomain.knu2018.gadgetbridge.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.rengwuxian.materialedittext.MaterialEditText;
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
import nodomain.knu2018.gadgetbridge.R;
import nodomain.knu2018.gadgetbridge.database.WriteBSDBHelper;
import nodomain.knu2018.gadgetbridge.database.WriteEntry;


public class WriteFitnessActivity extends AppCompatActivity {
    private static final String TAG = "WriteFitnessActivity";


    @BindView(R.id.closeButton)
    ImageView closeButton;
    @BindView(R.id.doneButton)
    ImageView doneButton;

    @BindView(R.id.startTimeText)
    TextView startTimeTextView;
    @BindView(R.id.startDateText)
    TextView startDateTextView;
    @BindView(R.id.startTimeButton)
    ImageView startTimeButton;
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;

    @BindView(R.id.time_edit_text)
    MaterialEditText timeEditText;

    @BindView(R.id.seek_bar)
    BubbleSeekBar bubbleSeekBar;

    SwitchDateTimeDialogFragment startDateTimeDialogFragment;

    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;
    Calendar now;

    String strDate;
    String dateValue;
    String timeValue;
    String inputType;
    String fitnessTime;
    String satisfaction;

    String initDate, initTime;
    String inputValue;

    String startDate;
    String startTime;

    WriteBSDBHelper mDBHelper;
    SQLiteDatabase db;
    ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_fitness);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }

        ButterKnife.bind(this);

        initDataBases();

        initDateTimeTextView();
        initStartDTPicker();
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
        bubbleSeekBar.setCustomSectionTextArray((sectionCount, array) -> {
            array.clear();
            array.put(2, "bad");
            array.put(5, "ok");
            array.put(7, "good");
            array.put(9, "great");

            return array;
        });

        bubbleSeekBar.setProgress(5);

        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
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

    private void initSpinner() {

        inputType = "실내걷기";

        List<String> dataset = new LinkedList<>(Arrays.asList("실내걷기", "실외걷기", "실외달리기", "실내자전거"));
        niceSpinner.attachDataSource(dataset);

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        inputType = "실내걷기";
                        break;
                    case 1:
                        inputType = "실외걷기";
                        break;
                    case 2:
                        inputType = "실외달리기";
                        break;
                    case 3:
                        inputType = "실내자전거";
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
        startDate = initDate;
        startTime = initTime;

        startDateTextView.setText(initDate);
        startTimeTextView.setText(initTime);
    }

    private void initStartDTPicker() {

        startDateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "운동 시간 선택",
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
                Log.e(TAG, "onPositiveButtonClick: " + date.toString());
                startDate = simpleDateFormat.format(date.getTime());
                startTime = simpleTimeFormat.format(date.getTime());
                startDateTextView.setText(startDate);
                startTimeTextView.setText(startTime);
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

    @OnClick(R.id.closeButton)
    public void onClickCloseButton() {
        finish();
    }

    @OnClick(R.id.doneButton)
    public void onClickDoneButton() {

        // TODO: 2018-05-13 Save Button 데이터베이스 저장 기능
        if (timeEditText.getText().toString().length() == 0) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "운동시간을 입력하세요", Snackbar.LENGTH_SHORT).show();
        } else {

            String tmp = "입력하신 정보를 확인해주세요." + "\n\n"
                    + "입력 시간 : " + startDate + " " + startTime + "\n\n"
                    + "운동 정보 : " + "\n" + inputType + "\n\n"
                    + "운동 시간 : " + timeEditText.getText().toString() + " 분" + "\n\n"
                    + "운동 부하 : " + satisfaction;

            String tmpValue = timeEditText.getText().toString();


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
                            contentValues.put(WriteEntry.FitnessEntry.COLUNM_NAME_DATE, startDate);
                            contentValues.put(WriteEntry.FitnessEntry.COLUNM_NAME_TIME, startTime);
                            contentValues.put(WriteEntry.FitnessEntry.COLUNM_NAME_TYPE, inputType);
                            contentValues.put(WriteEntry.FitnessEntry.COLUNM_NAME_VALUE, tmpValue);
                            contentValues.put(WriteEntry.FitnessEntry.COLUNM_NAME_LOAD, satisfaction);
                            long insertDB = db.insert(WriteEntry.FitnessEntry.TABLE_NAME, null, contentValues);
                            Toast.makeText(WriteFitnessActivity.this, "소중한 데이터를 잘 저장했어요. Code : " + insertDB, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setCancelable(true).show();

            Log.e(TAG, "onClickDoneButton: " + inputType + "/ "
                    + "/" + timeEditText.getText().toString());
        }


    }
}
