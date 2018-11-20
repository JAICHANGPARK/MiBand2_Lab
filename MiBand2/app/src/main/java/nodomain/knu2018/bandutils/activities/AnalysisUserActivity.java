package nodomain.knu2018.bandutils.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.remote.IAnalysisAPI;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.model.analysis.Ranks;
import nodomain.knu2018.bandutils.model.analysis.WriteCount;
import nodomain.knu2018.bandutils.util.AnimTextView.AnimaionTextViewV1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;

public class AnalysisUserActivity extends AppCompatActivity {
    private static final String TAG = "AnalysisUserActivity";


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fadingTextView)
    FadingTextView fadingTextView;

    @BindView(R.id.testTextView)
    AnimaionTextViewV1 animaionTextViewV1; // 철자 틀렸네 ..


    @BindView(R.id.my_write_count_text_view)
    AnimaionTextViewV1 myTotalCountTextView;

    @BindView(R.id.my_count_button)
    ImageView totalCountButton;

    @BindView(R.id.my_rank_button)
    ImageView totalRankButton;

    String userName;

    IAnalysisAPI service;
    Retrofit retrofit;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_user);

        initSetting();
        initRetrofit();

        if (networkInfo != null && networkInfo.isConnected()) {
            getBannerCount();
            getMyRank();
            getMyCount();
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "네트워크를 연결해주세요.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initSetting() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        ButterKnife.bind(this);
        Paper.init(this);

        setSupportActionBar(toolbar);
        networkInfo = getNetworkInfo();
        alertDialog = new SpotsDialog.Builder().setContext(this).build();

        userName = Paper.book().read("userName");

        progressDialog = new ProgressDialog(AnalysisUserActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("처리중...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);


        String[] texts = {"당뇨그루와 함께하는 정보를 알려드릴게요."};
        fadingTextView.setTexts(texts); //You can use an array resource or a string array as the parameter
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IAnalysisAPI.class);
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

    private void getBannerCount() {

        alertDialog.show();
        Call<WriteCount> request = service.getWriteCount();
        request.enqueue(new Callback<WriteCount>() {
            @Override
            public void onResponse(Call<WriteCount> call, Response<WriteCount> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {
                    WriteCount writeCount = response.body();

                    for (int i = 0; i < writeCount.getResponse().size(); i++) {
                        Log.e(TAG, "onResponse: " + writeCount.getResponse().get(i) + ", " + i);
                    }

                    String start = "당뇨그루와 함께하는 정보를 알려드릴게요.";
                    String total = "보유 총 기록 수 : " + writeCount.getResponse().get(0).getTotal() + " 회";
                    String today = "오늘 기록 수 : " + writeCount.getResponse().get(0).getToday() + " 회";
                    String totalBs = "혈당 총 기록 수 : " + writeCount.getResponse().get(0).getTotalBs() + " 회";
                    String totalFit = "운동 총 기록 수 : " + writeCount.getResponse().get(0).getTotalFit() + " 회";
                    String totalDrug = "투약 총 기록 수 : " + writeCount.getResponse().get(0).getTotalDrug() + " 회";
                    String totalMeal = "식사 총 기록 수 : " + writeCount.getResponse().get(0).getTotalMeal() + " 회";
                    String totalSleep = "수면 총 기록 수 : " + writeCount.getResponse().get(0).getTotalSleep() + " 회";
                    String[] texts = {start, total, today, totalBs, totalFit, totalDrug, totalMeal, totalSleep};
                    fadingTextView.setTexts(texts); //You can use an array resource or a string array as the parameter

                    Paper.book("count").write("total", total);
                    Paper.book("count").write("today", today);
                    Paper.book("count").write("totalBs", totalBs);
                    Paper.book("count").write("totalFit", totalFit);
                    Paper.book("count").write("totalDrug", totalDrug);
                    Paper.book("count").write("totalMeal", totalMeal);
                    Paper.book("count").write("totalSleep", totalSleep);

                }
            }

            @Override
            public void onFailure(Call<WriteCount> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(AnalysisUserActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMyRank() {
        alertDialog.show();

        Call<Ranks> request = service.getMyRanking(userName);
        request.enqueue(new Callback<Ranks>() {
            @Override
            public void onResponse(Call<Ranks> call, Response<Ranks> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        Ranks ranks = response.body();
                        String name = ranks.getResponse().get(0).getUserName();
                        String score = ranks.getResponse().get(0).getScore();
                        String ranking = ranks.getResponse().get(0).getRanking();
                        animaionTextViewV1.setText("100", ranking);

                    } catch (IndexOutOfBoundsException e) {
                        animaionTextViewV1.setText("100", "0");
                        Toast.makeText(AnalysisUserActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Ranks> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(AnalysisUserActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMyCount() {
        alertDialog.show();
        WriteBSDBHelper mHelper = new WriteBSDBHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ArrayList<String> info = mHelper.dbTotalCount();
        myTotalCountTextView.setText("9999", info.get(0));
        alertDialog.dismiss();
    }

    @OnClick(R.id.my_rank_button)
    public void onClickRankButton(){
        startActivity(new Intent(AnalysisUserActivity.this, RankingActivity.class));

    }

    @OnClick(R.id.my_count_button)
    public void onClickCountButton(){
        startActivity(new Intent(AnalysisUserActivity.this, WriteCountActivity.class));
    }

}
