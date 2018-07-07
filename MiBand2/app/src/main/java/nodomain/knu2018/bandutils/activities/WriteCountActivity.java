package nodomain.knu2018.bandutils.activities;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.Remote.IAnalysisAPI;
import nodomain.knu2018.bandutils.adapter.MyWriteCountAdapter;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.model.analysis.MyWriteCount;
import nodomain.knu2018.bandutils.model.analysis.WriteCountDays;
import nodomain.knu2018.bandutils.util.Charts.MyXAxisValueFormatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;

public class WriteCountActivity extends AppCompatActivity {
    private static final String TAG = "WriteCountActivity";

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;

    Retrofit retrofit;
    IAnalysisAPI service;
    NetworkInfo networkInfo;
    WriteBSDBHelper writeBSDBHelper;
    
    ProgressDialog progressDialog;

    MyWriteCountAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_count);
        initSetting();
        initRetrofit();
        if (networkInfo != null && networkInfo.isConnected()){
            getMyCountPerDay();
            getMyCount();
        }else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "네트워크를 연걸해주세요", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initSetting(){
        ButterKnife.bind(this);
        Paper.init(this);
        networkInfo = getNetworkInfo();
        writeBSDBHelper = new WriteBSDBHelper(this);

        progressDialog = new ProgressDialog(WriteCountActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("동기화 중..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        layoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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

    private void getMyCountPerDay(){
        progressDialog.show();
        String userName = Paper.book().read("userName");
        Call<WriteCountDays> request = service.getMyCountOfDay(userName);
        // TODO: 2018-06-05 요청하기 
        request.enqueue(new Callback<WriteCountDays>() {
            @Override
            public void onResponse(Call<WriteCountDays> call, Response<WriteCountDays> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    WriteCountDays tmp = response.body();
                    ArrayList<WriteCountDays.CountDay> list = tmp.getResponse();
                    List<Entry> entries = new ArrayList<>();
                    String[] xLabel = new String[100];
                    for (int i = 0 ; i < list.size(); i++){
                        xLabel[i] = list.get(i).getRgstDate();
                        float tmpValue = Float.valueOf(list.get(i).getCount());
                        entries.add(new Entry(i,tmpValue));
                    }
                    
                    LineDataSet dataSet = new LineDataSet(entries, "기록수");
                    dataSet.setDrawFilled(true);
                    dataSet.setCubicIntensity(0.3f);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    LineData lineData = new LineData(dataSet);

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setValueFormatter(new MyXAxisValueFormatter(xLabel));
                    xAxis.setGranularity(1.0f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    YAxis yAxis = lineChart.getAxisRight();
                    yAxis.setEnabled(false);

                    lineChart.animateX(1000);
                    lineChart.setData(lineData);
                    lineChart.invalidate();
                    
                }
            }

            @Override
            public void onFailure(Call<WriteCountDays> call, Throwable t) {
                Toast.makeText(WriteCountActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
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

    private void getMyCount(){
        ArrayList<MyWriteCount> fetch = writeBSDBHelper.getCount();
        adapter = new MyWriteCountAdapter(this, fetch);
        recyclerView.setAdapter(adapter);

        for (MyWriteCount s : fetch){
            Log.e(TAG, "getMyCount: " + s);
        }


    }

}
