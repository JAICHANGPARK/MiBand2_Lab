package nodomain.knu2018.bandutils.activities.selectdevice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.flaviofaria.kenburnsview.KenBurnsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.DiscoveryActivity;

public class FitnessDeviceActivity extends AppCompatActivity {
    private static final String TAG = "FitnessDeviceActivity";

    private static final String TOP_FILTER_COLOR = "#BDBDBD";
    private static final String IMAGE_FILTER_COLOR = "#858585";

    @BindView(R.id.top_image)
    KenBurnsView topImageView;

    @BindView(R.id.wearable_layout_0)
    LinearLayout wearableLayout0;
    @BindView(R.id.wearable_layout_1)
    LinearLayout wearableLayout1;
    @BindView(R.id.wearable_layout_2)
    LinearLayout wearableLayout2;
    @BindView(R.id.wearable_layout_3)
    LinearLayout wearableLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fitness_device);
        bindView();

        setImageColorFilter();

        setLayoutListener();
    }

    private void bindView(){
        ButterKnife.bind(this);
    }

    private void setStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_5));
        }
    }

    private void setImageColorFilter() {
        topImageView.setColorFilter(Color.parseColor(TOP_FILTER_COLOR), PorterDuff.Mode.MULTIPLY);
    }

    private void launchDiscoveryActivity() {
        startActivity(new Intent(this, DiscoveryActivity.class));
    }

    private void setLayoutListener() {
        wearableLayout0.setOnClickListener(v -> {
            launchDiscoveryActivity();
            finish();
        });
        wearableLayout1.setOnClickListener(v -> {
            launchDiscoveryActivity();
            finish();
        });
        wearableLayout2.setOnClickListener(v -> {
            launchDiscoveryActivity();
            finish();
        });

        wearableLayout3.setOnClickListener(v -> {
            launchDiscoveryActivity();
            finish();
        });
    }
}
