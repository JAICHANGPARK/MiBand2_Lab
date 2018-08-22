package nodomain.knu2018.bandutils.activities.selectdrug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.bandutils.Const.IntentConst;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.events.selectdrug.DrugDataEvent;
import nodomain.knu2018.bandutils.fragments.selectdrug.SelectDrugFragment;

public class SelectDrugActivity extends AppCompatActivity {

    private static final String TAG = "SelectDrugActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.doneTextView)
    TextView doneTextView;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private ArrayList<String> rrapid, rapid, neutral, longtime, mixed;

    SectionsPagerAdapter mSectionsPagerAdapter;
    EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("약물 선택");
        setContentView(R.layout.activity_select_drug);
        setSupportActionBar(toolbar);

        initSetting();
        setAllArrayList();
        setViewPager();

    }

    @OnClick(R.id.doneTextView)
    public void onDoneTextViewClicked() {
        Intent intent = new Intent(SelectDrugActivity.this, SelectDrugUnitActivity.class);
        StringBuilder stringBuilder = new StringBuilder();

        // TODO: 2018-02-03 내가 짜도 참 괜찮은 코드
        // TODO: 2018-06-22 unknown이 포함되면 제거하고 unknown이 포함된 약물 이름을 가져온다.

        for (int i = 0; i < rrapid.size(); i++) {
            if (!rrapid.get(i).contains("unknown")) {
                stringBuilder.append(rrapid.get(i));
                stringBuilder.append(",");
            }
        }
        for (int i = 0; i < rapid.size(); i++) {
            if (!rapid.get(i).contains("unknown")) {
                stringBuilder.append(rapid.get(i));
                stringBuilder.append(",");
            }
        }
        for (int i = 0; i < neutral.size(); i++) {
            if (!neutral.get(i).contains("unknown")) {
                stringBuilder.append(neutral.get(i));
                stringBuilder.append(",");
            }
        }
        for (int i = 0; i < longtime.size(); i++) {
            if (!longtime.get(i).contains("unknown")) {
                stringBuilder.append(longtime.get(i));
                stringBuilder.append(",");
            }
        }
        for (int i = 0; i < mixed.size(); i++) {
            if (!mixed.get(i).contains("unknown")) {
                stringBuilder.append(mixed.get(i));
                stringBuilder.append(",");
            }
        }

        Log.e(TAG, "onDoneTextViewClicked: result" + stringBuilder.toString());

        // TODO: 2018-06-23 약물을 선택합니다.
        if (stringBuilder.toString().length() == 0) {

            AlertDialog alertDialog = alertDialogCreate(this,
                    getResources().getString(R.string.select_drug_dialog_title),
                    getResources().getString(R.string.select_drug_dialog_message),
                    getResources().getString(R.string.select_drug_dialog_positive));

            alertDialog.show();

            Log.e(TAG, "onDoneTextViewClicked: result" + "선택된 약물 없음");
        } else {
            intent.putExtra(IntentConst.SELECT_DRUG_TYPE, stringBuilder.toString());
            startActivity(intent);
            finish();
        }
    }

    /**
     * 약물 선택이 없을 시 알람 메시지를 표시해주기 위한 빌더 생성 메소드 입니다.
     * @param context
     * @param title
     * @param message
     * @param positiveText
     * @return AlertDialog Builder
     *
     * @author 박제창 (Dreamwalker)
     * Created on 2018-06-23
     */
    private AlertDialog alertDialogCreate(Context context, String title, String message, String positiveText) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, null);
        return builder.create();
    }

    /**
     * 뷰 바인딩과 이벤트버스 등록 메소드
     * Created on 2018-06-23
     */
    private void initSetting() {

        ButterKnife.bind(this);
        bus.register(this);
    }

    /**
     * ArrayList 초기화 및 객체 생성 메소드
     * <p>
     * 혼합형을 제외한 리스트는 최대 5개의 약물을 보유하고 있기 때문에 5개를 생성하고
     * unknown으로 초기화한다.
     * 혼합형은 종류가 많기 때문에 10개의 리스트를 생성하고 unknown으로 초기화한다.
     *
     * @author : 박제창 (Dreamwalker)
     * Created on 2018-06-23
     */
    private void setAllArrayList() {
        rrapid = new ArrayList<>();
        rapid = new ArrayList<>();
        longtime = new ArrayList<>();
        neutral = new ArrayList<>();
        mixed = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            rrapid.add(i, "unknown");
            rapid.add(i, "unknown");
            longtime.add(i, "unknown");
            neutral.add(i, "unknown");
        }
        for (int i = 0; i < 10; i++) {
            mixed.add(i, "unknown");
        }
    }

    /**
     * 뷰페이저를 설정한다.
     *
     * @author 박제창(Dreamwalker)
     * Created on 2018-06-23
     */
    private void setViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * SelectDrugFragment로 부터 들어오는 이벤트 데이터를 받아 처리합니다.
     * @param event
     * @author 박제창 (Dreamwalker)
     * Created on 2018-06-23
     */

    @Subscribe
    public void onEvent(DrugDataEvent event) {
        int index = event.position;
        int pageNum = event.pageNumber;

        if (pageNum == 1) rrapid = event.drugRrapidList;
        if (pageNum == 2) rapid = event.drugRapidList;
        if (pageNum == 3) neutral = event.drugNeutralList;
        if (pageNum == 4) longtime = event.drugLongtimeList;
        if (pageNum == 5) mixed = event.drugMixedList;

        for (int i = 0; i < rrapid.size(); i++) {
            Log.e(TAG, "onEvent: rrapid : index " + i + " , " + "value : " + rrapid.get(i));
        }
        for (int i = 0; i < rapid.size(); i++) {
            Log.e(TAG, "onEvent: rapid : index " + i + " , " + "value : " + rapid.get(i));
        }
        for (int i = 0; i < neutral.size(); i++) {
            Log.e(TAG, "onEvent: neutral : index " + i + " , " + "value : " + neutral.get(i));
        }
        for (int i = 0; i < longtime.size(); i++) {
            Log.e(TAG, "onEvent: DRUG_TYPE : index " + i + " , " + "value : " + longtime.get(i));
        }
    }

    @Override
    protected void onStop() {
        // TODO: 2018-06-23 Event Bus 등록 해제 - 박제창
        bus.unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO: 2018-06-23 Event Bus 등록 해제 - 박제창
        bus.unregister(this);
        super.onDestroy();
    }


    /**
     * Fragment FragmentPagerAdapter 클래스
     * @author 박제창 (Dreamwalker)
     * Created on 2018-06-23
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int PAGE_NUMBER_SIZE = 5;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // TODO: 2018-06-22 초속효성
                    return SelectDrugFragment.newInstance("1");
                case 1:
                    // TODO: 2018-06-22 속효성
                    return SelectDrugFragment.newInstance("2");
                case 2:
                    // TODO: 2018-06-22 중간형
                    return SelectDrugFragment.newInstance("3");
                case 3:
                    // TODO: 2018-06-22 지속형
                    return SelectDrugFragment.newInstance("4");
                case 4:
                    // TODO: 2018-06-22 혼합형
                    return SelectDrugFragment.newInstance("5");
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_NUMBER_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.rapid).toUpperCase(l);
                case 1:
                    return getString(R.string.rapid2).toUpperCase(l);
                case 2:
                    return getString(R.string.netural).toUpperCase(l);
                case 3:
                    return getString(R.string.longtime).toUpperCase(l);
                case 4:
                    return getString(R.string.mixed).toUpperCase(l);
            }
            return null;
        }
    }
}
