package nodomain.knu2018.bandutils.Remote;

import nodomain.knu2018.bandutils.model.analysis.ResponseAnalysis;
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

}
