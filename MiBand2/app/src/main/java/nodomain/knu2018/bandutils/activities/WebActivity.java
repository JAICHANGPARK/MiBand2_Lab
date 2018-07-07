package nodomain.knu2018.bandutils.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import nodomain.knu2018.bandutils.Const.IntentConst;
import nodomain.knu2018.bandutils.R;

/**
 * The type Web activity.
 */
public class WebActivity extends AppCompatActivity {

    /**
     * The Web view.
     */
    @BindView(R.id.web_view)
    WebView webView;
    /**
     * The Alert dialog.
     */
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        alertDialog = new SpotsDialog(this);
        alertDialog.show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                alertDialog.dismiss();
            }
        });

        if (getIntent() != null) {
            if (!getIntent().getStringExtra(IntentConst.WEB_URL).isEmpty()) {
                webView.loadUrl(getIntent().getStringExtra(IntentConst.WEB_URL));
            }
        }

    }
}
