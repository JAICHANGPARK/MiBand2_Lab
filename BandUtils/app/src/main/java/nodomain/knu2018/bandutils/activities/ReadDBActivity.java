package nodomain.knu2018.bandutils.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.model.BloodSugar;
import nodomain.knu2018.bandutils.model.Drug;
import nodomain.knu2018.bandutils.model.Fitness;
import nodomain.knu2018.bandutils.model.Meal;
import nodomain.knu2018.bandutils.model.Sleep;

public class ReadDBActivity extends AppCompatActivity {

    private static final String TAG = "ReadDBActivity";

    SQLiteDatabase db;
    WriteBSDBHelper mHelper;

    @BindView(R.id.textView)
    TextView textView;


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

    String  gokryuValue;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_db);
        ButterKnife.bind(this);

        mHelper = new WriteBSDBHelper(this);
        db = mHelper.getReadableDatabase();
        textView.append("\n");

        dbFetch();

    }

    private void dbFetch(){
        fetchBloodSugar();
        fetchDrug();
        fetchFitness();
        fetchMeal();
        fetchSleep();
    }

    private ArrayList<BloodSugar> fetchBloodSugar() {

        textView.append("혈당 데이터" + "\n");

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

        for (BloodSugar i : bloodSugars){
            textView.append(i.getBsDate() + i.getBsTime() + i.getBsType() + i.getBsValue() + "\n");
            Log.e(TAG, "onCreate: " + i.getBsDate() + i.getBsTime() + i.getBsType() + i.getBsValue());
        }

        return bloodSugars;

    }

    private ArrayList<Drug> fetchDrug() {
        textView.append("투약 데이터" + "\n");
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
            drug.add(new Drug(dateValue,timeValue,valueType,valueType2,value));
        }

        for (Drug i : drug){
            textView.append(i.getDate() + i.getTime() + i.getTypeTop() + i.getTypeBottom() + i.getValue() + "\n");
            Log.e(TAG, "onCreate: " + i.getDate() + i.getTime() + i.getTypeTop() + i.getTypeBottom() + i.getValue());
        }
        return drug;
    }

    private ArrayList<Fitness> fetchFitness() {
        textView.append("운동 데이터" + "\n");
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

            fitnesses.add(new Fitness(valueType,value,dateValue,timeValue,valueType2));
        }

        for (Fitness i : fitnesses){
            textView.append(i.getDate() + i.getTime() +", "+ i.getType() +", "+ i.getValue() +", "+ i.getLoad() + "\n");
            Log.e(TAG, "onCreate: " + i.getDate() + i.getTime() + i.getType() + i.getValue() + i.getLoad());
        }
        return fitnesses;
    }

    private ArrayList<Meal> fetchMeal(){
        textView.append("식사 데이터" + "\n");
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


            meal.add(new Meal(dateValue,timeValue,startDateValue,startTimeValue,endDateValue,endTimeValue,durationValue,
                    valueType, gokryuValue,beefValue,vegetableValue,fatValue,milkValue,fruitValue,exchangeValue,
                    kcalValue,stfValue));
        }

        for (Meal i : meal){
            textView.append(i.getDate() + i.getTime() +", " + i.getDuration() +", "
                    +", "+ i.getExchange() +", "+ i.getKcal() +", "+ i.getSatisfaction() + "\n");

        }
        return meal;
    }

    private ArrayList<Sleep> fetchSleep(){
        textView.append("수면 데이터" + "\n");
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


            sleep.add(new Sleep(dateValue,timeValue,startDateValue,startTimeValue,
                    endDateValue,endTimeValue,durationValue, valueType, stfValue));
        }

        for (Sleep i : sleep){
            textView.append(i.getDate() + i.getTime() +", " + i.getDuration() +", "
                    +", "+ i.getType() +", "+ i.getSatisfaction() + "\n");

        }
        return sleep;
    }
}
