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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

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
import nodomain.knu2018.bandutils.remote.IPatternAPI;
import nodomain.knu2018.bandutils.remote.RetrofitClient;
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
public class WriteBloodActivity extends AppCompatActivity {

    private static final String TAG = "WriteBloodActivity";

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
     * The Value edit text.
     */
    @BindView(R.id.value_edit_text)
    EditText valueEditText;

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
     * The Input type.
     */
    String inputType;
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
     * The Db.
     */
    SQLiteDatabase db;
    /**
     * The M helper.
     */
    WriteBSDBHelper mHelper;
    /**
     * The Values.
     */
    ContentValues values;

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
        setContentView(R.layout.activity_write_blood);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.accent_custom));
        }

        ButterKnife.bind(this);
        Paper.init(this);


        WriteBSDBHelper mHelper = new WriteBSDBHelper(this);
        db = mHelper.getWritableDatabase();
        values = new ContentValues();

        initDateTimeTextView();
        initStartDTPicker();
        initSpinner();
        initRetrofit();

    }

    /**
     * Retrofit 객체 생성
     */
    private void initRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        service = retrofit.create(IPatternAPI.class);
        userName = Paper.book().read("userName");
        userUUID = Paper.book().read("userUUIDV2");

        progressDialog = new ProgressDialog(WriteBloodActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }

    private void initSpinner() {

        inputType = "공복";

        List<String> dataset = new LinkedList<>(Arrays.asList("공복", "취침전", "아침식사전", "아침식사후", "점심식사전",
                "점심식사후", "저녁식사전", "저녁식사후",
                "운동전", "운동후"));
        niceSpinner.attachDataSource(dataset);

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        inputType = "공복";
                        break;
                    case 1:
                        inputType = "취침전";
                        break;
                    case 2:
                        inputType = "아침식사전";
                        break;
                    case 3:
                        inputType = "아침식사후";
                        break;
                    case 4:
                        inputType = "점심식사전";
                        break;
                    case 5:
                        inputType = "점심식사후";
                        break;
                    case 6:
                        inputType = "저녁식사전";
                        break;
                    case 7:
                        inputType = "저녁식사후";
                        break;
                    case 8:
                        inputType = "운동전";
                        break;
                    case 9:
                        inputType = "운동후";
                        break;
                    default:
                        break;
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

        // TODO: 2018-05-13 Save Button 데이터베이스 저장 기능
        // TODO: 2018-05-13 입력값이 없는데 저장버튼이 눌렸을 경우의 예외처리
        if (valueEditText.getText().toString().length() == 0) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "혈당 수치를 입력하세요", Snackbar.LENGTH_SHORT).show();
        } else {
            
            String tmp = "입력하신 정보를 확인해주세요." + "\n\n"
                    + "채혈 시간 : " + startDate + " " + startTime + "\n\n"
                    + "채혈 시기 정보 : " + "\n" + inputType + "\n\n"
                    + "혈당 수치 : " + valueEditText.getText().toString() + " mg/dL";
            String valueEdt = valueEditText.getText().toString();

            Call<ResponseBody> request = service.writeBloodSugar(userName, startDate, startTime, inputType, valueEdt);

            values.put(WriteEntry.BloodSugarEntry.COLUNM_NAME_DATE, startDate);
            values.put(WriteEntry.BloodSugarEntry.COLUNM_NAME_TIME, startTime);
            values.put(WriteEntry.BloodSugarEntry.COLUNM_NAME_TYPE, inputType);
            values.put(WriteEntry.BloodSugarEntry.COLUNM_NAME_VALUE, valueEdt);


            new MaterialStyledDialog.Builder(this)
                    .setTitle("저장하시겠어요?")
                    .setIcon(R.drawable.ic_info_outline_black_24dp)
                    .setDescription(tmp)
                    .setPositiveText("저장")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Log.e("MaterialStyledDialogs", "Do something!");
                            // TODO: 2018-06-02 progressDialog가 꺼지지 않았던 문제를 수정함. - 박제창.
                            progressDialog.show();

                            long newRowId = db.insert(WriteEntry.BloodSugarEntry.TABLE_NAME, null, values);

                            request.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    progressDialog.dismiss();
                                    Toast.makeText(WriteBloodActivity.this, "데이터를 잘 저장했어요.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(WriteBloodActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    })
                    .setCancelable(true).show();

//            Log.e(TAG, "onClickDoneButton: " + inputType + "/ "
//                    + valueEditText.getText().toString());
        }

    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null){
            progressDialog = null;
        } else {
            super.onBackPressed();
        }
    }
}
