package nodomain.knu2018.bandutils.database.fooddb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.List;

import nodomain.knu2018.bandutils.model.foodmodel.Food;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    Context context;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "meal_db.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Entry.SQL_CREATE_FOOD_DB_ENTRIES);
        //Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Entry.SQL_DELETE_BS_ENTRIES);
        onCreate(db);
    }

    public void addFood(Food food) {

        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO ");
        sb.append(Entry.FoodEntry.TABLE_NAME + "(");
        sb.append(Entry.FoodEntry.COLUNM_NAME_NUMBER + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_GROUP + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_FOOD_NAME + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_AMOUNT + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_KCAL + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_CARBO + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_PROTEIN + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_FAT + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_SUGAR + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_NATRIUM + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_CHOLEST + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_FATTY + ",");
        sb.append(Entry.FoodEntry.COLUNM_NAME_TRANS_FATTY + ")");
        sb.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        String query = sb.toString();
        db.execSQL(query, new Object[]{food.getFoodNumber(), food.getFoodGroup(), food.getFoodName(),
                food.getFoodAmount(), food.getFoodKcal(), food.getFoodCarbo(),
                food.getFoodProtein(), food.getFoodFat(), food.getFoodSugar(), food.getFoodNatrium(),
                food.getFoodCholest(), food.getFoodFatty(), food.getFoodTransFatty()});
        //Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<String> readNameDate() {
        ArrayList<String> nameArrayList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        SQLiteDatabase db = getWritableDatabase();
        String valueName;
        String valueGroup;


        Cursor cursor = null;
        // TODO: 2018-02-11 혈당 값 가져오기
        sb.append(" SELECT foodName FROM mealdb");
        //sb.append(" LIMIT 20");

        cursor = db.rawQuery(sb.toString(), null);


        while (cursor.moveToNext()) {
            valueName = cursor.getString(0);
            nameArrayList.add(valueName);
        }
        return nameArrayList;
    }


    public ArrayList<Food> readSomeDate() {
        ArrayList<Food> foodArrayList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        SQLiteDatabase db = getWritableDatabase();
        String valueName;
        String valueGroup;


        Cursor cursor = null;
        // TODO: 2018-02-11 혈당 값 가져오기
        sb.append(" SELECT foodName, foodGroup FROM mealdb");
        sb.append(" LIMIT 20");

        cursor = db.rawQuery(sb.toString(), null);


        while (cursor.moveToNext()) {
            valueName = cursor.getString(0);
            valueGroup = cursor.getString(1);
            foodArrayList.add(new Food(valueGroup, valueName));
        }
        return foodArrayList;
    }

    public ArrayList<Food> searchDBDate(String name) {
        ArrayList<Food> nameArrayList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        SQLiteDatabase db = getWritableDatabase();
        String valueName;
        String valueGroup;


        Cursor cursor = null;
        // TODO: 2018-02-11 혈당 값 가져오기
        sb.append(" SELECT foodName,foodGroup FROM mealdb ");
        sb.append(" WHERE foodName LIKE '%" + name + "%'");
        //sb.append(" LIMIT 20");

        cursor = db.rawQuery(sb.toString(), null);


        while (cursor.moveToNext()) {
            valueName = cursor.getString(0);
            valueGroup = cursor.getString(1);
            nameArrayList.add(new Food(valueGroup, valueName));
        }
        return nameArrayList;
    }

    public ArrayList<Food> getSomeFood() {

        ArrayList<Food> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {
                "foodGroup ",
                "foodName"
        };

        String tableName = "mealdb";

        qb.setTables(tableName);

        Cursor cursor = qb.query(db, sqlSelect, null,
                null, null, null, null, "20");


        if (cursor.moveToFirst()) {

            do {
                Food food = new Food();
                food.setFoodNumber(cursor.getString(cursor.getColumnIndex("foodNumber")));
                food.setFoodGroup(cursor.getString(cursor.getColumnIndex("foodGroup")));
                food.setFoodName(cursor.getString(cursor.getColumnIndex("foodName")));
                food.setFoodAmount(cursor.getString(cursor.getColumnIndex("foodAmount")));
                food.setFoodKcal(cursor.getString(cursor.getColumnIndex("foodKcal")));
                food.setFoodCarbo(cursor.getString(cursor.getColumnIndex("foodCarbo")));
                food.setFoodProtein(cursor.getString(cursor.getColumnIndex("foodProtein")));
                food.setFoodFat(cursor.getString(cursor.getColumnIndex("foodFat")));
                food.setFoodSugar(cursor.getString(cursor.getColumnIndex("foodSugar")));
                food.setFoodNatrium(cursor.getString(cursor.getColumnIndex("foodNatrium")));
                food.setFoodCholest(cursor.getString(cursor.getColumnIndex("foodCholest")));
                food.setFoodFatty(cursor.getString(cursor.getColumnIndex("foodFatty")));
                food.setFoodTransFatty(cursor.getString(cursor.getColumnIndex("foodTransFatty")));

                result.add(food);
            } while (cursor.moveToFirst());
        }
        return result;
    }

    public List<Food> getFood() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {
                "foodNumber",
                "foodGroup ",
                "foodName",
                "foodAmount",
                "foodKcal",
                "foodCarbo",
                "foodProtein",
                "foodFat",
                "foodSugar",
                "foodNatrium",
                "foodCholest",
                "foodFatty",
                "foodTransFatty"
        };

        String tableName = "mealdb";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, null,
                null, null, null, null, null);
        List<Food> result = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                Food food = new Food();
                food.setFoodNumber(cursor.getString(cursor.getColumnIndex("foodNumber")));
                food.setFoodGroup(cursor.getString(cursor.getColumnIndex("foodGroup")));
                food.setFoodName(cursor.getString(cursor.getColumnIndex("foodName")));
                food.setFoodAmount(cursor.getString(cursor.getColumnIndex("foodAmount")));
                food.setFoodKcal(cursor.getString(cursor.getColumnIndex("foodKcal")));
                food.setFoodCarbo(cursor.getString(cursor.getColumnIndex("foodCarbo")));
                food.setFoodProtein(cursor.getString(cursor.getColumnIndex("foodProtein")));
                food.setFoodFat(cursor.getString(cursor.getColumnIndex("foodFat")));
                food.setFoodSugar(cursor.getString(cursor.getColumnIndex("foodSugar")));
                food.setFoodNatrium(cursor.getString(cursor.getColumnIndex("foodNatrium")));
                food.setFoodCholest(cursor.getString(cursor.getColumnIndex("foodCholest")));
                food.setFoodFatty(cursor.getString(cursor.getColumnIndex("foodFatty")));
                food.setFoodTransFatty(cursor.getString(cursor.getColumnIndex("foodTransFatty")));

                result.add(food);
            } while (cursor.moveToFirst());
        }
        return result;
    }

    public List<Food> getFoods() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {
                "foodGroup ",
                "foodName"
        };

        String tableName = "mealdb";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, null,
                null, null, null, null, null);
        List<Food> result = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                Food food = new Food();

                food.setFoodGroup(cursor.getString(cursor.getColumnIndex("foodGroup")));
                food.setFoodName(cursor.getString(cursor.getColumnIndex("foodName")));

                result.add(food);
            } while (cursor.moveToFirst());
        }
        return result;
    }


    public List<String> getName() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"foodName"};
        String tableName = "mealdb";
        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, null,
                null, null, null, null, null);
        List<String> result = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                Food food = new Food();
                result.add(cursor.getString(cursor.getColumnIndex("foodName")));
            } while (cursor.moveToFirst());
        }
        return result;
    }


    public List<Food> getGroupByName(String name) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {
                "foodNumber",
                "foodGroup ",
                "foodName",
                "foodAmount",
                "foodKcal",
                "foodCarbo",
                "foodProtein",
                "foodFat",
                "foodSugar",
                "foodNatrium",
                "foodCholest",
                "foodFatty",
                "foodTransFatty"
        };

        String tableName = "mealdb";

        qb.setTables(tableName);
        Cursor cursor = qb.query(db, sqlSelect, "foodName like ?",
                new String[]{"%" + name + "%"}, null, null, null, null);
        List<Food> result = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                Food food = new Food();
                food.setFoodNumber(cursor.getString(cursor.getColumnIndex("foodNumber")));
                food.setFoodGroup(cursor.getString(cursor.getColumnIndex("foodGroup")));
                food.setFoodName(cursor.getString(cursor.getColumnIndex("foodName")));
                food.setFoodAmount(cursor.getString(cursor.getColumnIndex("foodAmount")));
                food.setFoodKcal(cursor.getString(cursor.getColumnIndex("foodKcal")));
                food.setFoodCarbo(cursor.getString(cursor.getColumnIndex("foodCarbo")));
                food.setFoodProtein(cursor.getString(cursor.getColumnIndex("foodProtein")));
                food.setFoodFat(cursor.getString(cursor.getColumnIndex("foodFat")));
                food.setFoodSugar(cursor.getString(cursor.getColumnIndex("foodSugar")));
                food.setFoodNatrium(cursor.getString(cursor.getColumnIndex("foodNatrium")));
                food.setFoodCholest(cursor.getString(cursor.getColumnIndex("foodCholest")));
                food.setFoodFatty(cursor.getString(cursor.getColumnIndex("foodFatty")));
                food.setFoodTransFatty(cursor.getString(cursor.getColumnIndex("foodTransFatty")));

                result.add(food);
            } while (cursor.moveToFirst());
        }
        return result;

    }


}


