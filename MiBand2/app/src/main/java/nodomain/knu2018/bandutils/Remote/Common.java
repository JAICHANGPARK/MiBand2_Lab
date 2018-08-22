package nodomain.knu2018.bandutils.remote;

public class Common {
    public static final String BASE_URL = "http://kangwonelec.com/";

    public static FoodDataFetch getFoodDatabaseVersionCheck(){
        return RetrofitClient.getRetrofitClient(BASE_URL).create(FoodDataFetch.class);
    }

    public static FoodDataFetch getFoodDatabaseRequest(){
        return RetrofitClient.getRetrofitClient(BASE_URL).create(FoodDataFetch.class);
    }

}
