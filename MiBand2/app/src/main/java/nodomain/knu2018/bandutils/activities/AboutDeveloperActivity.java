package nodomain.knu2018.bandutils.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;
import nodomain.knu2018.bandutils.Const.IntentConst;
import nodomain.knu2018.bandutils.R;

/**
 * The type About developer activity.
 */
public class AboutDeveloperActivity extends AppCompatActivity {
    private static final String TAG = "AboutDeveloperActivity";

    /**
     * The Github button.
     */
    @BindView(R.id.GithubButton)
    FButton githubButton;

    /**
     * The Qiita button.
     */
    @BindView(R.id.qiitaButton)
    FButton qiitaButton;

    private BillingClient mBillingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        setTitle("Yes! Its Me Dreamwalker");
        ButterKnife.bind(this);

        mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

            }
        }).build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK){
                    Log.e(TAG, "onBillingSetupFinished: " + "OK" );
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

                Log.e(TAG, "onBillingSetupFinished: " + "onBillingServiceDisconnected" );
            }
        });
    }

    /**
     * On github clicked.
     *
     * @param view the view
     */
    @OnClick(R.id.GithubButton)
    void onGithubClicked(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(IntentConst.WEB_URL, "https://github.com/JAICHANGPARK");
        startActivity(intent);
    }

    /**
     * On qitta clicked.
     *
     * @param view the view
     */
    @OnClick(R.id.qiitaButton)
    void onQittaClicked(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(IntentConst.WEB_URL, "https://qiita.com/Dreamwalker");
        startActivity(intent);
    }

}
