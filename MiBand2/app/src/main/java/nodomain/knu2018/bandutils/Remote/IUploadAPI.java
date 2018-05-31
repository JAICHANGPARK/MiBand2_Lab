package nodomain.knu2018.bandutils.Remote;

import nodomain.knu2018.bandutils.model.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * ____  ____  _________    __  ____       _____    __    __ __ __________
 * / __ \/ __ \/ ____/   |  /  |/  / |     / /   |  / /   / //_// ____/ __ \
 * / / / / /_/ / __/ / /| | / /|_/ /| | /| / / /| | / /   / ,<  / __/ / /_/ /
 * / /_/ / _, _/ /___/ ___ |/ /  / / | |/ |/ / ___ |/ /___/ /| |/ /___/ _, _/
 * /_____/_/ |_/_____/_/  |_/_/  /_/  |__/|__/_/  |_/_____/_/ |_/_____/_/ |_|
 * <p>
 * 사진 업로드
 * 사용자 등록
 * 앱 평가
 * 식사 기록
 * 인터페이스
 * Created by Dreamwalker on 2018-05-25.
 */


public interface IUploadAPI {

    //@POST("upload/upload.php")
    @Multipart
    @POST("bandutils/code/upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part file, @Part("userUUID") RequestBody uuid);

    @FormUrlEncoded
    @POST("bandutils/code/userRegister.php")
    Call<ResponseBody> registerUser(@Field("userName") String name, @Field("userUUID") String uuid);

    @FormUrlEncoded
    @POST("bandutils/code/userAppRating.php")
    Call<ResponseBody> userAppRating(@Field("userName") String name,
                                     @Field("userUUID") String uuid,
                                     @Field("userStar") String rating,
                                     @Field("userComment") String comment);

    @FormUrlEncoded
    @POST("bandutils/code/getUserInfo.php")
    Call<UserInfo> getUserInfo(@Field("userUUID") String uuid);

    @FormUrlEncoded
    @POST("bandutils/code/userInfoUpdate.php")
    Call<UserInfo> updateUserInfo(@Field("userName") String name,
                                  @Field("userUUID") String uuid,
                                  @Field("phone") String phone,
                                  @Field("gender") String gender,
                                  @Field("birthday") String birthday,
                                  @Field("age") String age,
                                  @Field("ageGroup") String ageGroup);

    @FormUrlEncoded
    @POST("bandutils/code/userMealRegister.php")
    Call<ResponseBody> userMealRegiste(@Field("userName") String name,
                                       @Field("userPhoto") String userPhoto,
                                       @Field("type") String type,
                                       @Field("startDate") String startDate,
                                       @Field("startTime") String startTime,
                                       @Field("endDate") String endDate,
                                       @Field("endTime") String endTime,
                                       @Field("duration") String duration,

                                       @Field("gokryu") String gokryu,
                                       @Field("beef") String beef,
                                       @Field("vegetable") String vegetable,
                                       @Field("fat") String fat,
                                       @Field("milk") String milk,
                                       @Field("fruit") String fruit,

                                       @Field("exchange") String exchange,
                                       @Field("kcal") String kcal,
                                       @Field("satisfaction") String satisfaction);

}
