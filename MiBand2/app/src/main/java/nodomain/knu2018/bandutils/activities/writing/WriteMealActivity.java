package nodomain.knu2018.bandutils.activities.writing;

import android.content.ContentValues;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.xw.repo.BubbleSeekBar;

import org.angmarch.views.NiceSpinner;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.ControlCenterv2;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.database.WriteEntry;
import nodomain.knu2018.bandutils.database.meal.WriteMealDatabaseHelper;
import nodomain.knu2018.bandutils.database.meal.WriteMealEntry;
import nodomain.knu2018.bandutils.model.foodmodel.FoodCard;
import nodomain.knu2018.bandutils.remote.IUploadAPI;
import nodomain.knu2018.bandutils.remote.RetrofitClient;
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
    BubbleSeekBar seekBar01;    //곡류군
    @BindView(R.id.seek_bar_2)
    BubbleSeekBar seekBar02;   //어육류
    @BindView(R.id.seek_bar_3)
    BubbleSeekBar seekBar03;   //채소군
    @BindView(R.id.seek_bar_4)
    BubbleSeekBar seekBar04;   //지방군
    @BindView(R.id.seek_bar_5)
    BubbleSeekBar seekBar05;   //우유군
    @BindView(R.id.seek_bar_6)
    BubbleSeekBar seekBar06;   //과일군
    @BindView(R.id.seek_bar_7)
    BubbleSeekBar bubbleSeekBar; //포만감

    @BindView(R.id.demo_4_obs_scroll_view)
    ObservableScrollView observableScrollView;


    int intGokryu = 0;

    int intBeef = 0;

    int intVegetable = 0;

    int intFat = 0;

    int intMilk = 0;

    int intFruit = 0;

    int intTotalExchange = 0;

    int intTotalKcal = 0;

    // TODO: 2018-05-05 DB 순서대로 정렬
    String typeValue;
    String inputType;
    String gokryuValue = "0";
    String beefValue = "0";
    String vegetableValue = "0";
    String fatValue = "0";
    String milkValue = "0";
    String fruitValue = "0";
    String exchangeValue;

    String satisfaction;

    String kcalValue;

    String startTimeValue;

    String endTimeValue;

    String mealTimeValue;

    String dateValue;

    String timeValue;

    String strDate;


    String startDate;
    String startTime;
    String endDate;
    String endTime;

    SwitchDateTimeDialogFragment startDateTimeDialogFragment;
    SwitchDateTimeDialogFragment endDateTimeDialogFragment;

    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;

    Calendar now;

    String initDate;
    String initTime;


    WriteBSDBHelper mDBHelper;
    SQLiteDatabase db;

    ContentValues contentValues;

    Retrofit retrofit;
    IUploadAPI service;

    private IUploadAPI getAPIUpload() {
        return RetrofitClient.getClient(BASE_URL).create(IUploadAPI.class);
    }

    String userName;
    String userUUID;


    // TODO: 2018-08-23 1.1.7 버전으로 추가된 부분 - 박제창
    ArrayList<FoodCard> beforeList;
    SimpleDateFormat simpleDatetimeFormat;

    WriteMealDatabaseHelper writeMealDatabaseHelper;

    String foodCount;
    int sumKCal, sumAmount;
    float floatGroup1, floatGroup2, floatGroup3, floatGroup4, floatGroup5, floatGroup6;
    float sumTotalExchange;
    String saveDatetime, startDatetime, endDatetime;
    long saveTimestamp, startTimestamp, endTimestamp;
    String jsonPlace;

    float userChangeGroup1, userChangeGroup2, userChangeGroup3, userChangeGroup4, userChangeGroup5, userChangeGroup6;


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

        // TODO: 2018-08-23 선택한 음식을 가져옴 리스트로 모델 FoodCard를 가진다 .
        beforeList = getIntent().getParcelableArrayListExtra("MEAL_CHOOSE_RESULT");
        Log.e(TAG, "Get Parcelable AL: Size = " + beforeList.size());
        foodCount = String.valueOf(beforeList.size());

        Gson gson = new Gson();
        jsonPlace = gson.toJson(beforeList);
        Log.e(TAG, "onCreate: " + jsonPlace);

        Type type = new TypeToken<ArrayList<FoodCard>>() {
        }.getType();
        ArrayList<FoodCard> temp = gson.fromJson(jsonPlace, type);
        Log.e(TAG, "onCreate: gson 변환 사이즈 " + temp.size());
        for (FoodCard fc : temp) {
            Log.e(TAG, "onCreate: 복호화 데이터 --> " + fc.getFoodName());
        }

        for (int i = 0; i < beforeList.size(); i++) {
            Log.e(TAG, "Parcelable: " + beforeList.get(i).getFoodName());

            floatGroup1 += Float.valueOf(beforeList.get(i).getFoodGroup1());
            floatGroup2 += Float.valueOf(beforeList.get(i).getFoodGroup2());
            floatGroup3 += Float.valueOf(beforeList.get(i).getFoodGroup3());
            floatGroup4 += Float.valueOf(beforeList.get(i).getFoodGroup4());
            floatGroup5 += Float.valueOf(beforeList.get(i).getFoodGroup5());
            floatGroup6 += Float.valueOf(beforeList.get(i).getFoodGroup6());

            sumAmount += Integer.valueOf(beforeList.get(i).getFoodAmount());
            sumKCal += Integer.valueOf(beforeList.get(i).getKcal());

        }

        sumTotalExchange = (floatGroup1 + floatGroup2 + floatGroup3 + floatGroup4 + floatGroup5 + floatGroup6);

        Log.e(TAG, "floatGroup1 -->  " + floatGroup1);
        Log.e(TAG, "floatGroup2 -->  " + floatGroup2);
        Log.e(TAG, "floatGroup3 -->  " + floatGroup3);
        Log.e(TAG, "floatGroup4 -->  " + floatGroup4);
        Log.e(TAG, "floatGroup5 -->  " + floatGroup5);
        Log.e(TAG, "floatGroup6 -->  " + floatGroup6);
        Log.e(TAG, "sum -->  " + (floatGroup1 + floatGroup2 + floatGroup3 + floatGroup4 + floatGroup5 + floatGroup6));


        // TODO: 2018-05-14 데이터베이스 저장을 위한 객체 초기화 메소드
        initDataBases();
        initBubbleSeekBar();
        initExchangeSeekBar();
        initSpinner();
        initDateTimeTextView();
        initStartDTPicker();
        initEndDTPicker();

        initRetrofit();
        initTosty();

    }

    private void initTosty() {
        Toasty.Config.getInstance().apply();
    }

    /**
     * Retrofit 객체 생성
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        service = retrofit.create(IUploadAPI.class);
        userName = Paper.book().read("userName");
        userUUID = Paper.book().read("userUUIDV2");

    }

    private void initDataBases() {
        mDBHelper = new WriteBSDBHelper(this);
        db = mDBHelper.getWritableDatabase();
        contentValues = new ContentValues();

        writeMealDatabaseHelper = new WriteMealDatabaseHelper(this);
    }

    private void initExchangeSeekBar() {

        seekBar01.setProgress(floatGroup1); //곡류군
        seekBar02.setProgress(floatGroup2); // 어육류
        seekBar03.setProgress(floatGroup3); // 채소군
        seekBar04.setProgress(floatGroup6); // 지방군은 그룹 6 식!바는 4번
        seekBar05.setProgress(floatGroup5); // 우유군
        seekBar06.setProgress(floatGroup4); //과일군

        userChangeGroup1 = floatGroup1; // 곡류군
        userChangeGroup2 = floatGroup2; // 어육류
        userChangeGroup3 = floatGroup3; // 채소군
        userChangeGroup4 = floatGroup4; // 과일군
        userChangeGroup5 = floatGroup5; // 우유군
        userChangeGroup6 = floatGroup6; // 지방군

        seekBar01.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                gokryuValue = String.valueOf(progress);
                intGokryu = progress;
                userChangeGroup1 = progressFloat;
//                Log.e(TAG, "onProgressChanged: " + progress + ", " + progressFloat);
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
                userChangeGroup2 = progressFloat;
//                Log.e(TAG, "onProgressChanged 2 : " + progress + ", " + progressFloat);
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
                userChangeGroup3 = progressFloat;
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
                userChangeGroup6 = progressFloat;
            }
        });
        seekBar05.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                milkValue = String.valueOf(progress);
                intMilk = progress;
                userChangeGroup5 = progressFloat;
            }
        });
        seekBar06.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                fruitValue = String.valueOf(progress);
                intFruit = progress;
                userChangeGroup4 = progressFloat;
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
        Date nowDate = now.getTime();

        simpleDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        simpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        initDate = simpleDateFormat.format(nowDate);
        initTime = simpleTimeFormat.format(nowDate);
        startDate = initDate;
        startTime = initTime;
        endDate = initDate;
        endTime = initTime;

        startDateTextView.setText(initDate);
        startTimeTextView.setText(initTime);
        endDateTextView.setText(initDate);
        endTimeTextView.setText(initTime);

        // TODO: 2018-08-23 추가
        saveDatetime = simpleDatetimeFormat.format(nowDate);
        startDatetime = simpleDatetimeFormat.format(nowDate);
        endDatetime = simpleDatetimeFormat.format(nowDate);
        saveTimestamp = nowDate.getTime();
        startTimestamp = nowDate.getTime();
        endTimestamp = nowDate.getTime();

        Log.e(TAG, "initDateTimeTextView: " + saveTimestamp);
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

                // TODO: 2018-08-23 추가 - 박제창
                startDatetime = simpleDatetimeFormat.format(date);
                startTimestamp = date.getTime();
                Log.e(TAG, "onPositiveButtonClick: " + startDatetime);
                Log.e(TAG, "onPositiveButtonClick: " + startTimestamp);
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

                // TODO: 2018-08-23 추가 - 박제창
                endDatetime = simpleDatetimeFormat.format(date);
                endTimestamp = date.getTime();
                Log.e(TAG, "onPositiveButtonClick: " + endDatetime);
                Log.e(TAG, "onPositiveButtonClick: " + endTimestamp);

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

        if (startTimestamp == endTimestamp) {
            Toasty.error(this, "시작시간과 종료시간이 같을 수 없습니다.", Toast.LENGTH_SHORT, true).show();
        } else if (startTimestamp >= endTimestamp) {
            Toasty.error(this, "시작시간이 종료시간보다 빠를 수 없습니다.", Toast.LENGTH_SHORT, true).show();
        } else {

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

            float sumUserChangeExchangeValue = (userChangeGroup1 + userChangeGroup2 + userChangeGroup3 + userChangeGroup4
                    + userChangeGroup5 + userChangeGroup6);
            sumUserChangeExchangeValue = Math.round(sumUserChangeExchangeValue * 10) / 10.0f;
            float finalSumUserChangeExchangeValue = sumUserChangeExchangeValue;
            float floatTempSumKCal = (userChangeGroup1 * GOKRYU_UNIT_KCAL) + (userChangeGroup2 * BEEF_UNIT_KCAL) +
                    (userChangeGroup3 * VEGETABLE_UNIT_KCAL) + (userChangeGroup6 * FAT_UNIT_KCAL) +
                    (userChangeGroup5 * MILK_UNIT_KCAL) + (userChangeGroup4 * FRUIT_UNIT_KCAL);

            int integerSumKCal = Math.round(floatTempSumKCal);

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
                    + "교환단위 합계 : " + String.valueOf(finalSumUserChangeExchangeValue) + "\n\n"
                    + "섭취 칼로리  : " + String.valueOf(integerSumKCal) + "\n\n"
                    + "식사 포만감 : " + satisfaction;

            Call<ResponseBody> request = service.userMealRegiste(userName, "N", inputType, startDate, startTime, endDate, endTime,
                    mealTimeValue, gokryuValue, beefValue, vegetableValue, fatValue, milkValue, fruitValue, exchangeValue, kcalValue, satisfaction);

            new MaterialStyledDialog.Builder(this)
                    .setTitle("저장하시겠어요?")
                    .setIcon(R.drawable.ic_info_outline_black_24dp)
                    .setDescription(tmp)
                    .setPositiveText("저장")
                    .setHeaderDrawable(R.drawable.header)
                    .onPositive((dialog, which) -> {
                        //Log.e("MaterialStyledDialogs", "Do something!");

                        ContentValues myContentValue = new ContentValues();

                        now = Calendar.getInstance();
                        initDate = simpleDateFormat.format(now.getTime());
                        initTime = simpleTimeFormat.format(now.getTime());

                        saveDatetime = simpleDatetimeFormat.format(now.getTime());
                        saveTimestamp = now.getTime().getTime();

                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_FOOD_COUNT, foodCount);
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_AMOUNT, String.valueOf(sumAmount));
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_KCAL, String.valueOf(sumKCal));
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_EXCHANGE, String.valueOf(sumTotalExchange));
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_SAVE_DATETIME, saveDatetime);
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_SAVE_TIMESTAMP, String.valueOf(saveTimestamp));
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_START_DATETIME, startDatetime);
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_START_TIMESTAMP, String.valueOf(startTimestamp));
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_END_DATETIME, endDatetime);
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_END_TIMESTAMP, String.valueOf(endTimestamp));
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_INTAKE_TIME, mealTimeValue);
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_TYPE, inputType);
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_GOKRYU, String.valueOf(userChangeGroup1));
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_BEEF, String.valueOf(userChangeGroup2));
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_VEGETABLE, String.valueOf(userChangeGroup3));
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_FAT, String.valueOf(userChangeGroup6));
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_MILK, String.valueOf(userChangeGroup5));
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_FRUIT, String.valueOf(userChangeGroup4));
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_EXCHANGE, String.valueOf(finalSumUserChangeExchangeValue));
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_KCAL, String.valueOf(integerSumKCal));
                        myContentValue.put(WriteMealEntry.MealEntryTop.COLUNM_TOTAL_FOOD_LIST, jsonPlace);
                        myContentValue.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_SATISFACTION, satisfaction);

                        writeMealDatabaseHelper.addEntry(myContentValue, 2);

                        // TODO: 2018-05-14 base information
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_DATE, initDate);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_TIME, initTime);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_START_DATE, startDate);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_START_TIME, startTime);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_END_DATE, endDate);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_END_TIME, endTime);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_FOOD_TIME, mealTimeValue);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_TYPE, inputType);
                        // TODO: 2018-05-14 value information
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_GOKRYU, gokryuValue);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_BEEF, beefValue);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_VEGETABLE, vegetableValue);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_FAT, fatValue);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_MILK, milkValue);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_FRUIT, fruitValue);

                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_EXCHANGE, exchangeValue);
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_KCAL, kcalValue);
                        // TODO: 2018-05-14 satisfaction information.
                        contentValues.put(WriteEntry.MealEntry.COLUNM_NAME_VALUE_SATISFACTION, satisfaction);

                        long insertDB = db.insert(WriteEntry.MealEntry.TABLE_NAME, null, contentValues);
                        Toast.makeText(WriteMealActivity.this, "소중한 데이터를 잘 저장했어요. Code : " + insertDB, Toast.LENGTH_SHORT).show();

                        request.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Intent intent = new Intent(WriteMealActivity.this, ControlCenterv2.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(WriteMealActivity.this, t.getMessage().toUpperCase(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    })
                    .setCancelable(true).show();

        }


        //Log.e(TAG, "onClickDoneButton: " + inputType + "/ " + "/");


    }


}
