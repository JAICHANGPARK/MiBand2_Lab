package nodomain.knu2018.bandutils.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;
import nodomain.knu2018.bandutils.Const.IntentConst;
import nodomain.knu2018.bandutils.R;

public class AboutDeveloperActivity extends AppCompatActivity {

    @BindView(R.id.GithubButton)
    FButton githubButton;

    @BindView(R.id.qiitaButton)
    FButton qiitaButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        setTitle("Yes! Its Me Dreamwalker");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.GithubButton)
    void onGithubClicked(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(IntentConst.WEB_URL, "https://github.com/JAICHANGPARK");
        startActivity(intent);
    }

    @OnClick(R.id.qiitaButton)
    void onQittaClicked(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(IntentConst.WEB_URL, "https://qiita.com/Dreamwalker");
        startActivity(intent);
    }

}
