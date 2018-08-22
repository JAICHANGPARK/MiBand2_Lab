package nodomain.knu2018.bandutils.activities.writing;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.foodadapter.MealChooseAdapter;
import nodomain.knu2018.bandutils.adapter.foodadapter.OnItemClickListener;
import nodomain.knu2018.bandutils.adapter.foodadapter.OnSearchItemClickListener;
import nodomain.knu2018.bandutils.adapter.foodadapter.RecyclerItemTouchHelper;
import nodomain.knu2018.bandutils.adapter.foodadapter.WriteMealSearchAdapter;
import nodomain.knu2018.bandutils.model.foodmodel.FoodCard;
import nodomain.knu2018.bandutils.model.foodmodel.FoodTotal;
import nodomain.knu2018.bandutils.model.foodmodel.MixedFood;
import nodomain.knu2018.bandutils.model.foodmodel.Suggestions;

public class WriteMealChooseActivity extends AppCompatActivity implements OnItemClickListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        OnSearchItemClickListener {
    private static final String TAG = "WriteMealChooseActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.bottom_recycler_view)
    RecyclerView bottomRecyclerView;

    @BindView(R.id.bottomSheet)
    CoordinatorLayout mBottomSheet;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;

    @BindView(R.id.doneButton)
    ImageView doneButton;
    @BindView(R.id.closeButton)
    ImageView closeButton;
    @BindView(R.id.addButton)
    ImageView addButton;


    BottomSheetBehavior bottomSheetBehavior;
    MealChooseAdapter mAdapter;
    WriteMealSearchAdapter searchAdapterV2;
    ArrayList<MixedFood> searchListV2 = new ArrayList<>();


    ArrayList<FoodCard> foodCardArrayList = new ArrayList<>();
    ArrayList<FoodCard> backgroundArrayList = new ArrayList<>();
    ArrayList<MixedFood> mixedFoodArrayList = new ArrayList<>(10);
    ArrayList<FoodTotal> resultArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_meal_choose);

        bindViews();
        setBottomSheetBehavior();
        setInitDataVersion(3);
        initRecyclerView(3);
        initBottomRecyclerView();

        setFloatingSearchView();

    }




    private void bindViews() {
        ButterKnife.bind(this);
    }



    private void setInitDataVersion(int versionCode) {
        switch (versionCode) {
            case 1:
                break;
            case 2:

                break;
            case 3:
                foodCardArrayList.add(new FoodCard("국", "없음", "식품 터치 검색", "0", "0"));
                foodCardArrayList.add(new FoodCard("찬", "없음", "식품 터치 검색", "0", "0"));
                foodCardArrayList.add(new FoodCard("찬", "없음", "식품 터치 검색", "0", "0"));
                foodCardArrayList.add(new FoodCard("찬", "없음", "식품 터치 검색", "0", "0"));
                foodCardArrayList.add(new FoodCard("찬", "없음", "식품 터치 검색", "0", "0"));
                foodCardArrayList.add(new FoodCard("밥", "없음", "식품 터치 검색", "0", "0"));

                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));


                backgroundArrayList.add(new FoodCard("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                backgroundArrayList.add(new FoodCard("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                backgroundArrayList.add(new FoodCard("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                backgroundArrayList.add(new FoodCard("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                backgroundArrayList.add(new FoodCard("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));


        }
    }

    private void initRecyclerView(int versionCode) {
        RecyclerView.LayoutManager mLayoutManager;

        switch (versionCode) {
            case 1:
                break;
            case 2:
//                adapterV2 = new CartListAdapterV2(this, imageList, foodArrayList);
//                mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                recyclerView.setLayoutManager(mLayoutManager);
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//                recyclerView.setAdapter(adapterV2);
//                ItemTouchHelper.SimpleCallback itemTouchHelperCallbackV2 = new RecyclerItemTouchHelperV2(0, ItemTouchHelper.LEFT, this);
//                new ItemTouchHelper(itemTouchHelperCallbackV2).attachToRecyclerView(recyclerView);
//                adapterV2.setOnItemClickListrner(this);
                break;
            case 3:
                mAdapter = new MealChooseAdapter(this, foodCardArrayList);
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(mAdapter);
                ItemTouchHelper.SimpleCallback itemTouchHelperCallbackV3 = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
                new ItemTouchHelper(itemTouchHelperCallbackV3).attachToRecyclerView(recyclerView);
                mAdapter.setOnItemClickListener(this);
                break;
        }
    }


    private void setFloatingSearchView() {

        floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

            searchListV2.clear();
            searchAdapterV2.notifyDataSetChanged();

            List<Suggestions> foodName = new ArrayList<>();
            Log.e(TAG, ": oldQuery --> " + oldQuery + " new Query --> " + newQuery);
//            RealmResults<MixedFoodItem> result1 = realm.where(MixedFoodItem.class).contains("foodName", newQuery).findAll();
//            if (result1.size() != 0) {
//                for (MixedFoodItem item : result1) {
//                    foodName.add(new Suggestions(item.getFoodName()));
//                }
//            }
//            floatingSearchView.swapSuggestions(foodName);

        });


        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                // Log.e(TAG, "onSuggestionClicked: ");
                searchListV2.clear();
                searchAdapterV2.notifyDataSetChanged();

//                Log.e(TAG, "onSuggestionClicked: " + searchSuggestion.getBody());
//                RealmResults<MixedFoodItem> result1 = realm.where(MixedFoodItem.class).contains("foodName", searchSuggestion.getBody()).findAll();
//                searchListV2.addAll(result1);
//                //searchAdapter.notifyDataSetChanged();
//                searchAdapterV2.notifyDataSetChanged();
                floatingSearchView.setSearchFocused(false);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                Log.e(TAG, "onSearchAction: " + currentQuery);
                searchListV2.clear();
                List<String> foodName = new ArrayList<>();

//                RealmResults<MixedFoodItem> result1 = realm.where(MixedFoodItem.class).contains("foodName", currentQuery).findAll();
//                if (result1.size() != 0) {
//                    for (MixedFoodItem item : result1) {
//
//                        Log.e(TAG, "onSearchAction: " + item.getFoodName());
//                        foodName.add(item.getFoodName());
//                    }
//                }

//                searchListV2.addAll(result1);
                //searchAdapter.notifyDataSetChanged();
                searchAdapterV2.notifyDataSetChanged();
            }
        });
    }

    private void initBottomRecyclerView() {

        searchAdapterV2 = new WriteMealSearchAdapter(this, searchListV2);
        // TODO: 2018-08-18 터치 리스너 추가
        searchAdapterV2.setOnSearchItemClickListener(this);
        //bottomRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        bottomRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        bottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // bottomRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // bottomRecyclerView.setHasFixedSize(true);
        //bottomRecyclerView.setNestedScrollingEnabled(true);
        bottomRecyclerView.setAdapter(searchAdapterV2);
    }

    private void setBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        //finish();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheet.requestLayout();
                        //setStatusBarDim(false);
                        break;
                    default:
                        //setStatusBarDim(true);
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    private void setStatusBarDim(boolean dim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(dim ? Color.TRANSPARENT : ContextCompat.getColor(this, getThemedResId(R.attr.colorPrimaryDark)));
        }
    }

    private int getThemedResId(@AttrRes int attr) {
        TypedArray a = getTheme().obtainStyledAttributes(new int[]{attr});
        int resId = a.getResourceId(0, 0);
        a.recycle();
        return resId;
    }










    @OnClick(R.id.fab)
    public void fadClicked() {

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Log.e(TAG, "fadClicked: " + height);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    @OnClick(R.id.addButton)
    public void onClickedAddButton() {
//        foodArrayList.add(new Food("","식품 입력","","","","",""));
//        imageList.add(R.drawable.rice);
        final String[] selectedItem = new String[1];
        String[] listItems = new String[]{"밥", "국", "반찬", "기타"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Items");

        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "onClick: " + i);
                switch (i) {
                    case 0:
                        selectedItem[0] = "밥";
                        break;
                    case 1:
                        selectedItem[0] = "국";
                        break;
                    case 2:
                        selectedItem[0] = "찬";
                        break;
                    case 3:
                        selectedItem[0] = "찬";
                        break;
                }
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "반찬 추가 onClick: " + i);


//                adapterV2.addItem(new Food("", "식품 입력", "", "", "", "", ""),
//                        R.drawable.side_dish_04);
//                adapterV2.notifyDataSetChanged();
//                adapterV3.addItem(new FoodCard(selectedItem[0], "없음", "식품 터치 검색", "0", "0"));
//                adapterV3.notifyDataSetChanged();
//                // TODO: 2018-08-18 백그라운드에서 하나 생성
//                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                backgroundArrayList.add(new FoodCard("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                backgroundDBArrayList.add(new com.dreamwalker.diabetesfoodypilot.database.food.FoodCard("", "", "", "",
//                        "", "", "", "", "", "", "", "", "", "", "", ""));
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @OnClick(R.id.closeButton)
    public void onClickCloseButton() {
        finish();
    }

    @OnClick(R.id.doneButton)
    public void onClickSaveButton() {

        Intent intent = new Intent(WriteMealChooseActivity.this, WriteMealActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()){
            case R.id.name_text:
                Log.e(TAG, "onItemClick: " + " 이름 클릭" );
                break;
            case R.id.amount_text:
                Log.e(TAG, "onItemClick: " + " 중량  클릭" );
                break;
        }

    }

    @Override
    public void onSearchItemClick(View v, int position) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }
}
