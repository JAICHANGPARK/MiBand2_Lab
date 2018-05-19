package nodomain.knu2018.gadgetbridge.database;

import android.provider.BaseColumns;

public final class WriteEntry {

    public WriteEntry() {
    }

    public static class BloodSugarEntry implements BaseColumns{

        public static final String TABLE_NAME = "bloodsugar";
        public static final String COLUNM_NAME_DATE = "date";
        public static final String COLUNM_NAME_TIME = "time";
        public static final String COLUNM_NAME_TYPE = "type";
        public static final String COLUNM_NAME_VALUE = "value";
    }

    public static class FitnessEntry implements BaseColumns{

        public static final String TABLE_NAME = "fitness";
        public static final String COLUNM_NAME_DATE = "date";
        public static final String COLUNM_NAME_TIME = "time";
        public static final String COLUNM_NAME_TYPE = "type";
        public static final String COLUNM_NAME_VALUE = "value";
        public static final String COLUNM_NAME_LOAD = "load";
    }

    public static class DrugEntry implements BaseColumns{
        public static final String TABLE_NAME = "drug";
        public static final String COLUNM_NAME_DATE = "date";
        public static final String COLUNM_NAME_TIME = "time";
        public static final String COLUNM_NAME_TYPE_TOP = "type1";
        public static final String COLUNM_NAME_TYPE_BOTTOM = "type2";
        public static final String COLUNM_NAME_VALUE = "value";
    }

    public static class MealEntry implements BaseColumns{

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

    public static class SleepEntry implements BaseColumns{

        public static final String TABLE_NAME = "sleep";
        public static final String COLUNM_NAME_DATE = "date";
        public static final String COLUNM_NAME_TIME = "time";
        public static final String COLUNM_NAME_START_DATE = "startdate";
        public static final String COLUNM_NAME_START_TIME = "starttime";
        public static final String COLUNM_NAME_END_DATE = "enddate";
        public static final String COLUNM_NAME_END_TIME = "endtime";
        public static final String COLUNM_NAME_SLEEP_TIME = "duration";
        public static final String COLUNM_NAME_TYPE = "type";
        public static final String COLUNM_NAME_VALUE_SATISFACTION = "satisfaction";

    }
}
