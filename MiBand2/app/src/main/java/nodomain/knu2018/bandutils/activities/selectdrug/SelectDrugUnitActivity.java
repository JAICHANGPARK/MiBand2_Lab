package nodomain.knu2018.bandutils.activities.selectdrug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.Const.IntentConst;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.userprofile.HomeProfileActivity;
import nodomain.knu2018.bandutils.adapter.selectdrug.SelectDrugUnitAdapter;
import nodomain.knu2018.bandutils.events.selectdrug.SelectDrugEvent;
import nodomain.knu2018.bandutils.model.selectdrug.Drugs;

import static nodomain.knu2018.bandutils.Const.DataKeys.USER_ORIGIN_DRUG_INFO;

public class SelectDrugUnitActivity extends AppCompatActivity {

    private static final String TAG = "SelectDrugUnitActivity";

    @BindView(R.id.doneTextView)
    TextView doneTextView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    SelectDrugUnitAdapter adapter;
    ArrayList<String> myList;
    ArrayList<Drugs> drugArrayList;

    EventBus bus = EventBus.getDefault();

    String drugName;
    int values;
    int positions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drug_unit);

        setInitTitle();
        initSetting();
        initArrayList();

        String result = getIntent().getStringExtra(IntentConst.SELECT_DRUG_TYPE);
        if (result.length() == 0) {
            Toast.makeText(this, getResources().getString(R.string.select_drug_none), Toast.LENGTH_SHORT).show();
        } else {

            // TODO: 2018-06-23 이전 엑티비티에서 직렬화되어 온 데이터를 처리하는 부분 .
            // TODO: 2018-06-23 구분자는 ',' 로 분리되어 들어간다.
            Log.e(TAG, "onCreate: " + result);
            String[] drugNames = result.split(",");

            for (String drugName1 : drugNames) {
                myList.add(drugName1);
                drugArrayList.add(new Drugs(drugName1, "1"));
            }

        }

        setRecyclerView();

    }

    private void setInitTitle() {
        setTitle(getResources().getString(R.string.select_drug_unit));
    }

    private void initSetting() {
        Paper.init(this);
        ButterKnife.bind(this);
        bus.register(this);
    }

    private void initArrayList() {
        myList = new ArrayList<>();
        drugArrayList = new ArrayList<>();
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        adapter = new SelectDrugUnitAdapter(myList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    /**
     * 완료버튼 처리 리스터
     * 완료버튼을 누르게 되면 리스트를 Cache에 저장한다.
     * @author 박제창 (Dreamwalker)
     * Created on 2018-06-23
     */
    @OnClick(R.id.doneTextView)
    public void onDoneButtonClicked() {
        for (int i = 0; i < drugArrayList.size(); i++) {
            Log.e(TAG, "onCreate: " + drugArrayList.get(i).getDrugName() + drugArrayList.get(i).getValueUnit());
        }
        Paper.book().write(USER_ORIGIN_DRUG_INFO, drugArrayList);
        Intent intent = new Intent(SelectDrugUnitActivity.this, HomeProfileActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * EventBus 이벤트를 받아오는 부분.
     * Unit Value는 Adapter에서 받아 옴.
     * @param event
     * @author 박제창 (Dreamwalker)
     * Created on 2018-06-23
     */
    @Subscribe
    public void onEvent(SelectDrugEvent event) {
        String valueString;
        values = event.valueUnit;
        valueString = String.valueOf(values);
        drugName = event.drugName;
        drugArrayList.set(event.position, new Drugs(drugName, valueString));

        for (int k = 0; k < drugArrayList.size(); k++) {
            Log.e(TAG, "Unit onEvent: " + drugArrayList.get(k).getDrugName() + ", " + drugArrayList.get(k).getValueUnit());
        }
    }


    @Override
    protected void onDestroy() {
        // TODO: 2018-06-23 등록했던 이벤트 버스를 해제하는 과정.
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
