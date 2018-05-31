package nodomain.knu2018.bandutils.Remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 *   서버에 생활 패턴을 기록하기 위한 인터페이스
 *    @author : 박제창 (Dreamwalker)
 */

public interface IPatternAPI {


    @FormUrlEncoded
    @POST("bandutils/code/userBSRegister.php")
    Call<ResponseBody> writeBloodSugar(
            @Field("userName") String userName,
            @Field("date") String date,
            @Field("time") String time,
            @Field("type") String type,
            @Field("value") String value);


    @FormUrlEncoded
    @POST("bandutils/code/userFitnessRegister.php")
    Call<ResponseBody> writeFitness(
            @Field("userName") String userName,
            @Field("date") String date,
            @Field("time") String time,
            @Field("type") String type,
            @Field("value") String value,
            @Field("load") String load);

    @FormUrlEncoded
    @POST("bandutils/code/userDrugRegister.php")
    Call<ResponseBody> writeDrug(
            @Field("userName") String userName,
            @Field("date") String date,
            @Field("time") String time,
            @Field("type1") String type1,
            @Field("type2") String type2,
            @Field("value") String value);

    @FormUrlEncoded
    @POST("bandutils/code/userSleepRegister.php")
    Call<ResponseBody> writeSleep(
            @Field("userName") String userName,
            @Field("type") String type,
            @Field("startDate") String startDate,
            @Field("startTime") String startTime,
            @Field("endDate") String endDate,
            @Field("endTime") String endTime,
            @Field("duration") String duration,
            @Field("satisfaction") String satisfaction);

}
