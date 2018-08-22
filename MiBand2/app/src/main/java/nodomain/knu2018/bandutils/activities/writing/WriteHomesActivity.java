package nodomain.knu2018.bandutils.activities.writing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.bandutils.R;

/**
 * The type Write homes activity.
 */
public class WriteHomesActivity extends AppCompatActivity {

    private static final String TAG = "WriteHomesActivity";

    /**
     * The Card view blood sugar.
     */
    @BindView(R.id.card_view_bs)
    CardView cardViewBloodSugar;
    /**
     * The Card view fitness.
     */
    @BindView(R.id.card_view_fitness)
    CardView cardViewFitness;
    /**
     * The Card view drug.
     */
    @BindView(R.id.card_view_drug)
    CardView cardViewDrug;
    /**
     * The Card view sleep.
     */
    @BindView(R.id.card_view_sleep)
    CardView cardViewSleep;
    /**
     * The Card view meal.
     */
    @BindView(R.id.card_view_meal)
    CardView cardViewMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_homes);
        setTitle("생활 정보 기록");
        ButterKnife.bind(this);
    }

    /**
     * On click blood sugar card click listener.
     */
    @OnClick(R.id.card_view_bs)
    public void onClickBloodSugarCardClickListener(){
        startActivity(new Intent(this, WriteBloodActivity.class));

    }

    /**
     * On click fitness card click listener.
     */
    @OnClick(R.id.card_view_fitness)
    public void onClickFitnessCardClickListener(){
        startActivity(new Intent(this, WriteFitnessActivity.class));
    }

    /**
     * On click drug card click listener.
     */
    @OnClick(R.id.card_view_drug)
    public void onClickDrugCardClickListener(){
        startActivity(new Intent(this, WriteDrugActivity.class));
    }

    /**
     * On click meal card click listener.
     */
    @OnClick(R.id.card_view_meal)
    public void onClickMealCardClickListener(){
//        startActivity(new Intent(this, WriteMealActivity.class));
        startActivity(new Intent(this, WriteMealChooseActivity.class));
    }

    /**
     * On click sleep card click listener.
     */
    @OnClick(R.id.card_view_sleep)
    public void onClickSleepCardClickListener(){
        startActivity(new Intent(this, WriteSleepActivity.class));
    }
}
