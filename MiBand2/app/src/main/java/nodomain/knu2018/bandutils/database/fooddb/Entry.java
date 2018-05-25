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

    public static final String SQL_DELETE_BS_ENTRIES = "DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME;



    public Entry() {
    }

    public static class FoodEntry implements BaseColumns{
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



}
