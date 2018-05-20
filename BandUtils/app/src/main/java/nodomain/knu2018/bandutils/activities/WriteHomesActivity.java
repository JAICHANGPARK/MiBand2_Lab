package nodomain.knu2018.bandutils.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.bandutils.R;

public class WriteHomesActivity extends AppCompatActivity {

    private static final String TAG = "WriteHomesActivity";

    @BindView(R.id.card_view_bs)
    CardView cardViewBloodSugar;
    @BindView(R.id.card_view_fitness)
    CardView cardViewFitness;
    @BindView(R.id.card_view_drug)
    CardView cardViewDrug;
    @BindView(R.id.card_view_sleep)
    CardView cardViewSleep;
    @BindView(R.id.card_view_meal)
    CardView cardViewMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_homes);
        setTitle("생활 정보 기록");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.card_view_bs)
    public void onClickBloodSugarCardClickListener(){
        startActivity(new Intent(this, WriteBloodActivity.class));

    }

    @OnClick(R.id.card_view_fitness)
    public void onClickFitnessCardClickListener(){
        startActivity(new Intent(this, WriteFitnessActivity.class));
    }

    @OnClick(R.id.card_view_drug)
    public void onClickDrugCardClickListener(){
        startActivity(new Intent(this, WriteDrugActivity.class));
    }

    @OnClick(R.id.card_view_meal)
    public void onClickMealCardClickListener(){
        startActivity(new Intent(this, WriteMealActivity.class));
    }

    @OnClick(R.id.card_view_sleep)
    public void onClickSleepCardClickListener(){
        startActivity(new Intent(this, WriteSleepActivity.class));
    }
}
