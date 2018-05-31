package nodomain.knu2018.bandutils.activities.writing;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.xw.repo.BubbleSeekBar;

import org.angmarch.views.NiceSpinner;

import java.text.ParseException;
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
import nodomain.knu2018.bandutils.Remote.IUploadAPI;
import nodomain.knu2018.bandutils.Remote.RetrofitClient;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.database.WriteEntry;
import nodomain.knu2018.bandutils.util.ObservableScrollView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;


/**
 * The type Write meal activity.
 */
public class WriteMealActivity extends AppCompatActivity {

    private static final String TAG = "WriteMealActivity";

    private static final int GOKRYU_UNIT_KCAL = 100;
    private static final int BEEF_UNIT_KCAL = 75;
    private static final int VEGETABLE_UNIT_KCAL = 20;
    private static final int FAT_UNIT_KCAL = 45;
    private static final int MILK_UNIT_KCAL = 125;
    private static final int FRUIT_UNIT_KCAL = 50;

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
     * The Seek bar 01.
     */
    @BindView(R.id.seek_bar_1)
    BubbleSeekBar seekBar01;    //곡류군
    /**
     * The Seek bar 02.
     */
    @BindView(R.id.seek_bar_2)
    BubbleSeekBar seekBar02;   //어육류
    /**
     * The Seek bar 03.
     */
    @BindView(R.id.seek_bar_3)
    BubbleSeekBar seekBar03;   //채소군
    /**
     * The Seek bar 04.
     */
    @BindView(R.id.seek_bar_4)
    BubbleSeekBar seekBar04;   //지방군
    /**
     * The Seek bar 05.
     */
    @BindView(R.id.seek_bar_5)
    BubbleSeekBar seekBar05;   //우유군
    /**
     * The Seek bar 06.
     */
    @BindView(R.id.seek_bar_6)
    BubbleSeekBar seekBar06;   //과일군
    /**
     * The Bubble seek bar.
     */
    @BindView(R.id.seek_bar_7)
    BubbleSeekBar bubbleSeekBar; //포만감

    /**
     * The Observable scroll view.
     */
    @BindView(R.id.demo_4_obs_scroll_view)
    ObservableScrollView observableScrollView;


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

    /**
     * The Start time value.
     */
    String startTimeValue;
    /**
     * The End time value.
     */
    String endTimeValue;
    /**
     * The Meal time value.
     */
    String mealTimeValue;
    /**
     * The Date value.
     */
    String dateValue;
    /**
     * The Time value.
     */
    String timeValue;
    /**
     * The Str date.
     */
    String strDate;

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
    IUploadAPI service;
    private IUploadAPI getAPIUpload() {
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }

    /**
     * The UserInfo name.
     */
    String userName;
    /**
     * The UserInfo uuid.
     */
    String userUUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_meal);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }

        ButterKnife.bind(this);
        Paper.init(this);

        inputType = "아침";

        // TODO: 2018-05-14 데이터베이스 저장을 위한 객체 초기화 메소드
        initDataBases();
        initBubbleSeekBar();
        initExchangeSeekBar();
        initSpinner();
        initDateTimeTextView();
        initStartDTPicker();
        initEndDTPicker();

        initRetrofit();

    }

    /**
     * Retrofit 객체 생성
     */
    private void initRetrofit(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        service = retrofit.create(IUploadAPI.class);
        userName = Paper.book().read("userName");
        userUUID  = Paper.book().read("userUUIDV2");

    }

    private void initDataBases() {
        mDBHelper = new WriteBSDBHelper(this);
        db = mDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    private void initExchangeSeekBar() {
        seekBar01.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                gokryuValue = String.valueOf(progress);
                intGokryu = progress;
                //Log.e(TAG, "onProgressChanged: " + progress + ", " + progressFloat);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        seekBar02.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                beefValue = String.valueOf(progress);
                intBeef = progress;
                //Log.e(TAG, "onProgressChanged 2 : " + progress + ", " + progressFloat);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        seekBar03.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                vegetableValue = String.valueOf(progress);
                intVegetable = progress;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        seekBar04.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                fatValue = String.valueOf(progress);
                intFat = progress;
            }
        });
        seekBar05.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                milkValue = String.valueOf(progress);
                intMilk = progress;
            }
        });
        seekBar06.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                fruitValue = String.valueOf(progress);
                intFruit = progress;
            }
        });
    }

    private void initBubbleSeekBar() {

        observableScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

                seekBar01.correctOffsetWhenContainerOnScrolling();
                seekBar02.correctOffsetWhenContainerOnScrolling();
                seekBar03.correctOffsetWhenContainerOnScrolling();
                seekBar04.correctOffsetWhenContainerOnScrolling();
                seekBar05.correctOffsetWhenContainerOnScrolling();
                seekBar06.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBar.correctOffsetWhenContainerOnScrolling();

            }
        });

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
        List<String> dataset = new LinkedList<>(Arrays.asList("아침", "점심", "저녁", "간식"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        inputType = "아침";
                        break;
                    case 1:
                        inputType = "점심";
                        break;
                    case 2:
                        inputType = "저녁";
                        break;
                    case 3:
                        inputType = "간식";

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
        endDate = initDate;
        endTime = initTime;

        startDateTextView.setText(initDate);
        startTimeTextView.setText(initTime);
        endDateTextView.setText(initDate);
        endTimeTextView.setText(initTime);
    }

    private void initStartDTPicker() {

        startDateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "식사 시간 선택",
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
                "종료 시간 선택",
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
                //Log.e(TAG, "onPositiveButtonClick: " + date.toString());
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

        try {
            if (startTime != null || endTime != null) {
                startDates = dataFormat.parse(startTime);
                Date endDate = dataFormat.parse(endTime);
                long duration = (endDate.getTime() - startDates.getTime()) / 60000;
                mealTimeValue = String.valueOf(duration);
                Log.e(TAG, "duration: " + duration);
                tempString = (mealTimeValue != null) ? mealTimeValue : "정보 없음";

            } else {
                Toast.makeText(this, "시간을 설정해주세요 ", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        intTotalExchange = intBeef + intFat + intFruit + intGokryu + intMilk + intVegetable;
        intTotalKcal = (intGokryu * GOKRYU_UNIT_KCAL) + (intBeef * BEEF_UNIT_KCAL) +
                (intVegetable * VEGETABLE_UNIT_KCAL) + (intFat * FAT_UNIT_KCAL) +
                (intMilk * MILK_UNIT_KCAL) + (intFruit * FRUIT_UNIT_KCAL);

        exchangeValue = String.valueOf(intTotalExchange);
        kcalValue = String.valueOf(intTotalKcal);


        String tmp = "입력하신 정보를 확인해주세요." + "\n\n"
                + "식사 시작 시간 : " + startDate + " " + startTime + "\n\n"
                + "식사 종료 시간 : " + endDate + " " + endTime + "\n\n"
                + "식사 구분 : " + inputType + "\n\n"
                + "식사 시간 : " + mealTimeValue + " 분" + "\n\n"
                + "교환단위 합계 : " + exchangeValue + "\n\n"
                + "섭취 칼로리  : " + kcalValue + "\n\n"
                + "식사 포만감 : " + satisfaction;

        Call<ResponseBody> request = service.userMealRegiste(userName,"N", inputType,startDate,startTime,endDate,endTime,
                mealTimeValue, gokryuValue,beefValue,vegetableValue,fatValue,milkValue,fruitValue,exchangeValue,kcalValue,satisfaction);

        new MaterialStyledDialog.Builder(this)
                .setTitle("저장하시겠어요?")
                .setIcon(R.drawable.ic_info_outline_black_24dp)
                .setDescription(tmp)
                .setPositiveText("저장")
                .setHeaderDrawable(R.drawable.header)
                .onPositive((dialog, which) -> {
                    //Log.e("MaterialStyledDialogs", "Do something!");
                    now = Calendar.getInstance();
                    initDate = simpleDateFormat.format(now.getTime());
                    initTime = simpleTimeFormat.format(now.getTime());
                    // TODO: 2018-05-14 base information
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_DATE, initDate );
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_TIME, initTime );
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_START_DATE, startDate );
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_START_TIME, startTime );
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_END_DATE, endDate );
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_END_TIME, endTime );
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_FOOD_TIME, mealTimeValue );
                    contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_TYPE, inputType );
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

                    long insertDB = db.insert(WriteEntry.MealEntry.TABLE_NAME, null, contentValues);
                    Toast.makeText(WriteMealActivity.this, "소중한 데이터를 잘 저장했어요. Code : " + insertDB, Toast.LENGTH_SHORT).show();

                    request.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(WriteMealActivity.this, t.getMessage().toUpperCase(), Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .setCancelable(true).show();

        //Log.e(TAG, "onClickDoneButton: " + inputType + "/ " + "/");


    }
}
