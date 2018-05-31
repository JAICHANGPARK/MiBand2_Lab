package nodomain.knu2018.bandutils.activities.writing;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.Remote.IPatternAPI;
import nodomain.knu2018.bandutils.Remote.RetrofitClient;
import nodomain.knu2018.bandutils.database.WriteBSDBHelper;
import nodomain.knu2018.bandutils.database.WriteEntry;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static nodomain.knu2018.bandutils.Const.URLConst.BASE_URL;


/**
 * ____  ____  _________    __  ____       _____    __    __ __ __________
 * / __ \/ __ \/ ____/   |  /  |/  / |     / /   |  / /   / //_// ____/ __ \
 * / / / / /_/ / __/ / /| | / /|_/ /| | /| / / /| | / /   / ,<  / __/ / /_/ /
 * / /_/ / _, _/ /___/ ___ |/ /  / / | |/ |/ / ___ |/ /___/ /| |/ /___/ _, _/
 * /_____/_/ |_/_____/_/  |_/_/  /_/  |__/|__/_/  |_/_____/_/ |_/_____/_/ |_|
 * <p>
 * <p>
 * Created by Dreamwalker on 2018-05-28.
 */
public class WriteDrugActivity extends AppCompatActivity {

    private static final String TAG = "WriteDrugActivity";

    /**
     * The Close button.
     */
    @BindView(R.id.closeButton)
    ImageView closeButton;
    /**
     * The Done button.
     */
    @BindView(R.id.doneButton)
    ImageView doneButton;

    /**
     * The Start time text view.
     */
    @BindView(R.id.startTimeText)
    TextView startTimeTextView;
    /**
     * The Start date text view.
     */
    @BindView(R.id.startDateText)
    TextView startDateTextView;
    /**
     * The Start time button.
     */
    @BindView(R.id.startTimeButton)
    ImageView startTimeButton;
    /**
     * The Nice spinner.
     */
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;
    /**
     * The Nice spinner 2.
     */
    @BindView(R.id.nice_spinner2)
    NiceSpinner niceSpinner2;

    /**
     * The Unit edit text.
     */
    @BindView(R.id.unit_edit_text)
    MaterialEditText unitEditText;

    /**
     * The Start date time dialog fragment.
     */
    SwitchDateTimeDialogFragment startDateTimeDialogFragment;

    /**
     * The Simple date format.
     */
    SimpleDateFormat simpleDateFormat;
    /**
     * The Simple time format.
     */
    SimpleDateFormat simpleTimeFormat;
    /**
     * The Now.
     */
    Calendar now;

    /**
     * The Init date.
     */
    String initDate, /**
     * The Init time.
     */
    initTime;
    /**
     * The Input value.
     */
    String inputValue;

    /**
     * The Start date.
     */
    String startDate;
    /**
     * The Start time.
     */
    String startTime;


    /**
     * The Str date.
     */
    String strDate;
    /**
     * The Date value.
     */
    String dateValue;
    /**
     * The Time value.
     */
    String timeValue;
    /**
     * The Input type.
     */
    String inputType;
    /**
     * The Input type 2.
     */
    String inputType2;
    /**
     * The Drug unit.
     */
    String drugUnit;


    /**
     * The M db helper.
     */
    WriteBSDBHelper mDBHelper;
    /**
     * The Db.
     */
    SQLiteDatabase db;
    /**
     * The Content values.
     */
    ContentValues contentValues;

    /**
     * The Retrofit.
     */
    Retrofit retrofit;
    /**
     * The Service.
     */
    IPatternAPI service;
    private IPatternAPI getAPIUpload() {
        return RetrofitClient.getClient(BASE_URL).create(IPatternAPI.class);
    }

    /**
     * The UserInfo name.
     */
    String userName;
    /**
     * The UserInfo uuid.
     */
    String userUUID;
    /**
     * The Progress dialog.
     */
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_drug);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }

        ButterKnife.bind(this);
        Paper.init(this);


        inputType = "초속효성";
        inputType2 = "노보래피드";
        drugUnit = "0";

        initDataBases();
        initSpinner();
        initDateTimeTextView();
        initStartDTPicker();
        initRetrofit();

    }

    /**
     * Retrofit 객체 생성
     * @author : 박제창(Dreamwalker)
     */
    private void initRetrofit(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        service = retrofit.create(IPatternAPI.class);
        userName = Paper.book().read("userName");
        userUUID  = Paper.book().read("userUUIDV2");

        progressDialog = new ProgressDialog(WriteDrugActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    private void initDataBases() {
        mDBHelper = new WriteBSDBHelper(this);
        db = mDBHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }


    private void initSpinner() {
        List<String> dataset = new LinkedList<>(Arrays.asList("초속효성", "속효성", "중간형", "지속형", "혼합형"));
        niceSpinner.attachDataSource(dataset);
        List<String> dataset2 = new LinkedList<>(Arrays.asList("노보래피드", "애피드라", "휴마로그"));
        niceSpinner2.attachDataSource(dataset2);


        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        inputType = "초속효성";
                        List<String> set = new LinkedList<>(Arrays.asList("노보래피드", "애피드라", "휴마로그"));
                        niceSpinner2.attachDataSource(set);
                        inputType2 = "노보래피드";
                        break;
                    case 1:
                        inputType = "속효성";
                        List<String> set1 = new LinkedList<>(Arrays.asList("휴물린", "노보린R", "노보렛"));
                        niceSpinner2.attachDataSource(set1);
                        inputType2 = "휴물린";
                        break;
                    case 2:
                        inputType = "중간형";
                        List<String> set2 = new LinkedList<>(Arrays.asList("인슈라타드", "휴물린N", "노보린N", "노보렛N"));
                        niceSpinner2.attachDataSource(set2);
                        inputType2 = "인슈라타드";
                        break;
                    case 3:
                        inputType = "지속형";
                        List<String> set3 = new LinkedList<>(Arrays.asList("란투스", "레버미어", "투제오", "트레시바"));
                        niceSpinner2.attachDataSource(set3);
                        inputType2 = "란투스";

                        break;
                    case 4:
                        inputType = "혼합형";
                        List<String> set4 = new LinkedList<>(Arrays.asList("노보믹스30", "노보믹스50", "노보믹스70", "믹스타드30",
                                "휴마로그믹스25", "휴마로그믹스50", "휴물린70/30"));
                        niceSpinner2.attachDataSource(set4);
                        inputType2 = "노보믹스30";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        niceSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (inputType.equals("초속효성")) {

                    switch (position) {
                        case 0:
                            inputType2 = "노보래피드";
                            break;
                        case 1:
                            inputType2 = "애피드라";
                            break;
                        case 2:
                            inputType2 = "휴마로그";
                            break;
                    }
                } else if (inputType.equals("속효성")) {

                    switch (position) {
                        case 0:
                            inputType2 = "휴물린";
                            break;
                        case 1:
                            inputType2 = "노보린R";
                            break;
                        case 2:
                            inputType2 = "노보렛";
                            break;
                    }
                } else if (inputType.equals("중간형")) {

                    switch (position) {
                        case 0:
                            inputType2 = "인슈라타드";
                            break;
                        case 1:
                            inputType2 = "휴물린N";
                            break;
                        case 2:
                            inputType2 = "노보린N";
                            break;
                        case 3:
                            inputType2 = "노보렛N";
                            break;
                    }
                } else if (inputType.equals("지속형")) {

                    switch (position) {
                        case 0:
                            inputType2 = "란투스";
                            break;
                        case 1:
                            inputType2 = "레버미어";
                            break;
                        case 2:
                            inputType2 = "투제오";
                            break;
                        case 3:
                            inputType2 = "트레시바";
                            break;
                    }
                } else if (inputType.equals("혼합형")) {

                    switch (position) {
                        case 0:
                            inputType2 = "노보믹스30";
                            break;
                        case 1:
                            inputType2 = "노보믹스50";
                            break;
                        case 2:
                            inputType2 = "노보믹스70";
                            break;
                        case 3:
                            inputType2 = "믹스타드30";
                            break;
                        case 4:
                            inputType2 = "휴마로그믹스25";
                            break;
                        case 5:
                            inputType2 = "휴마로그믹스50";
                            break;
                        case 6:
                            inputType2 = "휴물린70/30";
//                            bus.post(new TWDataEvent(dateValue, timeValue, inputType, inputType2, drugUnit, "null", PAGE_NUM));
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initDateTimeTextView() {
        now = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        simpleTimeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
        initDate = simpleDateFormat.format(now.getTime());
        initTime = simpleTimeFormat.format(now.getTime());
        startDate = initDate;
        startTime = initTime;
        startDateTextView.setText(initDate);
        startTimeTextView.setText(initTime);
    }

    private void initStartDTPicker() {

        startDateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "채혈 시간 선택",
                "OK",
                "Cancel"
        );
        startDateTimeDialogFragment.startAtCalendarView();
        startDateTimeDialogFragment.set24HoursMode(true);

        startDateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        startDateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        startDateTimeDialogFragment.setDefaultDateTime(now.getTime());
        try {
            startDateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        startDateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                Log.e(TAG, "onPositiveButtonClick: " + date.toString());
                startDate = simpleDateFormat.format(date.getTime());
                startTime = simpleTimeFormat.format(date.getTime());
                startDateTextView.setText(startDate);
                startTimeTextView.setText(startTime);
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });
    }

    /**
     * On start time button listener.
     */
    @OnClick(R.id.startTimeButton)
    public void onStartTimeButtonListener() {
        startDateTimeDialogFragment.show(getSupportFragmentManager(), "startDateTimePicker");
    }

    /**
     * On click close button.
     */
    @OnClick(R.id.closeButton)
    public void onClickCloseButton() {
        finish();
    }


    /**
     * On click done button.
     */
    @OnClick(R.id.doneButton)
    public void onClickDoneButton() {


        // TODO: 2018-05-13 Save Button 데이터베이스 저장 기능
        // TODO: 2018-05-13 입력값이 없는데 저장버튼이 눌렸을 경우의 예외처리
        if (unitEditText.getText().toString().length() == 0) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "투약단위를 입력하세요", Snackbar.LENGTH_SHORT).show();
        } else {

            progressDialog.show();

            String tmp = "입력하신 정보를 확인해주세요." + "\n\n"
                    + "투약 시간 : " + startDate + " " + startTime + "\n\n"
                    + "인슐린 정보 : " + "\n" + inputType + "-" + inputType2 + "\n\n"
                    + "투약 단위 : " + unitEditText.getText().toString() + " 단위";

            String tmpValue = unitEditText.getText().toString();

            Call<ResponseBody> request = service.writeDrug(userName, startDate,startTime,inputType, inputType2, tmpValue);

            new MaterialStyledDialog.Builder(this)
                    .setTitle("저장하시겠어요?")
                    .setIcon(R.drawable.ic_info_outline_black_24dp)
                    .setDescription(tmp)
                    .setPositiveText("저장")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Log.e("MaterialStyledDialogs", "Do something!");

                            contentValues.put(WriteEntry.DrugEntry.COLUNM_NAME_DATE, startDate);
                            contentValues.put(WriteEntry.DrugEntry.COLUNM_NAME_TIME, startTime);
                            contentValues.put(WriteEntry.DrugEntry.COLUNM_NAME_TYPE_TOP, inputType);
                            contentValues.put(WriteEntry.DrugEntry.COLUNM_NAME_TYPE_BOTTOM, inputType2);
                            contentValues.put(WriteEntry.DrugEntry.COLUNM_NAME_VALUE, tmpValue);

                            long insertDB = db.insert(WriteEntry.DrugEntry.TABLE_NAME, null, contentValues);

                            request.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(WriteDrugActivity.this, "소중한 데이터를 잘 저장했어요. Code : " + insertDB, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(WriteDrugActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .setCancelable(true).show();

            Log.e(TAG, "onClickDoneButton: " + inputType + "/ "
                    + inputType2 + "/"
                    + unitEditText.getText().toString());
        }

    }


}
