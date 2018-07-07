package nodomain.knu2018.bandutils.activities.selectdevice;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import nodomain.knu2018.bandutils.R;

public class FoodDeviceActivity extends AppCompatActivity {

    @BindView(R.id.food_device_layout_0)
    LinearLayout foodDeviceLayout0;

    @BindView(R.id.food_device_layout_1)
    LinearLayout foodDeviceLayout1;
    @BindView(R.id.food_device_layout_2)
    LinearLayout foodDeviceLayout2;
    @BindView(R.id.food_device_layout_3)
    LinearLayout foodDeviceLayout3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_device);
        setStatusBar();

        foodDeviceLayout0.setOnClickListener(v -> {
            Toast.makeText(this, "특허 등록 예정 및 업데이트 예정", Toast.LENGTH_SHORT).show();
            finish();
        });

        foodDeviceLayout1.setOnClickListener(v -> {
            Toast.makeText(this, "특허 등록 예정 및 업데이트 예정", Toast.LENGTH_SHORT).show();
            finish();
        });

        foodDeviceLayout2.setOnClickListener(v -> {
            Toast.makeText(this, "특허 등록 예정 및 업데이트 예정", Toast.LENGTH_SHORT).show();
            finish();
        });

        foodDeviceLayout3.setOnClickListener(v -> {
            Toast.makeText(this, "특허 등록 예정 및 업데이트 예정", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private void setStatusBar(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_5));
        }
    }
}
