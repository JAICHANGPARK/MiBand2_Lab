package nodomain.knu2018.bandutils.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.TimeLineAdapter;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.model.pattern.BloodSugar;
import nodomain.knu2018.bandutils.model.pattern.Drug;
import nodomain.knu2018.bandutils.model.pattern.Fitness;
import nodomain.knu2018.bandutils.model.pattern.Meal;
import nodomain.knu2018.bandutils.model.pattern.PatternGlobal;
import nodomain.knu2018.bandutils.model.pattern.Sleep;
import nodomain.knu2018.bandutils.util.SublimePickerFragment;

public class ReadDBActivity extends AppCompatActivity {

    private static final String TAG = "ReadDBActivity";

    SQLiteDatabase db;
    WriteBSDBHelper mHelper;

//    @BindView(R.id.textView)
//    TextView textView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    TimeLineAdapter mTimeLineAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PatternGlobal> patternGlobals;


    String dateValue;
    String timeValue;
    String valueType;
    String valueType2;
    String value;

    String startDateValue;
    String startTimeValue;

    String endDateValue;
    String endTimeValue;

    String durationValue;

    String gokryuValue;
    String beefValue;
    String vegetableValue;
    String fatValue;
    String milkValue;
    String fruitValue;
    String exchangeValue;
    String kcalValue;
    String stfValue;

    ArrayList<BloodSugar> bloodSugars = new ArrayList<>();
    ArrayList<Drug> drug = new ArrayList<>();
    ArrayList<Fitness> fitnesses = new ArrayList<>();
    ArrayList<Meal> meal = new ArrayList<>();
    ArrayList<Sleep> sleep = new ArrayList<>();

    StringBuffer sb = new StringBuffer();
    Cursor cursor;

    SelectedDate mSelectedDate;
    int mHour, mMinute;
    String mRecurrenceOption, mRecurrenceRule;

    TextView tvYear, tvMonth, tvDay, tvHour,
            tvMinute, tvRecurrenceOption, tvRecurrenceRule,
            tvStartDate, tvEndDate;

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
            String dateTemp = mSelectedDate.toString().substring(0,10).replaceAll(". ", "-").trim();
            String splitTemp = mSelectedDate.toString().substring(12, 23).replaceAll(". ", "-").trim();

            Log.e(TAG, "onDateTimeRecurrenceSet: start " + dateTemp + "\n" );
            Log.e(TAG, "onDateTimeRecurrenceSet: end " + splitTemp + "\n" );

            mHour = hourOfDay;
            mMinute = minute;
            mRecurrenceOption = recurrenceOption != null ? recurrenceOption.name() : "n/a";
            mRecurrenceRule = recurrenceRule != null ? recurrenceRule : "n/a";

            Log.e(TAG, "onDateTimeRecurrenceSet: " + mSelectedDate + ", " + mHour + ", " + mMinute + ", " + mRecurrenceOption + ", " + mRecurrenceRule  );

//            updateInfoView();
//
//            svMainContainer.post(new Runnable() {
//                @Override
//                public void run() {
//                    svMainContainer.scrollTo(svMainContainer.getScrollX(),
//                           cbAllowDateRangeSelection.getBottom());
//                }
//            });
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_db);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        patternGlobals = new ArrayList<>();

        mHelper = new WriteBSDBHelper(this);
        db = mHelper.getReadableDatabase();
        //textView.append("\n");

        fetchGlobal();
        mTimeLineAdapter = new TimeLineAdapter(this, patternGlobals);
        recyclerView.setAdapter(mTimeLineAdapter);

        //dbFetch();

    }

    private void dbFetch() {
        fetchBloodSugar();
        fetchDrug();
        fetchFitness();
        fetchMeal();
        fetchSleep();
    }

    private ArrayList<PatternGlobal> fetchGlobal() {

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

            patternGlobals.add(new PatternGlobal(dateValue, timeValue, valueType, value));
        }

        for (PatternGlobal i : patternGlobals) {
            //textView.append(i.getBsDate() + i.getBsTime() + i.getBsType() + i.getBsValue() + "\n");
            Log.e(TAG, "onCreate: " + i.getDate() + i.getTime() + i.getType() + i.getValue());
        }

        return patternGlobals;
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
