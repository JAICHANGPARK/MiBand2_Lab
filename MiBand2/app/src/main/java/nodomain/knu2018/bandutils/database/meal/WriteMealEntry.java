package nodomain.knu2018.bandutils.database.meal;

import android.provider.BaseColumns;

public final class WriteMealEntry {

    public WriteMealEntry() {
    }



    public static class MealEntry implements BaseColumns {

        public static final String TABLE_NAME = "meal";
        public static final String COLUNM_NAME_DATE = "date";
        public static final String COLUNM_NAME_TIME = "time";
        public static final String COLUNM_NAME_START_DATE = "startdate";
        public static final String COLUNM_NAME_START_TIME = "starttime";
        public static final String COLUNM_NAME_END_DATE = "enddate";
        public static final String COLUNM_NAME_END_TIME = "endtime";
        public static final String COLUNM_NAME_FOOD_TIME = "duration";
        public static final String COLUNM_NAME_TYPE = "type";
        public static final String COLUNM_NAME_VALUE_GOKRYU = "gokryu";
        public static final String COLUNM_NAME_VALUE_BEEF = "beef";
        public static final String COLUNM_NAME_VALUE_VEGETABLE = "vegetable";
        public static final String COLUNM_NAME_VALUE_FAT = "fat";
        public static final String COLUNM_NAME_VALUE_MILK = "milk";
        public static final String COLUNM_NAME_VALUE_FRUIT = "fruit";
        public static final String COLUNM_NAME_VALUE_EXCHANGE = "exchange";
        public static final String COLUNM_NAME_VALUE_KCAL = "kcal";
        public static final String COLUNM_NAME_VALUE_SATISFACTION = "satisfaction";

    }



    public static class MealEntryTop implements BaseColumns {
        public static final String TABLE_NAME = "meal_main";
        public static final String COLUNM_ID = "id";
        public static final String COLUNM_TOTAL_FOOD_COUNT = "totalFoodCount";
        public static final String COLUNM_TOTAL_AMOUNT = "totalAmount";
        public static final String COLUNM_TOTAL_KCAL = "totalKCal";
        public static final String COLUNM_TOTAL_EXCHANGE = "totalExchange";
        public static final String COLUNM_TOTAL_SAVE_DATETIME = "saveDatetime";
        public static final String COLUNM_TOTAL_SAVE_TIMESTAMP = "saveTimestamp";
        public static final String COLUNM_TOTAL_START_DATETIME = "startDatetime";
        public static final String COLUNM_TOTAL_START_TIMESTAMP = "startTimestamp";
        public static final String COLUNM_TOTAL_END_DATETIME = "endDatetime";
        public static final String COLUNM_TOTAL_END_TIMESTAMP = "endTimestamp";
        public static final String COLUNM_INTAKE_TIME = "intakeTime";

    }

    public static class MealEntryMid implements BaseColumns {
        public static final String TABLE_NAME = "meal_mid";

    }


}
