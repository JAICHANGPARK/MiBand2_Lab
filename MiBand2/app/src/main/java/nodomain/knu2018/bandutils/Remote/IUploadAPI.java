package nodomain.knu2018.bandutils.Remote;

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
 *
 *       ____  ____  _________    __  ____       _____    __    __ __ __________
 *      / __ \/ __ \/ ____/   |  /  |/  / |     / /   |  / /   / //_// ____/ __ \
 *     / / / / /_/ / __/ / /| | / /|_/ /| | /| / / /| | / /   / ,<  / __/ / /_/ /
 *    / /_/ / _, _/ /___/ ___ |/ /  / / | |/ |/ / ___ |/ /___/ /| |/ /___/ _, _/
 *   /_____/_/ |_/_____/_/  |_/_/  /_/  |__/|__/_/  |_/_____/_/ |_/_____/_/ |_|
 *
 *   Created by Dreamwalker on 2018-05-25.
 *
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
                                     @Field("rating") String rating,
                                     @Field("comment") String comment);
}
