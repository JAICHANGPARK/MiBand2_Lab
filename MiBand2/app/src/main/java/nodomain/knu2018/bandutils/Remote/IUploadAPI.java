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

public interface IUploadAPI {

    //@POST("upload/upload.php")
    @Multipart
    @POST("bandutils/code/upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part file, @Part("userUUID") RequestBody uuid);

    @FormUrlEncoded
    @POST("bandutils/code/userRegister.php")
    Call<ResponseBody> registerUser(@Field("userName") String name, @Field("userUUID") String uuid);

}
