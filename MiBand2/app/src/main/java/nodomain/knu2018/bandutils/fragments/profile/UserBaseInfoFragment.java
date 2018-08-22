package nodomain.knu2018.bandutils.fragments.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.remote.IUploadAPI;
import nodomain.knu2018.bandutils.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static nodomain.knu2018.bandutils.Const.DataKeys.USER_AGE;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_AGE_GROUP;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_BIRTH;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_GENDER;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_MAX_BLOOD_SUGAR;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_MIN_BLOOD_SUGAR;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_NAME;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_PHONE;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_UUID;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_UUID_V2;
import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;

/**
 * 제작 : 박제창
 * 작성일 : 2018-06-22
 */
public class UserBaseInfoFragment extends Fragment {

    private static final String TAG = "UserBaseInfoFragment";
    private static final int REQUEST_CODE = 1000;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.bs_min_textview)
    TextView bsMinTextView;
    @BindView(R.id.bs_max_textview)
    TextView bsMaxTextView;
    @BindView(R.id.gender_textview)
    TextView genderTextView;
    @BindView(R.id.age_textview)
    TextView ageTextView;
    @BindView(R.id.phone_textview)
    TextView phoneTextView;
    @BindView(R.id.birth_textview)
    TextView birthTextView;

    Map<String, String> userInfo = new HashMap<>();
    Retrofit retrofit;
    IUploadAPI service;

    NetworkInfo networkInfo;
    ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    public UserBaseInfoFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UserBaseInfoFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static UserBaseInfoFragment newInstance(String param1, String param2) {
//        UserBaseInfoFragment fragment = new UserBaseInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_base_info, container, false);
        ButterKnife.bind(this, view);
        Paper.init(getActivity().getApplicationContext());

        initRetrofit();
        initProgressDialog();
        initNetworkSetting();
        initServerFetch();

        return view;
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    private void initNetworkSetting(){
        networkInfo = getNetworkInfo();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IUploadAPI.class);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("처리중...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    /**
     * 서버에 등록되어있는 사용자 값을 가져온다.
     *
     * @author : 박제창 (dreamwalker)
     */
    private void initServerFetch() {

        userInfo.put(USER_NAME, Paper.book().read(USER_NAME));
        userInfo.put(USER_UUID, Paper.book().read(USER_UUID_V2));

        String userUUID = userInfo.get(USER_UUID);

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
                        String userUUID = tmpInfo.getResponse().get(0).getUserUUID();
                        String userGender = tmpInfo.getResponse().get(0).getGender();
                        String userPhone = tmpInfo.getResponse().get(0).getPhone();
                        String userAge = tmpInfo.getResponse().get(0).getAge();
                        String userAgeGroup = tmpInfo.getResponse().get(0).getAgeGroup();
                        String userBirth = tmpInfo.getResponse().get(0).getBirthday();
                        String userBsMin = tmpInfo.getResponse().get(0).getBsMin();
                        String userBsMax = tmpInfo.getResponse().get(0).getBsMax();

                        // TODO: 2018-06-24 사용자 성별 값이 null일 때 예외 처리.
                        if (userGender != null) {
                            // TODO: 2018-06-24 널이 아니지만 길이가 0일 때
                            if (userGender.length() == 0) {
                                String massage = "등록된 정보가 없습니다.";
                                genderTextView.setText(massage);
                            } else {
                                genderTextView.setText(userGender);
                                Paper.book().write(USER_GENDER, userGender);
                                userInfo.put(USER_GENDER, userGender);
                            }
                        } else {
                            String massage = "등록된 정보가 없습니다.";
                            genderTextView.setText(massage);
                        }

                        // TODO: 2018-06-24 사용자 나이가 null일때 예외 처리.
                        if (userAge != null && userAgeGroup != null) {
                            if (userAge.length() == 0) {
                                String massage = "등록된 정보가 없습니다.";
                                ageTextView.setText(massage);
                            } else {
                                Paper.book().write(USER_AGE, userAge);
                                Paper.book().write(USER_AGE_GROUP, userAgeGroup);
                                String ageString = userAge + " (" + userAgeGroup + "대)";
                                ageTextView.setText(ageString);
                                userInfo.put(USER_AGE, userAge);
                                userInfo.put(USER_AGE_GROUP, userAgeGroup);
                            }
                        } else {
                            String massage = "등록된 정보가 없습니다.";
                            ageTextView.setText(massage);
                        }

                        // TODO: 2018-06-04 목표 혈당 값 처리
                        if (userBsMin != null && userBsMax != null) {
                            if (userBsMin.length() == 0 && userBsMax.length() == 0) {
                                String massage = "등록된 정보가 없습니다.";
                                bsMinTextView.setText(massage);
                                bsMaxTextView.setText(massage);
                            } else {
                                Paper.book().write(USER_MIN_BLOOD_SUGAR, userBsMin);
                                Paper.book().write(USER_MAX_BLOOD_SUGAR, userBsMax);
                                String minBs = userBsMin + " mg/dL";
                                String maxBs = userBsMax + " mg/dL";
                                bsMinTextView.setText(minBs);
                                bsMaxTextView.setText(maxBs);
                                userInfo.put(USER_MIN_BLOOD_SUGAR, userBsMin);
                                userInfo.put(USER_MAX_BLOOD_SUGAR, userBsMax);
                            }
                        } else {
                            String massage = "등록된 정보가 없습니다.";
                            bsMinTextView.setText(massage);
                            bsMaxTextView.setText(massage);
                        }

                        // TODO: 2018-06-24 생일 정보 예외처리
                        if (userBirth != null) {
                            if (userBirth.length() == 0) {
                                String massage = "등록된 정보가 없습니다.";
                                birthTextView.setText(massage);
                            } else {
                                birthTextView.setText(userBirth);
                                Paper.book().write(USER_BIRTH, userBirth);
                                userInfo.put(USER_BIRTH, userBirth);
                            }
                        } else {
                            String massage = "등록된 정보가 없습니다.";
                            birthTextView.setText(massage);
                        }

                        // TODO: 2018-06-24 핸드폰 번호
                        if (userPhone != null) {
                            if (userPhone.length() == 0) {
                                String massage = "등록된 정보가 없습니다.";
                                phoneTextView.setText(massage);
                            } else {
                                phoneTextView.setText(userPhone);
                                Paper.book().write(USER_PHONE, userPhone);
                                userInfo.put(USER_PHONE, userPhone);
                                //buttonPhoneRegister.setVisibility(View.GONE);
                            }
                        } else {
                            String massage = "등록된 정보가 없습니다.";
                            phoneTextView.setText(massage);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        
        // TODO: 2018-05-30 인터넷 연결이 없다면 캐시 데이터를 가져오는 코드를 처리해야합니다. - 박제창 (Dreamwalker)
        else {
            
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(),
                    getActivity().getResources().getString(R.string.profile_network_exception_message),
                    Snackbar.LENGTH_SHORT).show();

            // TODO: 2018-06-24 만약 성별 정보가 캐쉬에 있는지 없는지에 대한 예외처리합니다. - 박제창 (Dreamwalker)
            if (Paper.book().read(USER_GENDER) != null && Paper.book().read(USER_GENDER).toString().length() != 0) {
                // TODO: 2018-05-30 데이터가 있다면.
                String userGender = Paper.book().read(USER_GENDER);
                genderTextView.setText(userGender);
            } else {
                String massage = "등록된 정보가 없습니다.";
                genderTextView.setText(massage);
            }


            // TODO: 2018-06-04 네트워크 연결이 없을때 목표 혈당값 처리 - 박제창 (Dreamwalker)
            if (Paper.book().read(USER_MIN_BLOOD_SUGAR) != null && Paper.book().read(USER_MAX_BLOOD_SUGAR) != null) {
                // TODO: 2018-05-30 데이터가 있다면.  - 박제창 (Dreamwalker)
                String userMin = Paper.book().read(USER_MIN_BLOOD_SUGAR);
                String userMax = Paper.book().read(USER_MAX_BLOOD_SUGAR);

                String minBs = userMin + " mg/dL";
                String maxBs = userMax + " mg/dL";
                bsMinTextView.setText(minBs);
                bsMaxTextView.setText(maxBs);
                //String message = userMin + " - " + userMax + " mg/dL";

            } else {
                String massage = "등록된 정보가 없습니다.";
                bsMinTextView.setText(massage);
                bsMaxTextView.setText(massage);
            }

            // TODO: 2018-06-04  네트워크 연결이 없을때 목표 혈당값 처리 사용자 나이 처리
            if (Paper.book().read(USER_AGE) != null && Paper.book().read(USER_AGE_GROUP) != null) {
                // TODO: 2018-05-30 데이터가 있다면.
                String userAge = Paper.book().read(USER_AGE);
                String userAgeGroup = Paper.book().read(USER_AGE_GROUP);
                String message = userAge + " (" + userAgeGroup + "대)";
                ageTextView.setText(message);
            } else {
                String massage = "등록된 정보가 없습니다.";
                ageTextView.setText(massage);
            }

            // TODO: 2018-06-24 생년월일 정보를 가져옵니다. -박제창 (Dreamwalker)
            if (Paper.book().read(USER_BIRTH) != null) {
                // TODO: 2018-05-30 데이터가 있다면. -박제창 (Dreamwalker)
                String userBirth = Paper.book().read(USER_BIRTH);
                birthTextView.setText(userBirth);
            } else {
                String massage = "등록된 정보가 없습니다.";
                birthTextView.setText(massage);
            }

            if (Paper.book().read(USER_PHONE) != null) {
                // TODO: 2018-05-30 데이터가 있다면.
                String userPhone = Paper.book().read(USER_PHONE);
                phoneTextView.setText(userPhone);
                //buttonPhoneRegister.setVisibility(View.GONE);
            } else {
                String massage = "등록된 정보가 없습니다.";
                phoneTextView.setText(massage);
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
