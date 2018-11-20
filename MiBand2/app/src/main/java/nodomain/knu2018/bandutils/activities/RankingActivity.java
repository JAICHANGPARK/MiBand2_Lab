package nodomain.knu2018.bandutils.activities;

import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.remote.IAnalysisAPI;
import nodomain.knu2018.bandutils.adapter.RankingAdapter;
import nodomain.knu2018.bandutils.model.analysis.Ranks;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;

public class RankingActivity extends AppCompatActivity {

    private static final String TAG = "RankingActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<Ranks> totalRanks;
    ArrayList<Ranks.Rank> rankArrayList;
    RankingAdapter rankingAdapter;

    Retrofit retrofit;
    IAnalysisAPI service;
    NetworkInfo networkInfo;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("전체 순위");

        initSetting();
        initRetrofit();

        if (networkInfo != null && networkInfo.isConnected()){
            getRank();
        }else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"네트워크를 연결하세요", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void initSetting(){
        ButterKnife.bind(this);
        Paper.init(this);
        alertDialog =  new SpotsDialog.Builder().setContext(this).build();
        networkInfo = getNetworkInfo();
        totalRanks = new ArrayList<>();
        rankArrayList = new ArrayList<>();
        layoutManager  = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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

    private void getRank(){

        alertDialog.show();
        Call<Ranks> request = service.getRanking();
        request.enqueue(new Callback<Ranks>() {
            @Override
            public void onResponse(Call<Ranks> call, Response<Ranks> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()){
                    Ranks ranks = response.body();
                    try {
                        rankArrayList = ranks.getResponse();

                        for (int i = 0 ; i < rankArrayList.size(); i++){
                            Log.e(TAG, "onResponse: " + rankArrayList.get(i).getUserName() + rankArrayList.get(i).getRanking());
                        }
                        rankingAdapter = new RankingAdapter(getApplicationContext(), rankArrayList);
                        recyclerView.setAdapter(rankingAdapter);

                    }catch (NullPointerException e){
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<Ranks> call, Throwable t) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        if (alertDialog != null){
            alertDialog = null;
        }else {
            super.onBackPressed();
        }

    }
}
