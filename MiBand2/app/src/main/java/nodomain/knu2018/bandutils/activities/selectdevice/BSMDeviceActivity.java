package nodomain.knu2018.bandutils.activities.selectdevice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.DiscoveryActivity;

public class BSMDeviceActivity extends AppCompatActivity {
    @BindView(R.id.caresens_n_premier_bluetooth)
    LinearLayout caresensPrimierBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsmdevice);
        bindView();


    }

    private void bindView(){
        ButterKnife.bind(this);
    }

    @OnClick(R.id.caresens_n_premier_bluetooth)
    public void onClickedPremierBLE(){
        startActivity(new Intent(BSMDeviceActivity.this, DiscoveryActivity.class));
        finish();
    }
}

