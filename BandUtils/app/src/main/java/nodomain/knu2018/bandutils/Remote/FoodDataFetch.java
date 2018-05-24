package nodomain.knu2018.bandutils.Remote;


import nodomain.knu2018.bandutils.model.foodmodel.Foods;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FoodDataFetch {

    @GET("meal/code/{name}")
    Call<Foods> fetchFood(@Path("name") String foodKind);

    @GET("meal/{name}")
    Call<Foods> fetchTotalFood(@Path("name") String foodKind);

}
