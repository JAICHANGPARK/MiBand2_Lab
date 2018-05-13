package nodomain.freeyourgadget.gadgetbridge.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

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
import nodomain.freeyourgadget.gadgetbridge.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_fitness);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }

        ButterKnife.bind(this);

        initDateTimeTextView();
        initStartDTPicker();
        initSpinner();
        initBubbleSeekBar();
    }

    private void initBubbleSeekBar() {
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

        inputType = "Unknown";

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

    }
}
