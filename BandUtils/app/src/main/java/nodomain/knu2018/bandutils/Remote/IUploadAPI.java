package nodomain.knu2018.bandutils.Remote;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadAPI {

    @Multipart
    @POST("upload/upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("bandutils/code/userRegister.php")
    Call<ResponseBody> registerUser(@Field("userName") String name, @Field("userUUID") String uuid);

}
