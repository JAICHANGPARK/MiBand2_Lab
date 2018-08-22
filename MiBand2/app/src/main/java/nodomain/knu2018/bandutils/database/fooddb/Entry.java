package nodomain.knu2018.bandutils.database.fooddb;

import android.provider.BaseColumns;

public final class Entry {


    public static final String SQL_CREATE_FOOD_DB_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FoodEntry.TABLE_NAME + " (" +
                    FoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FoodEntry.COLUNM_NAME_NUMBER + " TEXT," +
                    FoodEntry.COLUNM_NAME_GROUP + " TEXT, " +
                    FoodEntry.COLUNM_NAME_FOOD_NAME + " TEXT, " +
                    FoodEntry.COLUNM_NAME_AMOUNT + " TEXT, " +
                    FoodEntry.COLUNM_NAME_KCAL + " TEXT, " +
                    FoodEntry.COLUNM_NAME_CARBO + " TEXT, " +
                    FoodEntry.COLUNM_NAME_PROTEIN + " TEXT, " +
                    FoodEntry.COLUNM_NAME_FAT + " TEXT, " +
                    FoodEntry.COLUNM_NAME_SUGAR + " TEXT, " +
                    FoodEntry.COLUNM_NAME_NATRIUM + " TEXT, " +
                    FoodEntry.COLUNM_NAME_CHOLEST + " TEXT, " +
                    FoodEntry.COLUNM_NAME_FATTY + " TEXT, " +
                    FoodEntry.COLUNM_NAME_TRANS_FATTY + " TEXT " + " )";

    public static final String SQL_CREATE_FOOD_DB_V2_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FoodEntryV2.TABLE_NAME + " (" +
                    FoodEntryV2._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FoodEntryV2.COLUNM_DB_CLASS + " TEXT," +
                    FoodEntryV2.COLUNM_FOOD_CLASS + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_NAME + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_AMOUNT + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_GROUP_1 + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_GROUP_2 + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_GROUP_3 + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_GROUP_4 + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_GROUP_5 + " TEXT, " +
                    FoodEntryV2.COLUNM_FOOD_GROUP_6 + " TEXT, " +
                    FoodEntryV2.COLUNM_TOTAL_EXCHANGE + " TEXT, " +
                    FoodEntryV2.COLUNM_KCAL + " TEXT, " +
                    FoodEntryV2.COLUNM_CARBO + " TEXT, " +
                    FoodEntryV2.COLUNM_FATT + " TEXT, " +
                    FoodEntryV2.COLUNM_PROTEIN + " TEXT, " +
                    FoodEntryV2.COLUNM_FIBER + " TEXT " + " )";

    public static final String SQL_DELETE_BS_ENTRIES = "DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME;
    public static final String SQL_DELETE_FOOD_DB_V2_ENTRIES = "DROP TABLE IF EXISTS " + FoodEntryV2.TABLE_NAME;

    public Entry() {
    }

    public static class FoodEntry implements BaseColumns {
        public static final String TABLE_NAME = "mealdb";
        public static final String COLUNM_NAME_NUMBER = "foodNumber";
        public static final String COLUNM_NAME_GROUP = "foodGroup";
        public static final String COLUNM_NAME_FOOD_NAME = "foodName";
        public static final String COLUNM_NAME_AMOUNT = "foodAmount";

        public static final String COLUNM_NAME_KCAL = "foodKcal";
        public static final String COLUNM_NAME_CARBO = "foodCarbo";
        public static final String COLUNM_NAME_PROTEIN = "foodProtein";
        public static final String COLUNM_NAME_FAT = "foodFat";
        public static final String COLUNM_NAME_SUGAR = "foodSugar";
        public static final String COLUNM_NAME_NATRIUM = "foodNatrium ";
        public static final String COLUNM_NAME_CHOLEST = "foodCholest";
        public static final String COLUNM_NAME_FATTY = "foodFatty";
        public static final String COLUNM_NAME_TRANS_FATTY = "foodTransFatty";
    }

    /**
     * 데이터 베이스 버전 2
     * dbClass : 데이터 베이스 종류
     * foodClass : 밥류
     * foodName : 김밥
     * foodAmount : 191
     * foodGroup1 : 0.9
     * foodGroup2 : 0.8
     * foodGroup3 : 2
     * foodGroup4 : 0
     * foodGroup5 : 0
     * foodGroup6 : 1.6
     * totalExchange : 5.3
     * kcal : 445
     * carbo : 71.2
     * fatt : 12.8
     * prot : 10.7
     * fiber : 3.3
     */

    public static class FoodEntryV2 implements BaseColumns {
        public static final String TABLE_NAME = "mealdb_v2";

        public static final String COLUNM_DB_CLASS = "dbClass";
        public static final String COLUNM_FOOD_CLASS = "foodClass";
        public static final String COLUNM_FOOD_NAME = "foodName";
        public static final String COLUNM_FOOD_AMOUNT = "foodAmount";
        public static final String COLUNM_FOOD_GROUP_1 = "foodGroup1";
        public static final String COLUNM_FOOD_GROUP_2 = "foodGroup2";
        public static final String COLUNM_FOOD_GROUP_3 = "foodGroup3";
        public static final String COLUNM_FOOD_GROUP_4 = "foodGroup4";
        public static final String COLUNM_FOOD_GROUP_5 = "foodGroup5";
        public static final String COLUNM_FOOD_GROUP_6 = "foodGroup6";
        public static final String COLUNM_TOTAL_EXCHANGE = "totalExchange";
        public static final String COLUNM_KCAL = "kcal";
        public static final String COLUNM_CARBO = "carbo";
        public static final String COLUNM_FATT = "fatt";
        public static final String COLUNM_PROTEIN = "prot";
        public static final String COLUNM_FIBER = "fiber";

    }


}
