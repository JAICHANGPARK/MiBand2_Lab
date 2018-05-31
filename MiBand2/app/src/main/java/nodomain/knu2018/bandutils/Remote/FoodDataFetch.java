package nodomain.knu2018.bandutils.Remote;


import nodomain.knu2018.bandutils.model.foodmodel.Foods;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *   서버의 식품, 음식, 가공식품 데이터베이스를 가져오는 인터페이스
 *   @author : 박제창 (Dreamwalker)
 */

public interface FoodDataFetch {

    @GET("meal/code/{name}")
    Call<Foods> fetchFood(@Path("name") String foodKind);

    @GET("meal/{name}")
    Call<Foods> fetchTotalFood(@Path("name") String foodKind);

}
