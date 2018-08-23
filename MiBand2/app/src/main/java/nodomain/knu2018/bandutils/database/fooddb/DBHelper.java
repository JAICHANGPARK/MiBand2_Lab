package nodomain.knu2018.bandutils.database.fooddb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nodomain.knu2018.bandutils.model.foodmodel.Food;
import nodomain.knu2018.bandutils.model.foodmodel.MixedFood;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    Context context;
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "meal_db.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Entry.SQL_CREATE_FOOD_DB_ENTRIES);
        db.execSQL(Entry.SQL_CREATE_FOOD_DB_V2_ENTRIES);
        //Toast.makeText(context, "DB 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 3){
            db.execSQL(Entry.SQL_DELETE_FOOD_DB_V2_ENTRIES);
            onCreate(db);
        }else {
            db.execSQL(Entry.SQL_DELETE_BS_ENTRIES);
            onCreate(db);
        }

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

    /**
     * 두번째 버전 데이터베이스를 서버로 부터 읽어온 데이터를
     * 저장하는 메소드
      * @param contentValues
     * @param versionCode
     */
    public void addFood(ContentValues contentValues, int versionCode) {

        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        switch (versionCode) {
            case 1:
                break;
            case 2:
                db.insert(Entry.FoodEntryV2.TABLE_NAME, null, contentValues);
                break;
            default:
                break;
        }
        db.close();
    }


    public Map<String, String> fetchDetailFoodInfo(String foodName) {

        Map<String, String> foodMap = new HashMap<>();
        SQLiteDatabase database = getWritableDatabase();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SELECT foodNumber, foodGroup, foodName, " +
                "foodAmount, foodKcal, foodCarbo, foodProtein," +
                "foodFat, foodSugar, foodNatrium, foodCholest," +
                "foodFatty,foodTransFatty FROM mealdb ");
        stringBuilder.append("WHERE foodName = ");
        stringBuilder.append("'");
        stringBuilder.append(foodName);
        stringBuilder.append("'");

        Cursor cursor = database.rawQuery(stringBuilder.toString(), null);

        while (cursor.moveToNext()) {

            foodMap.put(Entry.FoodEntry.COLUNM_NAME_NUMBER, cursor.getString(0));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_GROUP, cursor.getString(1));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_FOOD_NAME, cursor.getString(2));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_AMOUNT, cursor.getString(3));

            foodMap.put(Entry.FoodEntry.COLUNM_NAME_KCAL, cursor.getString(4));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_CARBO, cursor.getString(5));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_PROTEIN, cursor.getString(6));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_FAT, cursor.getString(7));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_SUGAR, cursor.getString(8));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_NATRIUM, cursor.getString(9));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_CHOLEST, cursor.getString(10));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_FATTY, cursor.getString(11));
            foodMap.put(Entry.FoodEntry.COLUNM_NAME_TRANS_FATTY, cursor.getString(12));

        }
        return foodMap;
    }

    public ArrayList<String> readNameDate() {
        ArrayList<String> nameArrayList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        SQLiteDatabase db = getWritableDatabase();
        String valueName;
        String valueGroup;


        Cursor cursor = null;
        // TODO: 2018-06-25 모든 데이터에서 음식 이름만 가져오기 - 박제창
        sb.append(" SELECT foodName FROM mealdb");
        sb.append(" LIMIT 5");

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
        // TODO: 2018-06-25 모든 데이터에서 음식 이름, 식품군 가져오기 - 박제창
        sb.append(" SELECT foodName, foodGroup FROM mealdb");
        sb.append(" LIMIT 10");

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
        // TODO: 2018-06-25 검색되는 음식 이름, 식품군 가져오기 - 박제창
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

    public ArrayList<MixedFood> searchDB(String name) {
        ArrayList<MixedFood> nameArrayList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        SQLiteDatabase db = getWritableDatabase();
        String valueName;
        String valueGroup;


        Cursor cursor = null;
        // TODO: 2018-08-23 데이터베이스 버전 2 의 이름과 그룹 가져오기
        sb.append(" SELECT foodName, foodClass FROM mealdb_v2 ");
        sb.append(" WHERE foodName LIKE '%" + name + "%'");
        //sb.append(" LIMIT 20");

        cursor = db.rawQuery(sb.toString(), null);


        while (cursor.moveToNext()) {
            valueName = cursor.getString(0);
            valueGroup = cursor.getString(1);
            nameArrayList.add(new MixedFood(valueGroup, valueName));
        }
        return nameArrayList;
    }

    /**
     * 두번째 데이터베이스 검색한 입력 쿼리를 받아
     * 전부 검색하여 가져오는 메소드
     *
     * @param name
     * @return
     */
    public ArrayList<MixedFood> searchDBAll(String name) {
        ArrayList<MixedFood> nameArrayList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        SQLiteDatabase db = getWritableDatabase();

        sb.append(" SELECT ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_CLASS + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_NAME + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_AMOUNT + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_1 + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_2 + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_3 + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_4 + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_5 + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FOOD_GROUP_6 + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_TOTAL_EXCHANGE + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_KCAL + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_CARBO + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FATT + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_PROTEIN + ", ");
        sb.append(Entry.FoodEntryV2.COLUNM_FIBER + " ");
        sb.append("FROM " + Entry.FoodEntryV2.TABLE_NAME );
        sb.append(" WHERE foodName LIKE '%" + name + "%'");

//        // TODO: 2018-08-23 데이터베이스 버전 2 의 이름과 그룹 가져오기
//        sb.append(" SELECT * FROM mealdb_v2 ");
//        sb.append(" WHERE foodName LIKE '%" + name + "%'");
        //sb.append(" LIMIT 20");

        Cursor cursor = db.rawQuery(sb.toString(), null);


        while (cursor.moveToNext()) {
            nameArrayList.add(
                    new MixedFood(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getString(14)));
        }
        cursor.close();
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

    public boolean truncateTable(int versionCode) {
        SQLiteDatabase db = getWritableDatabase();

        switch (versionCode) {
            case 1:
                return false;
            case 2:
                db.execSQL("DELETE FROM " + Entry.FoodEntryV2.TABLE_NAME);
                return true;
        }
        return false;
    }

    public int countDatabaseSize(){
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder stringBuilder = new StringBuilder();

        Cursor cursor = null;
        // TODO: 2018-06-25 모든 데이터에서 음식 이름, 식품군 가져오기 - 박제창
        stringBuilder.append("SELECT count( " + Entry.FoodEntryV2.COLUNM_FOOD_NAME  + ") ");
        stringBuilder.append("FROM " + Entry.FoodEntryV2.TABLE_NAME);

        cursor = db.rawQuery(stringBuilder.toString(), null);

        while (cursor.moveToNext()){
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;

    }

}


