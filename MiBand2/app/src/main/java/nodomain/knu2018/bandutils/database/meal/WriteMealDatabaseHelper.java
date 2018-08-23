package nodomain.knu2018.bandutils.database.meal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class WriteMealDatabaseHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_MEAL_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WriteMealEntry.MealEntry.TABLE_NAME + " (" +
                    WriteMealEntry.MealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WriteMealEntry.MealEntry.COLUNM_NAME_DATE + " TEXT," +
                    WriteMealEntry.MealEntry.COLUNM_NAME_TIME + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_START_DATE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_START_TIME + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_END_DATE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_END_TIME + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_FOOD_TIME + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_TYPE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_GOKRYU + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_BEEF + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_VEGETABLE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_FAT + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_MILK + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_FRUIT + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_EXCHANGE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_KCAL + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_SATISFACTION + " TEXT " + " )";

    private static final String SQL_CREATE_MEAL_TEST_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WriteMealEntry.MealEntryTop.TABLE_NAME + " (" +

                    WriteMealEntry.MealEntryTop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_FOOD_COUNT + " TEXT," +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_AMOUNT + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_KCAL + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_EXCHANGE + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_SAVE_DATETIME + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_SAVE_TIMESTAMP + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_START_DATETIME + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_START_TIMESTAMP + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_END_DATETIME + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_END_TIMESTAMP + " TEXT, " +
                    WriteMealEntry.MealEntryTop.COLUNM_INTAKE_TIME + " TEXT, " +

                    WriteMealEntry.MealEntry.COLUNM_NAME_TYPE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_GOKRYU + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_BEEF + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_VEGETABLE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_FAT + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_MILK + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_FRUIT + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_EXCHANGE + " TEXT, " +
                    WriteMealEntry.MealEntry.COLUNM_NAME_VALUE_KCAL + " TEXT, " +

                    WriteMealEntry.MealEntryTop.COLUNM_TOTAL_FOOD_LIST + " TEXT, " +

                    WriteMealEntry.MealEntryTop.COLUNM_NAME_VALUE_SATISFACTION + " TEXT " + " )";


    private static final String SQL_DELETE_MEAL_ENTRIES = "DROP TABLE IF EXISTS " + WriteMealEntry.MealEntry.TABLE_NAME;
    private static final String SQL_DELETE_MEAL_TEST_ENTRIES = "DROP TABLE IF EXISTS " + WriteMealEntry.MealEntryTop.TABLE_NAME;

    // TODO: 2018-05-28 SQLITE3는 TRUNCATE 명령어가 없다. 따라서 Delete 명령어를 사용했다. - 박제창
    private static final String SQL_TRUNCATE_MEAL_ENTRIES = "DELETE FROM " + WriteMealEntry.MealEntry.TABLE_NAME;
    private static final String SQL_TRUNCATE_MEAL_TEST_ENTRIES = "DELETE FROM " + WriteMealEntry.MealEntryTop.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "test_write.db";

    public WriteMealDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MEAL_TEST_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            db.execSQL(SQL_DELETE_MEAL_TEST_ENTRIES);
            onCreate(db);
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void onTruncate() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_TRUNCATE_MEAL_TEST_ENTRIES);

    }

    public void addEntry(ContentValues contentValues, int versionCode) {

        SQLiteDatabase db = getWritableDatabase();

        switch (versionCode) {
            case 1:
                break;
            case 2:
                db.insert(WriteMealEntry.MealEntryTop.TABLE_NAME, null, contentValues);
                break;
            default:
                break;
        }
        db.close();
    }

    public ArrayList<String> readJsonAll() {
        ArrayList<String> foodList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(WriteMealEntry.MealEntryTop.TABLE_NAME,
                new String[]{WriteMealEntry.MealEntryTop.COLUNM_TOTAL_FOOD_LIST},
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            foodList.add(cursor.getString(0));
        }

        if (foodList.size() != 0) {
            return foodList;
        } else {
            return null;
        }

    }


}
