package nodomain.knu2018.bandutils.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.sunfusheng.marqueeview.MarqueeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.Remote.IAnalysisAPI;
import nodomain.knu2018.bandutils.activities.writing.WriteHomesActivity;
import nodomain.knu2018.bandutils.adapter.TimeLineAdapter;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.model.analysis.ResponseAnalysis;
import nodomain.knu2018.bandutils.model.pattern.BloodSugar;
import nodomain.knu2018.bandutils.model.pattern.Drug;
import nodomain.knu2018.bandutils.model.pattern.Fitness;
import nodomain.knu2018.bandutils.model.pattern.Meal;
import nodomain.knu2018.bandutils.model.pattern.PatternGlobal;
import nodomain.knu2018.bandutils.model.pattern.Sleep;
import nodomain.knu2018.bandutils.util.SublimePickerFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tyrantgit.explosionfield.ExplosionField;

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
public class ReadDBActivity extends AppCompatActivity {

    private static final String TAG = "ReadDBActivity";

    /**
     * The Db.
     */
    SQLiteDatabase db;
    /**
     * The M helper.
     */
    WriteBSDBHelper mHelper;

//    @BindView(R.id.textView)
//    TextView textView;

    /**
     * The Recycler view.
     */
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    /**
     * The Relative layout.
     */
    @BindView(R.id.animation_view)
    RelativeLayout relativeLayout;
    /**
     * The Lottie animation view.
     */
    @BindView(R.id.lottie_animation)
    LottieAnimationView lottieAnimationView;

    /**
     * The Marquee view.
     */
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;

    /**
     * The Layout bottom sheet.
     */
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    /**
     * The Total text view.
     */
    @BindView(R.id.total_text_view)
    TextView totalTextView;

    /**
     * The Today text view.
     */
    @BindView(R.id.today_text_view)
    TextView todayTextView;

    /**
     * The Write button.
     */
    @BindView(R.id.write_button)
    Button writeButton;

    /**
     * The Fab.
     */
    @BindView(R.id.fab)
    FloatingActionButton fab;

    /**
     * The M time line adapter.
     */
    TimeLineAdapter mTimeLineAdapter;
    /**
     * The Layout manager.
     */
    RecyclerView.LayoutManager layoutManager;
    /**
     * The Pattern globals.
     */
    ArrayList<PatternGlobal> patternGlobals;

    /**
     * The Date value.
     */
    String dateValue;
    /**
     * The Time value.
     */
    String timeValue;
    /**
     * The Value type.
     */
    String valueType;
    /**
     * The Value type 2.
     */
    String valueType2;
    /**
     * The Value.
     */
    String value;

    /**
     * The Start date value.
     */
    String startDateValue;
    /**
     * The Start time value.
     */
    String startTimeValue;

    /**
     * The End date value.
     */
    String endDateValue;
    /**
     * The End time value.
     */
    String endTimeValue;

    /**
     * The Duration value.
     */
    String durationValue;

    /**
     * The Gokryu value.
     */
    String gokryuValue;
    /**
     * The Beef value.
     */
    String beefValue;
    /**
     * The Vegetable value.
     */
    String vegetableValue;
    /**
     * The Fat value.
     */
    String fatValue;
    /**
     * The Milk value.
     */
    String milkValue;
    /**
     * The Fruit value.
     */
    String fruitValue;
    /**
     * The Exchange value.
     */
    String exchangeValue;
    /**
     * The Kcal value.
     */
    String kcalValue;
    /**
     * The Stf value.
     */
    String stfValue;

    /**
     * The Blood sugars.
     */
    ArrayList<BloodSugar> bloodSugars = new ArrayList<>();
    /**
     * The Drug.
     */
    ArrayList<Drug> drug = new ArrayList<>();
    /**
     * The Fitnesses.
     */
    ArrayList<Fitness> fitnesses = new ArrayList<>();
    /**
     * The Meal.
     */
    ArrayList<Meal> meal = new ArrayList<>();
    /**
     * The Sleep.
     */
    ArrayList<Sleep> sleep = new ArrayList<>();

    /**
     * The Sb.
     */
    StringBuffer sb = new StringBuffer();
    /**
     * The Cursor.
     */
    Cursor cursor;

    /**
     * The Pref.
     */
    SharedPreferences pref;
    /**
     * The Toolbar.
     */
    Toolbar toolbar;

    /**
     * The Bottom sheet behavior.
     */
    BottomSheetBehavior bottomSheetBehavior;

    /**
     * The Retrofit.
     */
    Retrofit retrofit;
    /**
     * The Service.
     */
    IAnalysisAPI service;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;

    /**
     * The Total count.
     */
    String totalCount;
    /**
     * The Today count.
     */
    String todayCount;

    /**
     * The Network info.
     */
    NetworkInfo networkInfo;

    /**
     * The M selected date.
     */
    SelectedDate mSelectedDate;
    /**
     * The Explosion field.
     */
    ExplosionField explosionField;

    /**
     * The M fragment callback.
     */
    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
            // rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            // TODO: 2018-05-27 RangePicker 에서 받아온 값을 처리하는 부분.
            mSelectedDate = selectedDate;
            String[] selectDate = selectDateConverter(mSelectedDate);
            Log.e(TAG, "startDate : " + selectDate[0]);
            Log.e(TAG, "endDate : " + selectDate[1]);
            // TODO: 2018-05-29 선택된 날짜의 데이터를 가져오고 어뎁터를 다시 설정합니다. - 박제창.
            patternGlobals = mHelper.onChooseDateRead(selectDate);
            mTimeLineAdapter = new TimeLineAdapter(getApplicationContext(), patternGlobals);
            recyclerView.setAdapter(mTimeLineAdapter);

//            mHour = hourOfDay;
//            mMinute = minute;
//            mRecurrenceOption = recurrenceOption != null ? recurrenceOption.name() : "n/a";
//            mRecurrenceRule = recurrenceRule != null ? recurrenceRule : "n/a";

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_db);

        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_search);

        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);


        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("read_db_guide", false)) {
            Log.e(TAG, "onCreate: Guide Done ");
        } else {
            onGuideTapTarget();
        }


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        patternGlobals = new ArrayList<>();

        mHelper = new WriteBSDBHelper(this);
        db = mHelper.getReadableDatabase();

        patternGlobals = fetchGlobal();

        // TODO: 2018-05-28 데이터가 있을때와 없을때를 구분지어 뷰 처리하는 곳.  - 박제창
        if (patternGlobals.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.cancelAnimation();
            mTimeLineAdapter = new TimeLineAdapter(this, patternGlobals);
            recyclerView.setAdapter(mTimeLineAdapter);
        }

        explosionField = ExplosionField.attach2Window(this);
        networkInfo = getNetworkInfo();
        //dbFetch();
        //setSwipeableCard();
//        initText();
        initMarqee();
        initBottomSheet();
        initRetrofit();

    }

    /**
     * On click write button.
     *
     * @param v the v
     */
    @OnClick(R.id.write_button)
    public void onClickWriteButton(View v) {
        explosionField.explode(v);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                startActivity(new Intent(ReadDBActivity.this, WriteHomesActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }


    /**
     * On click floating action button.
     *
     * @param v the v
     */
    @OnClick(R.id.fab)
    public void onClickFloatingActionButton(View v) {
        //explosionField.explode(v);
        startActivity(new Intent(ReadDBActivity.this, WriteHomesActivity.class));
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
     * Retrofit 객체 생성
     *
     * @author : 박제창(Dreamwalker)
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IAnalysisAPI.class);
        progressDialog = new ProgressDialog(ReadDBActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("동기화 중..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String today = simpleDateFormat.format(now.getTime());
        Log.e(TAG, "initRetrofit: " + today);
        Call<ResponseAnalysis> request = service.getCount(today);

        if (networkInfo != null && networkInfo.isConnected()) {
            progressDialog.show();
            request.enqueue(new Callback<ResponseAnalysis>() {
                @Override
                public void onResponse(Call<ResponseAnalysis> call, Response<ResponseAnalysis> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        ResponseAnalysis tmp = response.body();
                        Log.e(TAG, "onResponse: size" + tmp.getResponse().size());
                        for (int i = 0; i < tmp.getResponse().size(); i++) {
                            Log.e(TAG, "onResponse: getCount- " + tmp.getResponse().get(i).getCount());
                        }
                        // TODO: 2018-05-30 데이터베이스의 모든 값이 삭제되면 인덱스 아웃 에러가 뜹니다.- 박제창. 
                        // TODO: 2018-05-30 좀 더 효과적인 해결 방법이 필요했으면 좋겠습니다. - 박제창. 
                        if (tmp.getResponse().size() > 1) {
                            totalCount = response.body().getResponse().get(0).getCount();
                            todayCount = response.body().getResponse().get(1).getCount();
                            String textTotal = "전체 기록 수 : " + totalCount + "개";
                            String textToday = "오늘 총 기록 수 :" + todayCount + "개";
                            totalTextView.setText(textTotal);
                            todayTextView.setText(textToday);
                        } else {
                            String textTotal = "전체 기록 수 : " + 0 + " 개";
                            String textToday = "오늘 총 기록 수 :" + 0 + " 개";
                            totalTextView.setText(textTotal);
                            todayTextView.setText(textToday);
                        }


                    } else {
                        Toast.makeText(ReadDBActivity.this, "실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAnalysis> call, Throwable t) {
                    Toast.makeText(ReadDBActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "네트워크를 연결해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void initBottomSheet() {
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    /**
     * manually opening / closing bottom sheet on button click
     */
    @OnClick(R.id.bottom_sheet)
    public void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //btnBottomSheet.setText("Close sheet");
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //btnBottomSheet.setText("Expand sheet");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        marqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
    }

    private void initMarqee() {

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> info = new ArrayList<>();
        info = mHelper.dbCount();
        // TODO: 2018-05-29 특정 인덱스에 값을 추가하면 추가되고 기존 인덱스 데이터는 하나씩 밀려난다.
        info.add(0, "당신의 총 기록수를 알려드릴게요");
//        info.add("1. 大家好，我是孙福生。");
//        info.add("2. 欢迎大家关注我哦！");
//        info.add("3. GitHub帐号：sfsheng0322");
//        info.add("4. 新浪微博：孙福生微博");
//        info.add("5. 个人博客：sunfusheng.com");
//        info.add("6. 微信公众号：孙福生");
        marqueeView.startWithList(info);

// 在代码里设置自己的动画
        marqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
    }

//    String[] sentences = {"What is design?",
//            "Design is not just",
//            "what it looks like and feels like.",
//            "Design is how it works. \n- Steve Jobs",
//            "Older people",
//            "sit down and ask,",
//            "'What is it?'",
//            "but the boy asks,",
//            "'What can I do with it?'. \n- Steve Jobs",
//            "Swift",
//            "Objective-C",
//            "iPhone",
//            "iPad",
//            "Mac Mini", "MacBook Pro", "Mac Pro", "爱老婆", "老婆和女儿"};
//    int index;
//
//
//    class ClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            if (v instanceof HTextView) {
//                if (index + 1 >= sentences.length) {
//                    index = 0;
//                }
//                ((HTextView) v).animateText(sentences[index++]);
//            }
//        }
//    }
//
//    class SimpleAnimationListener implements AnimationListener {
//
//        private Context context;
//
//        public SimpleAnimationListener(Context context) {
//            this.context = context;
//        }
//        @Override
//        public void onAnimationEnd(HTextView hTextView) {
//            Toast.makeText(context, "Animation finished", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void initText(){
//        scaleTextView.setOnClickListener(new ClickListener());
//        scaleTextView.setAnimationListener(new SimpleAnimationListener(this));
//    }

//    private void setRotatingTextWrapper(){
//        rotatingTextWrapper.setSize(35);
//
//        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word", "Word01", "Word02");
//        rotatable.setSize(35);
//        rotatable.setAnimationDuration(500);
//
//        rotatingTextWrapper.setContent("This is ?", rotatable);

//    RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
//        rotatingTextWrapper.setSize(35);
//
//    Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word", "Word01", "Word02");
//        rotatable.setSize(35);
//        rotatable.setAnimationDuration(500);
//
//        rotatingTextWrapper.setContent("This is ?", rotatable);

//    }

//    private void setSwipeableCard(){
//
//        Toolbar.OnMenuItemClickListener toolbarListener = new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.action_settings) {
//                    Toast.makeText(getApplicationContext(), "Settings Menu!", Toast.LENGTH_LONG).show();
//                    return true;
//                }
//
//                return false;
//            }
//        };
//
//        final View.OnClickListener clickFab = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Fab Button!", Toast.LENGTH_LONG).show();
//            }
//        };
//
//        final OptionView singleSwipe = new OptionView.Builder()
//                .normalCard()
//                .text("Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text," +
//                        " a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text, a lot of Text")
//                .title("Single Card")
//                .menuItem(R.menu.menu_main)
//                .toolbarListener(toolbarListener)
//                .setAdditionalItem(new OptionViewAdditional.Builder()
//                        .setFabIcon(android.R.drawable.ic_dialog_info)
//                        .setFabColor(R.color.colorPrimary)
//                        .setOnClickListenerFab(clickFab)
//                        .build())
//                .build();
//
//        swipeableCard.init(getApplicationContext(), singleSwipe);
//    }


    /**
     * 달력에서 선택된 날짜를 데이터베이스에 맞게 올바른 형식으로
     * 변형시키는 메소드
     *
     * @param selectedDate
     * @return 시작, 종료 날짜가 담긴 문자열 배열.
     * @author : 박제창(Dreamwalker)
     */
    private String[] selectDateConverter(SelectedDate selectedDate) {

        String[] tmpString = selectedDate.toString().split("\n");
        //tmpString[0].replace(".", "-");

        String startDate[] = tmpString[0]
                .substring(0, tmpString[0].length() - 1)
                .replace(". ", "-")
                .split("-");

        String endDate[] = tmpString[1]
                .substring(0, tmpString[1].length() - 1)
                .replace(". ", "-").split("-");

        startDate[1] = (startDate[1].length() == 1) ? "0" + startDate[1] : startDate[1];
        endDate[1] = (endDate[1].length() == 1) ? "0" + endDate[1] : endDate[1];

        startDate[2] = (startDate[2].length() == 1) ? "0" + startDate[2] : startDate[2];
        endDate[2] = (endDate[2].length() == 1) ? "0" + endDate[2] : endDate[2];

        String startDateTemp = startDate[0] + "-" + startDate[1] + "-" + startDate[2];
        String endDateTemp = endDate[0] + "-" + endDate[1] + "-" + endDate[2];

        return new String[]{startDateTemp, endDateTemp};
    }

    /**
     * 1회성 가이드를 위한 메소드
     *
     * @author : 박제창(Dreamwalker)
     */
    private void onGuideTapTarget() {
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forToolbarMenuItem(toolbar, R.id.search, "날짜 선택하기",
                        "날짜를 검색하세요. 달력을 길게 누르면 범위 지정이 가능합니다.")
                        // All options below are optional
                        .outerCircleColor(R.color.primary_custom_light)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(30)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.primary_custom_light)  // Specify the color of the description text
                        .textColor(R.color.accent_custom)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        .targetRadius(50),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        SharedPreferences.Editor ed = pref.edit();
                        ed.putBoolean("read_db_guide", true);
                        ed.apply();
                    }
                });
    }

    private void dbFetch() {
        fetchBloodSugar();
        fetchDrug();
        fetchFitness();
        fetchMeal();
        fetchSleep();
    }

    private ArrayList<PatternGlobal> fetchGlobal() {

        ArrayList<PatternGlobal> tmpArray = new ArrayList<>();

        sb.append(" SELECT date, time, type, value FROM bloodsugar");
        sb.append(" UNION");
        sb.append(" select date, time, (type1 || '-' || type2) as type, value from drug ");
        sb.append(" UNION");
        sb.append(" select date, time, type, value from fitness ");
        sb.append(" UNION");
        sb.append(" select date, time, type, exchange from meal ");
        sb.append(" UNION");
        sb.append(" select date, time, type, duration from sleep ");
        sb.append(" ORDER BY ");
        sb.append(" DATE ASC");
        // 읽기 전용 DB 객체를 만든다.

        cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {
            dateValue = cursor.getString(0);
            timeValue = cursor.getString(1);
            valueType = cursor.getString(2);
            value = cursor.getString(3);

            tmpArray.add(new PatternGlobal(dateValue, timeValue, valueType, value));
        }

        for (PatternGlobal i : tmpArray) {
            //textView.append(i.getBsDate() + i.getBsTime() + i.getBsType() + i.getBsValue() + "\n");
            Log.e(TAG, "onCreate: " + i.getDate() + i.getTime() + i.getType() + i.getValue());
        }

        return tmpArray;
    }

    private ArrayList<BloodSugar> fetchBloodSugar() {

        //textView.append("혈당 데이터" + "\n");

        // TODO: 2018-02-11 혈당 값 가져오기
        sb.append(" SELECT TYPE, VALUE, DATE, TIME FROM bloodsugar");
        sb.append(" ORDER BY ");
        sb.append(" DATE ASC");
        // 읽기 전용 DB 객체를 만든다.
        cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {
            valueType = cursor.getString(0);
            value = cursor.getString(1);
            dateValue = cursor.getString(2);
            timeValue = cursor.getString(3);
            bloodSugars.add(new BloodSugar(valueType, value, dateValue, timeValue));
        }

        for (BloodSugar i : bloodSugars) {
            //textView.append(i.getBsDate() + i.getBsTime() + i.getBsType() + i.getBsValue() + "\n");
            Log.e(TAG, "onCreate: " + i.getBsDate() + i.getBsTime() + i.getBsType() + i.getBsValue());
        }

        return bloodSugars;

    }

    private ArrayList<Drug> fetchDrug() {
        //textView.append("투약 데이터" + "\n");
        // TODO: 2018-05-14 StringBuilder 초기화.
        sb.setLength(0);
        // TODO: 2018-05-14 투약 값 가져오기
        sb.append(" SELECT TYPE1, TYPE2, VALUE, DATE, TIME FROM drug");
        sb.append(" ORDER BY ");
        sb.append(" DATE ASC");
        // 읽기 전용 DB 객체를 만든다.
        cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {
            valueType = cursor.getString(0);
            valueType2 = cursor.getString(1);
            value = cursor.getString(2);
            dateValue = cursor.getString(3);
            timeValue = cursor.getString(4);
            drug.add(new Drug(dateValue, timeValue, valueType, valueType2, value));
        }

        for (Drug i : drug) {
            //textView.append(i.getDate() + i.getTime() + i.getTypeTop() + i.getTypeBottom() + i.getValue() + "\n");
            Log.e(TAG, "onCreate: " + i.getDate() + i.getTime() + i.getTypeTop() + i.getTypeBottom() + i.getValue());
        }
        return drug;
    }

    private ArrayList<Fitness> fetchFitness() {
        //textView.append("운동 데이터" + "\n");
        // TODO: 2018-05-14 StringBuilder 초기화.
        sb.setLength(0);
        // TODO: 2018-05-14 투약 값 가져오기
        sb.append(" SELECT TYPE, VALUE, LOAD, DATE, TIME FROM fitness");
        sb.append(" ORDER BY ");
        sb.append(" DATE ASC");
        // 읽기 전용 DB 객체를 만든다.
        cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {
            valueType = cursor.getString(0);
            value = cursor.getString(1);
            valueType2 = cursor.getString(2);
            dateValue = cursor.getString(3);
            timeValue = cursor.getString(4);

            fitnesses.add(new Fitness(valueType, value, dateValue, timeValue, valueType2));
        }

        for (Fitness i : fitnesses) {
            //textView.append(i.getDate() + i.getTime() + ", " + i.getType() + ", " + i.getValue() + ", " + i.getLoad() + "\n");
            Log.e(TAG, "onCreate: " + i.getDate() + i.getTime() + i.getType() + i.getValue() + i.getLoad());
        }
        return fitnesses;
    }

    private ArrayList<Meal> fetchMeal() {
        //textView.append("식사 데이터" + "\n");
        // TODO: 2018-05-14 StringBuilder 초기화.
        sb.setLength(0);
        // TODO: 2018-05-14 meal 값 가져오기
        sb.append(" SELECT * FROM meal");
        sb.append(" ORDER BY ");
        sb.append(" DATE ASC");
        // 읽기 전용 DB 객체를 만든다.
        cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {

            dateValue = cursor.getString(1);
            timeValue = cursor.getString(2);
            startDateValue = cursor.getString(3);
            startTimeValue = cursor.getString(4);
            endDateValue = cursor.getString(5);
            endTimeValue = cursor.getString(6);
            durationValue = cursor.getString(7);

            valueType = cursor.getString(8);

            gokryuValue = cursor.getString(9);
            beefValue = cursor.getString(10);
            vegetableValue = cursor.getString(11);
            fatValue = cursor.getString(12);
            milkValue = cursor.getString(13);
            fruitValue = cursor.getString(14);

            exchangeValue = cursor.getString(15);
            kcalValue = cursor.getString(16);

            stfValue = cursor.getString(17);


            meal.add(new Meal(dateValue, timeValue, startDateValue, startTimeValue, endDateValue, endTimeValue, durationValue,
                    valueType, gokryuValue, beefValue, vegetableValue, fatValue, milkValue, fruitValue, exchangeValue,
                    kcalValue, stfValue));
        }

        for (Meal i : meal) {
            //textView.append(i.getDate() + i.getTime() + ", " + i.getDuration() + ", " + ", " + i.getExchange() + ", " + i.getKcal() + ", " + i.getSatisfaction() + "\n");

        }
        return meal;
    }

    private ArrayList<Sleep> fetchSleep() {
        //textView.append("수면 데이터" + "\n");
        // TODO: 2018-05-14 StringBuilder 초기화.
        sb.setLength(0);
        // TODO: 2018-05-14 meal 값 가져오기
        sb.append(" SELECT * FROM sleep");
        sb.append(" ORDER BY ");
        sb.append(" DATE ASC");
        // 읽기 전용 DB 객체를 만든다.
        cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {

            dateValue = cursor.getString(1);
            timeValue = cursor.getString(2);
            startDateValue = cursor.getString(3);
            startTimeValue = cursor.getString(4);
            endDateValue = cursor.getString(5);
            endTimeValue = cursor.getString(6);
            durationValue = cursor.getString(7);
            valueType = cursor.getString(8);
            stfValue = cursor.getString(9);


            sleep.add(new Sleep(dateValue, timeValue, startDateValue, startTimeValue,
                    endDateValue, endTimeValue, durationValue, valueType, stfValue));
        }

        for (Sleep i : sleep) {
            //textView.append(i.getDate() + i.getTime() + ", " + i.getDuration() + ", "+ ", " + i.getType() + ", " + i.getSatisfaction() + "\n");

        }
        return sleep;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:

                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                //SublimeOptions options = new SublimeOptions();
                Bundle bundle = new Bundle();
                //options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
                //options.setCanPickDateRange(true);
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                if (!optionsPair.first) { // If options are not valid
                    Toast.makeText(ReadDBActivity.this, "No pickers activated", Toast.LENGTH_SHORT).show();
                    return true;
                }

                // Valid options

                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");

//                bundle.putParcelable("SUBLIME_OPTIONS", options);
//                pickerFrag.setArguments(bundle);
//                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     * Gets options.
     *
     * @return the options
     */
    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);
        // Enable/disable the date range selection feature
        options.setCanPickDateRange(true);

        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);

        options.setDateParams(startCal, endCal);*/

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }
}
