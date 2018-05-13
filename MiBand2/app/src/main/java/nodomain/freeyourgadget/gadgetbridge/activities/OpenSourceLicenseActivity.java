package nodomain.freeyourgadget.gadgetbridge.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import nodomain.freeyourgadget.gadgetbridge.R;
import us.feras.mdv.MarkdownView;

public class OpenSourceLicenseActivity extends AppCompatActivity {

    @BindView(R.id.markdownView)
    MarkdownView markdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarkdownView webView = new MarkdownView(this);
        setContentView(webView);
        webView.loadMarkdownFile("file:///android_asset/hello.md");
//        setContentView(R.layout.activity_open_source_license);
//        setTitle("Licenses Information");
//        ButterKnife.bind(this);
//
//        String text = getResources().getString(R.string.md_sample_data);
//
//        updateMarkdownView(text);
    }

    private void updateMarkdownView(String text) {
        markdownView.loadMarkdown(text);
    }
}
