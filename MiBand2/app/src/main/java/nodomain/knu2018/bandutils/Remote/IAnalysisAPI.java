package nodomain.knu2018.bandutils.remote;

import nodomain.knu2018.bandutils.model.analysis.Ranks;
import nodomain.knu2018.bandutils.model.analysis.ResponseAnalysis;
import nodomain.knu2018.bandutils.model.analysis.WriteCount;
import nodomain.knu2018.bandutils.model.analysis.WriteCountDays;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;



/**
 *   사용자 기록 통계를 가져오는 인터페이스
 *    @author : 박제창 (Dreamwalker)
 */

public interface IAnalysisAPI {

    @FormUrlEncoded
    @POST("bandutils/code/userWriteCount.php")
    Call<ResponseAnalysis> getCount(@Field("userDate") String today);

    @FormUrlEncoded
    @POST("bandutils/code/getMyRank.php")
    Call<ResponseAnalysis> getMyRank(@Field("userName") String name);

    @FormUrlEncoded
    @POST("bandutils/code/getUserRank.php")
    Call<ResponseAnalysis> getAllRank(@Field("userName") String name);


    @POST("bandutils/code/rank/getWriteCount.php")
    Call<WriteCount> getWriteCount();

    @FormUrlEncoded
    @POST("bandutils/code/rank/getMyRank.php")
    Call<Ranks> getMyRanking(@Field("userName") String name);

    @POST("bandutils/code/rank/getRanking.php")
    Call<Ranks> getRanking();

    @FormUrlEncoded
    @POST("bandutils/code/rank/getUserCountDay.php")
    Call<WriteCountDays> getMyCountOfDay(@Field("userName") String name);

}
