package nodomain.knu2018.gadgetbridge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
