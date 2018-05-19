package nodomain.knu2018.gadgetbridge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.gadgetbridge.R;


public class AboutContactActivity extends AppCompatActivity {

    @BindView(R.id.buttonContact0)
    Button buttonContact0;
    @BindView(R.id.buttonContact1)
    Button buttonContact1;
    @BindView(R.id.buttonContact2)
    Button buttonContact2;
    @BindView(R.id.buttonContact3)
    Button buttonContact3;
    @BindView(R.id.buttonService0)
    Button buttonService0;
    @BindView(R.id.buttonService1)
    Button buttonService1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_contact);

        setTitle("문의 및 도움");
        setContentView(R.layout.activity_about_contact);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.buttonContact0)
    public void onButtonContact0licked(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] address = {"itsmejeffrey.dev@gmail.com"};    //이메일 주소 입력
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[개선사항건의]애플리케이션 개선사항 건의");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "개선사항을 건의합니다. 하단에 문제의 내용을 추가해주세요 ");
        startActivity(emailIntent);
    }

    @OnClick(R.id.buttonContact1)
    public void onButtonContact1licked(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] address = {"itsmejeffrey.dev@gmail.com"};    //이메일 주소 입력
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[이용불편건의]애플리케이션 이용관련 불편 건의");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "이용불편건의 합니다. 하단에 문제의 내용을 추가해주세요 ");
        startActivity(emailIntent);
    }

    @OnClick(R.id.buttonContact2)
    public void onButtonContact2licked(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] address = {"itsmejeffrey.dev@gmail.com"};    //이메일 주소 입력
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[버그 및 이상 신고]애플리케이션 관련 개선사항 건의");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "버그 및 이상 신고 건의합니다. 하단에 문제의 내용을 추가해주세요 ");
        startActivity(emailIntent);
    }

    @OnClick(R.id.buttonContact3)
    public void onButtonContact3licked(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] address = {"itsmejeffrey.dev@gmail.com"};    //이메일 주소 입력
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[기타문의]애플리케이션 관련 개선사항 건의");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "기타문의합니다.. 하단에 문제의 내용을 추가해주세요 ");
        startActivity(emailIntent);
    }
}
