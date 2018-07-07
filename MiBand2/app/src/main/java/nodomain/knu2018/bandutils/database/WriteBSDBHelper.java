package nodomain.knu2018.bandutils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import nodomain.knu2018.bandutils.model.analysis.MyWriteCount;
import nodomain.knu2018.bandutils.model.pattern.PatternGlobal;

public class WriteBSDBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_BS_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WriteEntry.BloodSugarEntry.TABLE_NAME + " (" +
                    WriteEntry.BloodSugarEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WriteEntry.BloodSugarEntry.COLUNM_NAME_DATE + " TEXT," +
                    WriteEntry.BloodSugarEntry.COLUNM_NAME_TIME + " TEXT, " +
                    WriteEntry.BloodSugarEntry.COLUNM_NAME_TYPE + " TEXT, " +
                    WriteEntry.BloodSugarEntry.COLUNM_NAME_VALUE + " TEXT )";

    private static final String SQL_CREATE_FITNESS_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WriteEntry.FitnessEntry.TABLE_NAME + " (" +
                    WriteEntry.FitnessEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WriteEntry.FitnessEntry.COLUNM_NAME_DATE + " TEXT," +
                    WriteEntry.FitnessEntry.COLUNM_NAME_TIME + " TEXT, " +
                    WriteEntry.FitnessEntry.COLUNM_NAME_TYPE + " TEXT, " +
                    WriteEntry.FitnessEntry.COLUNM_NAME_VALUE + " TEXT," +
                    WriteEntry.FitnessEntry.COLUNM_NAME_LOAD + " TEXT" + " )";

    private static final String SQL_CREATE_DRUG_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WriteEntry.DrugEntry.TABLE_NAME + " (" +
                    WriteEntry.DrugEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WriteEntry.DrugEntry.COLUNM_NAME_DATE + " TEXT," +
                    WriteEntry.DrugEntry.COLUNM_NAME_TIME + " TEXT, " +
                    WriteEntry.DrugEntry.COLUNM_NAME_TYPE_TOP + " TEXT, " +
                    WriteEntry.DrugEntry.COLUNM_NAME_TYPE_BOTTOM + " TEXT, " +
                    WriteEntry.DrugEntry.COLUNM_NAME_VALUE + " TEXT " + " )";

    private static final String SQL_CREATE_MEAL_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WriteEntry.MealEntry.TABLE_NAME + " (" +
                    WriteEntry.MealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WriteEntry.MealEntry.COLUNM_NAME_DATE + " TEXT," +
                    WriteEntry.MealEntry.COLUNM_NAME_TIME + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_START_DATE + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_START_TIME + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_END_DATE + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_END_TIME + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_FOOD_TIME + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_TYPE + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_GOKRYU + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_BEEF + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_VEGETABLE + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_FAT + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_MILK + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_FRUIT + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_EXCHANGE + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_KCAL + " TEXT, " +
                    WriteEntry.MealEntry.COLUNM_NAME_VALUE_SATISFACTION + " TEXT " + " )";

    private static final String SQL_CREATE_SLEEP_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WriteEntry.SleepEntry.TABLE_NAME + " (" +
                    WriteEntry.SleepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WriteEntry.SleepEntry.COLUNM_NAME_DATE + " TEXT," +
                    WriteEntry.SleepEntry.COLUNM_NAME_TIME + " TEXT, " +
                    WriteEntry.SleepEntry.COLUNM_NAME_START_DATE + " TEXT, " +
                    WriteEntry.SleepEntry.COLUNM_NAME_START_TIME + " TEXT, " +
                    WriteEntry.SleepEntry.COLUNM_NAME_END_DATE + " TEXT, " +
                    WriteEntry.SleepEntry.COLUNM_NAME_END_TIME + " TEXT, " +
                    WriteEntry.SleepEntry.COLUNM_NAME_SLEEP_TIME + " TEXT, " +
                    WriteEntry.SleepEntry.COLUNM_NAME_TYPE + " TEXT, " +
                    WriteEntry.SleepEntry.COLUNM_NAME_VALUE_SATISFACTION + " TEXT " + " )";


    private static final String SQL_DELETE_BS_ENTRIES = "DROP TABLE IF EXISTS " + WriteEntry.BloodSugarEntry.TABLE_NAME;
    private static final String SQL_DELETE_FITNESS_ENTRIES = "DROP TABLE IF EXISTS " + WriteEntry.FitnessEntry.TABLE_NAME;
    private static final String SQL_DELETE_DRUG_ENTRIES = "DROP TABLE IF EXISTS " + WriteEntry.DrugEntry.TABLE_NAME;
    private static final String SQL_DELETE_MEAL_ENTRIES = "DROP TABLE IF EXISTS " + WriteEntry.MealEntry.TABLE_NAME;
    private static final String SQL_DELETE_SLEEP_ENTRIES = "DROP TABLE IF EXISTS " + WriteEntry.SleepEntry.TABLE_NAME;

    // TODO: 2018-05-28 SQLITE3는 TRUNCATE 명령어가 없다. 따라서 Delete 명령어를 사용했다. - 박제창
    private static final String SQL_TRUNCATE_BS_ENTRIES = "DELETE FROM " + WriteEntry.BloodSugarEntry.TABLE_NAME;
    private static final String SQL_TRUNCATE_FITNESS_ENTRIES = "DELETE FROM " + WriteEntry.FitnessEntry.TABLE_NAME;
    private static final String SQL_TRUNCATE_DRUG_ENTRIES = "DELETE FROM " + WriteEntry.DrugEntry.TABLE_NAME;
    private static final String SQL_TRUNCATE_MEAL_ENTRIES = "DELETE FROM " + WriteEntry.MealEntry.TABLE_NAME;
    private static final String SQL_TRUNCATE_SLEEP_ENTRIES = "DELETE FROM " + WriteEntry.SleepEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "lifedata.db";

    public WriteBSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BS_ENTRIES);
        db.execSQL(SQL_CREATE_FITNESS_ENTRIES);
        db.execSQL(SQL_CREATE_DRUG_ENTRIES);
        db.execSQL(SQL_CREATE_MEAL_ENTRIES);
        db.execSQL(SQL_CREATE_SLEEP_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BS_ENTRIES);
        db.execSQL(SQL_DELETE_FITNESS_ENTRIES);
        db.execSQL(SQL_DELETE_DRUG_ENTRIES);
        db.execSQL(SQL_DELETE_MEAL_ENTRIES);
        db.execSQL(SQL_DELETE_SLEEP_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void onTruncate() {
        SQLiteDatabase db = getWritableDatabase();
//        StringBuffer sb = new StringBuffer();
//        sb.append(" TRUNCATE foodName, foodGroup FROM mealdb");
//        sb.append(" LIMIT 20");

        db.execSQL(SQL_TRUNCATE_BS_ENTRIES);
        db.execSQL(SQL_TRUNCATE_FITNESS_ENTRIES);
        db.execSQL(SQL_TRUNCATE_DRUG_ENTRIES);
        db.execSQL(SQL_TRUNCATE_MEAL_ENTRIES);
        db.execSQL(SQL_TRUNCATE_SLEEP_ENTRIES);

//        String query = sb.toString();
//        db.execSQL(query, null);
//        db.rawQuery(sb.toString(), null);

    }

    public ArrayList<PatternGlobal> onChooseDateRead(String[] date) {
        ArrayList<PatternGlobal> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT date, time, type, value FROM bloodsugar");
        sb.append(" WHERE");
        sb.append(" DATE BETWEEN '" + date[0] + "'");
        sb.append(" AND'" + date[1] + "'");
        sb.append(" UNION");
        sb.append(" select date, time, (type1 || '-' || type2) as type, value from drug ");
        sb.append(" WHERE");
        sb.append(" DATE BETWEEN '" + date[0] + "'");
        sb.append(" AND'" + date[1] + "'");
        sb.append(" UNION");
        sb.append(" select date, time, type, value from fitness ");
        sb.append(" WHERE");
        sb.append(" DATE BETWEEN '" + date[0] + "'");
        sb.append(" AND'" + date[1] + "'");
        sb.append(" UNION");
        sb.append(" select date, time, type, exchange from meal ");
        sb.append(" WHERE");
        sb.append(" DATE BETWEEN '" + date[0] + "'");
        sb.append(" AND'" + date[1] + "'");
        sb.append(" UNION");
        sb.append(" select date, time, type, duration from sleep ");
        sb.append(" WHERE");
        sb.append(" DATE BETWEEN '" + date[0] + "'");
        sb.append(" AND'" + date[1] + "'");
        sb.append(" ORDER BY ");
        sb.append(" DATE ASC");
        Cursor cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {

            result.add(new PatternGlobal(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)));
        }

        for (PatternGlobal i : result) {
            //textView.append(i.getBsDate() + i.getBsTime() + i.getBsType() + i.getBsValue() + "\n");
            Log.e("onChooseDateRead", "result: " + i.getDate() + i.getTime() + i.getType() + i.getValue());
        }

        return result;
    }

    public ArrayList<String> dbCount() {

        int bsCount = 0;
        int fitnessCount = 0;
        int drugCount = 0;
        int mealCount = 0;
        int sleepCount = 0;
        int totalCount = 0;

        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        Cursor cursor = null;

        sb.append(" SELECT date FROM bloodsugar");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            bsCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM fitness");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            fitnessCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM drug");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            drugCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM meal");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            mealCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM sleep");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            sleepCount += 1;
            totalCount += 1;
        }

        result.add("1. 모든 기록 수 : " + String.valueOf(totalCount) + "개");
        result.add("2. 혈당 기록 수 : " + String.valueOf(bsCount) + "개");
        result.add("3. 운동 기록 수 : " + String.valueOf(fitnessCount) + "개");
        result.add("4. 투약 기록 수 : " + String.valueOf(drugCount) + "개");
        result.add("5. 식사 기록 수 : " + String.valueOf(mealCount) + "개");
        result.add("6. 수면 기록 수 : " + String.valueOf(sleepCount) + "개");
        return result;
    }


    public ArrayList<String> dbTotalCount() {

        int bsCount = 0;
        int fitnessCount = 0;
        int drugCount = 0;
        int mealCount = 0;
        int sleepCount = 0;
        int totalCount = 0;

        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        Cursor cursor = null;

        sb.append(" SELECT date FROM bloodsugar");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            bsCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM fitness");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            fitnessCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM drug");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            drugCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM meal");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            mealCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM sleep");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            sleepCount += 1;
            totalCount += 1;
        }

        result.add(String.valueOf(totalCount));
        return result;
    }


    public ArrayList<MyWriteCount> getCount() {

        int bsCount = 0;
        int fitnessCount = 0;
        int drugCount = 0;
        int mealCount = 0;
        int sleepCount = 0;
        int totalCount = 0;

        ArrayList<MyWriteCount> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        Cursor cursor = null;

        sb.append(" SELECT date FROM bloodsugar");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            bsCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM fitness");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            fitnessCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM drug");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            drugCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM meal");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            mealCount += 1;
            totalCount += 1;
        }

        sb.setLength(0);
        sb.append(" SELECT date FROM sleep");
        cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            sleepCount += 1;
            totalCount += 1;
        }

        result.add(new MyWriteCount("total", String.valueOf(totalCount)));
        result.add(new MyWriteCount("blood_sugar", String.valueOf(bsCount)));
        result.add(new MyWriteCount("fitness", String.valueOf(fitnessCount)));
        result.add(new MyWriteCount("drug", String.valueOf(drugCount)));
        result.add(new MyWriteCount("meal", String.valueOf(mealCount)));
        result.add(new MyWriteCount("sleep", String.valueOf(sleepCount)));

        return result;
    }


}
