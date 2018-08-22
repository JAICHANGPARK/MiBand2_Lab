package nodomain.knu2018.bandutils.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ebr163.bifacialview.view.BifacialView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;

/**
 * The type App lab activity.
 */
public class AppLabActivity extends AppCompatActivity {


    /**
     * The Bifacial view.
     */
    @BindView(R.id.view)
    BifacialView bifacialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lab);

        ButterKnife.bind(this);

    }
}
