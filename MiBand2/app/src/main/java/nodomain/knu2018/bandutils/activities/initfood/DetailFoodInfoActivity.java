package nodomain.knu2018.bandutils.activities.initfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.foodadapter.DetailFoodAdapter;
import nodomain.knu2018.bandutils.database.fooddb.DBHelper;
import nodomain.knu2018.bandutils.database.fooddb.Entry;

import static nodomain.knu2018.bandutils.Const.IntentConst.SELECT_FOOD_NAME;


/**
 * 식품 상세 대이터를 보여주기 위한 Activity
 * @author 박제창(Dreamwalker)
 */
public class DetailFoodInfoActivity extends AppCompatActivity {
    private static final String TAG = "DetailFoodInfoActivity";

    DBHelper dbHelper;

    Map<String,String> foodMap;
    String foodName, foodNumber;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.foodName)
    TextView foodNameText;
    @BindView(R.id.foodGroup)
    TextView foodGroupText;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ArrayList<String> titleList;
    ArrayList<String> valueList;

    DetailFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food_info);
        setTitle(getResources().getString(R.string.detail_food_activity_title));

        initSetting();
        foodName = getIntent().getStringExtra(SELECT_FOOD_NAME);
        // TODO: 2018-08-22 전달 받은 이름을 통해 데이터 베이스로 검색
        foodMap = dbHelper.fetchDetailFoodInfo(foodName);

//        for (String name : foodMap.keySet()){
//            Log.e(TAG, "onCreate: " + name );
//        }
//
//        for (String name: foodMap.keySet()){
//            String key = name;
//            String value = foodMap.get(name);
//            Log.e(TAG, "onCreate: Food Map Check  " + key + " " + value );
//        }


        Glide.with(this).load(R.drawable.icon_food).into(imageView);
        foodNameText.setText(foodMap.get(Entry.FoodEntry.COLUNM_NAME_FOOD_NAME));
        foodGroupText.setText(foodMap.get(Entry.FoodEntry.COLUNM_NAME_GROUP));

        initRecycler();
        setData();
        setAdapter();
    }

    private void initSetting(){
        ButterKnife.bind(this);
        foodMap = new HashMap<>();
        dbHelper = new DBHelper(this);
        titleList = new ArrayList<>();
        valueList = new ArrayList<>();
    }

    private void initRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
    }

    private void setData(){

        titleList.add(getResources().getString(R.string.food_detail_Number));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_NUMBER));

        titleList.add(getResources().getString(R.string.food_detail_amount));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_AMOUNT));

        titleList.add(getResources().getString(R.string.food_detail_kcal));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_KCAL));

        titleList.add(getResources().getString(R.string.food_detail_carbo));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_CARBO));

        titleList.add(getResources().getString(R.string.food_detail_protein));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_PROTEIN));

        titleList.add(getResources().getString(R.string.food_detail_fat));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_FAT));

        titleList.add(getResources().getString(R.string.food_detail_sugar));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_SUGAR));

        titleList.add(getResources().getString(R.string.food_detail_natrium));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_NATRIUM));

        titleList.add(getResources().getString(R.string.food_detail_cholesterol));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_CHOLEST));

        titleList.add(getResources().getString(R.string.food_detail_fatty));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_FATTY));

        titleList.add(getResources().getString(R.string.food_detail_trans_fat));
        valueList.add(foodMap.get(Entry.FoodEntry.COLUNM_NAME_TRANS_FATTY));


    }


    private void setAdapter(){
        adapter = new DetailFoodAdapter(this, titleList, valueList);
        recyclerView.setAdapter(adapter);
    }


}
