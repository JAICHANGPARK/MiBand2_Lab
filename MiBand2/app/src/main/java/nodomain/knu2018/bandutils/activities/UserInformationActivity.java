package nodomain.knu2018.bandutils.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.Remote.IUploadAPI;
import nodomain.knu2018.bandutils.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;

/**
 * 사용자 정보를 가져오고 서버에 업로드
 * 1. userName
 * 2. userUUID
 * 3. userGender
 * 4. userAge
 * 5. userBirth
 * 6. userPhone
 * 각 필드의 변수는 해시맵에 임시 저장하는 방식을 사용했습니다.
 *
 * @author : 박제창(Dreamwalker)
 */

public class UserInformationActivity extends AppCompatActivity {

    private static final String TAG = "UserInformationActivity";
    private static final int REQUEST_CODE = 1000;

    @BindView(R.id.user_name)
    TextView userNameTextView;

    @BindView(R.id.user_uuid)
    TextView userUUIDTextView;
    @BindView(R.id.user_gender)
    TextView userGenderTextView;
    @BindView(R.id.user_age)
    TextView userAgeTextview;
    @BindView(R.id.user_birth)
    TextView userBirthTextView;

    @BindView(R.id.user_phone)
    TextView userPhoneTextView;
    @BindView(R.id.phone_button)
    Button buttonPhoneRegister;

    @BindView(R.id.age_button)
    Button buttonAge;

    @BindView(R.id.gender_button)
    Button buttonGender;

    @BindView(R.id.birth_button)
    Button buttonBirth;

    @BindView(R.id.save_button)
    Button buttonSave;

    Map<String, String> userInfo = new HashMap<>();

    Retrofit retrofit;
    IUploadAPI service;

    NetworkInfo networkInfo;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        setTitle("사용자 정보");
        ButterKnife.bind(this);
        Paper.init(this);
        initRetrofit();
        networkInfo = getNetworkInfo();


        userInfo.put("userName", Paper.book().read("userName"));
        userInfo.put("userUUID", Paper.book().read("userUUIDV2"));

        userNameTextView.setText(userInfo.get("userName"));
        userUUIDTextView.setText(userInfo.get("userUUID"));

        // TODO: 2018-05-30 서버에서 값을 가져온다.
        initServerFetch();
        // TODO: 2018-05-30 사용자 정보를 수정한다. 
        // TODO: 2018-05-30 저장버튼을 눌러 서버에 업데이트한다. 
        // TODO: 2018-05-30 캐시에 저장한다.
        // TODO: 2018-05-30 캐시 데이터를 가져온다.

    }

    /**
     * 서버에 등록되어있는 사용자 값을 가져온다.
     *
     * @author : 박제창 (dreamwalker)
     */
    private void initServerFetch() {

        String userUUID = userInfo.get("userUUID");

        if (networkInfo != null && networkInfo.isConnected()) {
            progressDialog.show();
            Call<UserInfo> request = service.getUserInfo(userUUID);
            request.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    Log.e(TAG, "onResponse: " + response.body().getResponse().size());
                    if (response.isSuccessful()) {
                        UserInfo tmpInfo = response.body();
                        String userName = tmpInfo.getResponse().get(0).getUserName();
                        String userGender = tmpInfo.getResponse().get(0).getGender();
                        String userAge = tmpInfo.getResponse().get(0).getAge();
                        String userAgeGroup = tmpInfo.getResponse().get(0).getAgeGroup();
                        String userBirth = tmpInfo.getResponse().get(0).getBirthday();
                        String userPhone = tmpInfo.getResponse().get(0).getPhone();

                        userNameTextView.setText(userName);

                        if (userGender.length() == 0) {
                            String massage = "등록된 정보가 없습니다.";
                            userGenderTextView.setText(massage);
                        } else {
                            userGenderTextView.setText(userGender);
                            Paper.book().write("userGender", userGender);
                            userInfo.put("userGender", userGender);
                        }

                        if (userAge.length() == 0) {
                            String massage = "등록된 정보가 없습니다.";
                            userAgeTextview.setText(massage);
                        } else {
                            userAgeTextview.setText(userAge);
                            Paper.book().write("userAge", userAge);
                            userInfo.put("userAge", userAge);
                        }

                        if (userBirth.length() == 0) {
                            String massage = "등록된 정보가 없습니다.";
                            userBirthTextView.setText(massage);
                        } else {
                            userBirthTextView.setText(userBirth);
                            Paper.book().write("userBirth", userBirth);
                            userInfo.put("userBirth", userBirth);
                        }

                        if (userPhone.length() == 0) {
                            String massage = "등록된 정보가 없습니다.";
                            userPhoneTextView.setText(massage);
                        } else {
                            userPhoneTextView.setText(userPhone);
                            Paper.book().write("userPhone", userPhone);
                            userInfo.put("userPhone", userPhone);
                            buttonPhoneRegister.setVisibility(View.GONE);
                        }
                        //Log.e(TAG, "onResponse: " + user.getUserName() + user.getUserUUID() + user.getPhone() + user.getGender() + user.getBirthday());

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(UserInformationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            // TODO: 2018-05-30 인터넷 연결이 없다면 캐시 데이터를 가져온다.
            Snackbar.make(getWindow().getDecorView().getRootView(), "네트워크를 연결해주세요. 캐시 정보를 가져왔어요", Snackbar.LENGTH_SHORT).show();

            if (Paper.book().read("userGender") != null && Paper.book().read("userGender").toString().length() != 0) {
                // TODO: 2018-05-30 데이터가 있다면.
                String userGender = Paper.book().read("userGender");
                userGenderTextView.setText(userGender);
            } else {
                String massage = "등록된 정보가 없습니다.";
                userGenderTextView.setText(massage);
            }

            if (Paper.book().read("userAge") != null) {
                // TODO: 2018-05-30 데이터가 있다면.
                String userAge = Paper.book().read("userAge");
                userAgeTextview.setText(userAge);
            } else {
                String massage = "등록된 정보가 없습니다.";
                userAgeTextview.setText(massage);
            }

            if (Paper.book().read("userBirth") != null) {
                // TODO: 2018-05-30 데이터가 있다면.
                String userBirth = Paper.book().read("userBirth");
                userBirthTextView.setText(userBirth);
            } else {
                String massage = "등록된 정보가 없습니다.";
                userBirthTextView.setText(massage);
            }

            if (Paper.book().read("userPhone") != null) {
                // TODO: 2018-05-30 데이터가 있다면.
                String userPhone = Paper.book().read("userPhone");
                userPhoneTextView.setText(userPhone);
                buttonPhoneRegister.setVisibility(View.GONE);
            } else {
                String massage = "등록된 정보가 없습니다.";
                userPhoneTextView.setText(massage);
            }

        }


    }

    /**
     * 네트워크 시스템 연결 객체를 가져오는 메소드
     *
     * @return
     * @author : 박제창 (Dreamwalker)
     */
    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;

    }

    /**
     * Retrofit 객체 생성
     *
     * @author : 박제창(Dreamwalker)
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IUploadAPI.class);
        progressDialog = new ProgressDialog(UserInformationActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("처리중...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

//        if (networkInfo != null && networkInfo.isConnected()) {
//            progressDialog.show();
//        }

    }

    /**
     * 나이 수정 버튼 리스너
     * --------------------
     * 나이 : 변경 값(대)
     * 0~9 : 0 대
     * 10~19 : 10대
     * 20 ~ 29 : 20대
     * 30 ~ 39 : 30대
     * 40 ~ 49 : 40대
     * .....
     *
     * @author : Dreamwalker
     */
    @OnClick(R.id.age_button)
    public void onClickAgeButton() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("REGISTER");

        LayoutInflater inflater = this.getLayoutInflater();
        View registerLayout = inflater.inflate(R.layout.age_register_layout, null);
        MaterialEditText editText = (MaterialEditText) registerLayout.findViewById(R.id.edt);
        Button button = (Button)registerLayout.findViewById(R.id.btn_done);
        editText.setHint("Age");

        editText.addTextChangedListener(new PatternedTextWatcher("##"));
        alertDialog.setView(registerLayout);
        AlertDialog dialog = alertDialog.create();

        button.setOnClickListener(v -> {

            if (TextUtils.isEmpty(editText.getText().toString())) {
                Toast.makeText(this, "나이를 입력하세요", Toast.LENGTH_SHORT).show();
            }else {
                String userAge = editText.getText().toString();

                String transAge = null;
                int age = Integer.parseInt(userAge);
                if (age >= 90 && age < 100  ){
                    transAge = "90";
                }else if (age >= 80 && age < 90){
                    transAge = "80";
                }
                else if (age >= 70 && age < 80){
                    transAge = "70";
                }
                else if (age >= 60 && age < 70){
                    transAge = "60";
                }
                else if (age >= 50 && age < 60){
                    transAge = "50";
                }
                else if (age >= 40 && age < 50){
                    transAge = "40";
                }
                else if (age >= 30 && age < 40){
                    transAge = "30";
                }
                else if (age >= 20 && age < 30){
                    transAge = "20";
                }
                else if (age >= 10 && age < 20){
                    transAge = "10";
                }
                else if (age >= 0 && age < 10){
                    transAge = "0";
                }

                userInfo.put("userAge", userAge);
                userInfo.put("userAgeGroup", transAge);
                userAgeTextview.setText(userAge + "세 " + "(" + transAge + ")");
            }
            dialog.dismiss();
        });

        dialog.show();

    }

    /**
     * 성별 수정 버튼 리스너
     */
    @OnClick(R.id.gender_button)
    public void onClickUserGender() {
        final CharSequence[] items = {"Male", " Female"};
        final ArrayList seletedItems = new ArrayList(); // arraylist to keep the selected items
        final int[] selectedIndex = {0};

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Gender")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "onClick: " + which);
                        selectedIndex[0] = which;
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        // TODO: 2018-05-30 선택된 현재 상황 저장.
//                        Paper.book().write("userGender", items[selectedIndex[0]].toString());
                        String tmpGender =
                        userInfo.put("userGender", items[selectedIndex[0]].toString());
                        userGenderTextView.setText(items[selectedIndex[0]]);
                        dialog.dismiss();
//                        Log.e(TAG, "onClick: " + id);
//                        for (int i = 0 ; i < seletedItems.size(); i++){
//                            Log.e(TAG, "onClick: " + seletedItems.get(i));
//                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
//                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
//                        if (isChecked) {
//                            // If the user checked the item, add it to the selected items
//                            seletedItems.add(indexSelected);
//                        } else if (seletedItems.contains(indexSelected)) {
//                            // Else, if the item is already in the array, remove it
//                            seletedItems.remove(Integer.valueOf(indexSelected));
//                        }
//                    }
//                })
        dialog.show();
    }

    /**
     * 생일 정보 수정 및 등록 리스너
     */
    @OnClick(R.id.birth_button)
    public void onClickBirthButton() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("생년월일을 입력하세요");

        LayoutInflater inflater = this.getLayoutInflater();
        View registerLayout = inflater.inflate(R.layout.age_register_layout, null);
        MaterialEditText editText = (MaterialEditText) registerLayout.findViewById(R.id.edt);
        Button button = (Button)registerLayout.findViewById(R.id.btn_done);
        editText.setHint("Birthday");

        editText.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        alertDialog.setView(registerLayout);
        AlertDialog dialog = alertDialog.create();

        button.setOnClickListener(v -> {

            if (TextUtils.isEmpty(editText.getText().toString())) {
                Toast.makeText(this, "태어난 날짜를 입력하세요", Toast.LENGTH_SHORT).show();
            }else {
                String userBirth = editText.getText().toString();
                userInfo.put("userBirth", userBirth);
                userBirthTextView.setText(userBirth);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Facebook 과 연동하여 사용자 핸드폰 번호 인증을 진행합니다.
     * 필요할 수 도 필요 없을 수도 있어요 - 박제창
     *
     * @author : 박제창 (Dreamwalker)
     */
    @OnClick(R.id.phone_button)
    public void onClickPhoneButton() {
        startLoginPage(LoginType.PHONE);
    }


    /**
     * 저장버튼을 눌렀을 떄 정보를 업데이트 처리하는 메소드 입니다.
     * 네트워크 연결된 상태와 안된 상태의 처리가 중요합니다. - 박제창
     *
     * @author : 박제창 (Dreamwalker)
     */
    @OnClick(R.id.save_button)
    public void onClickSaveButton(View v) {

        progressDialog.show();
        if (networkInfo != null && networkInfo.isConnected()) {

            Call<UserInfo> request = service.updateUserInfo(
                    userInfo.get("userName"),
                    userInfo.get("userUUID"),
                    userInfo.get("userPhone"),
                    userInfo.get("userGender"),
                    userInfo.get("userBirth"),
                    userInfo.get("userAge"),
                    userInfo.get("userAgeGroup"));

            request.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Toast.makeText(UserInformationActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });

        } else {
            progressDialog.dismiss();
            Snackbar.make(v, "네트워크를 연결하세요", Snackbar.LENGTH_SHORT).show();
        }
    }


    /**
     * 페이스북 SMS 인증으로 넘어갑니다. 쉽지 않네요~ = 박제창
     *
     * @param loginType
     * @author : 박제창 (Dreamwalker)
     */
    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(UserInformationActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder = new AccountKitConfiguration
                .AccountKitConfigurationBuilder(loginType, AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }


    /**
     * Intent 결과 후 처리를 진행하는 곳 .
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "Cancle", Toast.LENGTH_SHORT).show();
            } else {
                if (result.getAccessToken() != null) {
                    android.app.AlertDialog alertDialog = new SpotsDialog(UserInformationActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please waiting");

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(Account account) {

                            Log.e(TAG, "onSuccess: Phone " + account.getPhoneNumber().toString());

                            String phoneNumber = account.getPhoneNumber().toString();
//                            Paper.book().write("userPhone", phoneNumber);
                            userInfo.put("userPhone", phoneNumber); //임시로 데이터를 가지고 있을 헤시맵 버퍼.
                            userPhoneTextView.setText(phoneNumber);
                            buttonPhoneRegister.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                        }
                    });
                    alertDialog.dismiss();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
