package nodomain.freeyourgadget.gadgetbridge.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

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
import nodomain.freeyourgadget.gadgetbridge.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sleep);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }
        ButterKnife.bind(this);

        initDateTimeTextView();
        initStartDTPicker();
        initEndDTPicker();
        initSpinner();
        initBubbleSeekBar();
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
                Log.e(TAG, "onPositiveButtonClick: " + date.toString());
                endDate = simpleDateFormat.format(date.getTime());
                endTime = simpleTimeFormat.format(date.getTime());
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
    public void onClickCloseButton(){
        finish();
    }
    @OnClick(R.id.doneButton)
    public void onClickDoneButton(){
        // TODO: 2018-05-13 Save Button 데이터베이스 저장 기능  
        
    }


    
}
