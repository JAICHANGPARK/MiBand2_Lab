package nodomain.knu2018.bandutils.remote;


import java.util.ArrayList;

import nodomain.knu2018.bandutils.model.foodmodel.AppVersion;
import nodomain.knu2018.bandutils.model.foodmodel.Foods;
import nodomain.knu2018.bandutils.model.foodmodel.MixedFood;
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

    //http://kangwonelec.com/version_factory/bandutil/bandutil_version.php
    @GET("version_factory/bandutil/{name}")
    Call<AppVersion> checkFoodDatabaseVersion(@Path("name") String versionCheckPath);

    // TODO: 2018-08-22 첫번째 버전의 데이터베이스를 가져온다.
    @GET("meal/{name}")
    Call<Foods> fetchTotalFood(@Path("name") String foodKind);

    // TODO: 2018-08-22 데이터베이스 버전 2 를 가져온다.
    @GET("food/{name}")
    Call<ArrayList<MixedFood>> fetchFoodDatabaseVersion2(@Path("name") String foodKind);

}
