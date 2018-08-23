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
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.foodadapter.MealChooseAdapter;
import nodomain.knu2018.bandutils.adapter.foodadapter.OnItemClickListener;
import nodomain.knu2018.bandutils.adapter.foodadapter.OnSearchItemClickListener;
import nodomain.knu2018.bandutils.adapter.foodadapter.RecyclerItemTouchHelper;
import nodomain.knu2018.bandutils.adapter.foodadapter.WriteMealSearchAdapter;
import nodomain.knu2018.bandutils.database.fooddb.DBHelper;
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

    // TODO: 2018-08-18 선택하지 않고 검색만 할 경우를 대비해야한다. - 박제창
    // TODO: 2018-08-23 onItemClick리스너로 이동
    private int cartListPosition = 1000;

    DBHelper foodDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_meal_choose);

        bindViews();
        initDatabaseHelper();
        setBottomSheetBehavior();
        setInitDataVersion(3);
        initRecyclerView(3);
        initBottomRecyclerView();
        setFloatingSearchView();
        initTosty();

    }


    private void bindViews() {
        ButterKnife.bind(this);
    }

    private void initDatabaseHelper() {
        foodDBHelper = new DBHelper(this);
    }

    private void initTosty() {
        Toasty.Config.getInstance().apply();
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

    /**
     * 검색 처리
     */
    private void setFloatingSearchView() {

        floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

            searchListV2.clear();
            searchAdapterV2.notifyDataSetChanged();

            List<Suggestions> foodName = new ArrayList<>();
            Log.e(TAG, ": oldQuery --> " + oldQuery + " new Query --> " + newQuery);
            ArrayList<MixedFood> searchResult = foodDBHelper.searchDB(newQuery);
            if (searchResult.size() != 0) {
                for (MixedFood item : searchResult) {
                    foodName.add(new Suggestions(item.getFoodName()));
                }
            }
            floatingSearchView.swapSuggestions(foodName);

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

                ArrayList<MixedFood> searchResult = foodDBHelper.searchDBAll(searchSuggestion.getBody());
                searchListV2.addAll(searchResult);
                searchAdapterV2.notifyDataSetChanged();
                floatingSearchView.setSearchFocused(false);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                Log.e(TAG, "onSearchAction: " + currentQuery);
                searchListV2.clear();
                ArrayList<MixedFood> searchResult = foodDBHelper.searchDBAll(currentQuery);
                searchListV2.addAll(searchResult);
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
                // TODO: 2018-08-23 Adapter의 addItem을 호출하여 리스트 하나 추가 - 박제창
                mAdapter.addItem(new FoodCard(selectedItem[0], "없음", "식품 터치 검색",
                        "0", "0"));
                mAdapter.notifyDataSetChanged();
                // TODO: 2018-08-18 백그라운드에서 하나 생성
                mixedFoodArrayList.add(new MixedFood("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                backgroundArrayList.add(new FoodCard("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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

        boolean dataExistCheck = false;
        for (MixedFood mf : mixedFoodArrayList) {
            if (!mf.getFoodName().equals("")) {
                dataExistCheck = true;
            }
        }

        // TODO: 2018-08-18 식품선택이 진행되지 않았을 경우의 사용자 예외 처리 - 박제창
        if (dataExistCheck) {
//            showSaveDialog();
            ArrayList<FoodCard> tempList = new ArrayList<>();
            for (int k = 0; k < backgroundArrayList.size(); k++) {
                if (!backgroundArrayList.get(k).getFoodName().equals("")) {
                    tempList.add(backgroundArrayList.get(k));
                } else {
                    // TODO: 2018-08-18 Pass
                    Log.e(TAG, "onClick: " + "pass");
                }
            }

            if (tempList.size() != 0) {
                resultArrayList.add(new FoodTotal(null, tempList));
                for (FoodTotal m : resultArrayList) {
                    Log.e(TAG, "최종: 크기 " + m.getFoodCardArrayList().size());
                    for (FoodCard mf : m.getFoodCardArrayList()) {
                        Log.e(TAG, "최종 리스트 결과: " + mf.getFoodName());
                    }
                }

                Intent intent = new Intent(WriteMealChooseActivity.this, WriteMealActivity.class);
                intent.putParcelableArrayListExtra("MEAL_CHOOSE_RESULT", tempList);
                startActivity(intent);

            } else {
                Log.e(TAG, "onClick: tempList Size Zero");
            }

        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "식품을 추가해주세요", Snackbar.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onItemClick(View v, int position) {
        int selectedPosition;
        switch (v.getId()) {
            case R.id.name_text:
                cartListPosition = position;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Log.e(TAG, "onItemClick: " + " 이름 클릭");
                break;
            case R.id.amount_text:

                Log.e(TAG, "onItemClick: " + " 중량  클릭");
                selectedPosition = position;

                if (backgroundArrayList.get(position).getFoodAmount().equals("")) {
                    Toasty.error(this, "식품을 검색하여 입력하세요", Toast.LENGTH_SHORT, true).show();
//                    Toast.makeText(this, "식품을 검색하여 입력하세요", Toast.LENGTH_SHORT).show();
                } else {

                    final String userAmount = backgroundArrayList.get(position).getFoodAmount();

                    // TODO: 2018-08-23 기존 값
                    float originValueGroup1 = Float.valueOf(backgroundArrayList.get(position).getFoodGroup1());
                    float originValueGroup2 = Float.valueOf(backgroundArrayList.get(position).getFoodGroup2());
                    float originValueGroup3 = Float.valueOf(backgroundArrayList.get(position).getFoodGroup3());
                    float originValueGroup4 = Float.valueOf(backgroundArrayList.get(position).getFoodGroup4());
                    float originValueGroup5 = Float.valueOf(backgroundArrayList.get(position).getFoodGroup5());
                    float originValueGroup6 = Float.valueOf(backgroundArrayList.get(position).getFoodGroup6());

                    float originValueTotalExchange = Float.valueOf(backgroundArrayList.get(position).getTotalExchange());
                    float originValueKCal = Float.valueOf(backgroundArrayList.get(position).getKcal());

                    float originValueCarbo = Float.valueOf(backgroundArrayList.get(position).getCarbo());
                    float originValueProtein = Float.valueOf(backgroundArrayList.get(position).getProt());
                    float originValueFatt = Float.valueOf(backgroundArrayList.get(position).getFatt());
                    float originValueFiber = Float.valueOf(backgroundArrayList.get(position).getFiber());

                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    EditText input = new EditText(this);
                    input.setText(userAmount);
                    input.setSingleLine(true);

                    builder.setTitle("섭취량(g) 변경");
                    builder.setView(input);
                    builder.setPositiveButton(getString(R.string.ok), (dialog, whichButton) -> {
                        Log.e(TAG, "onItemClick: " + selectedPosition);
                        // TODO: 2018-08-23 중량 변화 값
                        String changeAmount = input.getText().toString();

                        if (userAmount.equals(changeAmount)) {
                            // TODO: 2018-08-23 프로세서 연산 최소화를 위해 만약 기존 값과 같다면 Pass- 박제창
                            input.setText(changeAmount);
                        } else {
                            input.setText(changeAmount);
                            // TODO: 2018-08-23 사용자 중량을 변화 시키면
                            float changeGroup1 = ((Float.valueOf(changeAmount) * originValueGroup1) / Float.valueOf(userAmount));
                            float changeGroup2 = ((Float.valueOf(changeAmount) * originValueGroup2) / Float.valueOf(userAmount));
                            float changeGroup3 = ((Float.valueOf(changeAmount) * originValueGroup3) / Float.valueOf(userAmount));
                            float changeGroup4 = ((Float.valueOf(changeAmount) * originValueGroup4) / Float.valueOf(userAmount));
                            float changeGroup5 = ((Float.valueOf(changeAmount) * originValueGroup5) / Float.valueOf(userAmount));
                            float changeGroup6 = ((Float.valueOf(changeAmount) * originValueGroup6) / Float.valueOf(userAmount));

                            float changeTotalExchange = ((Float.valueOf(changeAmount) * originValueTotalExchange) / Float.valueOf(userAmount));

                            float changeKcal = ((Float.valueOf(changeAmount) * originValueKCal) / Float.valueOf(userAmount));
                            float changeCarbo = ((Float.valueOf(changeAmount) * originValueCarbo) / Float.valueOf(userAmount));
                            float changeProtein = ((Float.valueOf(changeAmount) * originValueProtein) / Float.valueOf(userAmount));
                            float changeFatt = ((Float.valueOf(changeAmount) * originValueFatt) / Float.valueOf(userAmount));
                            float changeFiber = ((Float.valueOf(changeAmount) * originValueFiber) / Float.valueOf(userAmount));

                            changeGroup1 = Math.round(changeGroup1 * 10) / 10.0f;
                            changeGroup2 = Math.round(changeGroup2 * 10) / 10.0f;
                            changeGroup3 = Math.round(changeGroup3 * 10) / 10.0f;
                            changeGroup4 = Math.round(changeGroup4 * 10) / 10.0f;
                            changeGroup5 = Math.round(changeGroup5 * 10) / 10.0f;
                            changeGroup6 = Math.round(changeGroup6 * 10) / 10.0f;

                            changeTotalExchange = Math.round(changeTotalExchange * 10) / 10.0f;

                            changeKcal = Math.round(changeKcal * 10) / 10.0f;
                            changeCarbo = Math.round(changeCarbo * 10) / 10.0f;
                            changeProtein = Math.round(changeProtein * 10) / 10.0f;
                            changeFatt = Math.round(changeFatt * 10) / 10.0f;
                            changeFiber = Math.round(changeFiber * 10) / 10.0f;

                            Log.e(TAG, "변화된 값: " + changeGroup1 + " | "
                                    + changeGroup2 + " | " +
                                    +changeGroup3 + " | " +
                                    changeGroup4 + " | " +
                                    changeGroup5 + " | " +
                                    changeGroup6);
                            Log.e(TAG, "변환 총 교환단위: --> " + changeTotalExchange);
                            Log.e(TAG, "각 식품군 합 -->" + "" + (changeGroup1 + changeGroup2 + changeGroup3 + changeGroup4 + changeGroup5 + changeGroup6));

                            foodCardArrayList.set(selectedPosition,
                                    new FoodCard(backgroundArrayList.get(selectedPosition).getCardClass(),
                                            backgroundArrayList.get(selectedPosition).getFoodClass(),
                                            backgroundArrayList.get(selectedPosition).getFoodName(),
                                            changeAmount,
                                            String.valueOf(changeTotalExchange)));

                            mAdapter.notifyDataSetChanged();

                            backgroundArrayList.set(selectedPosition,
                                    new FoodCard(
                                            backgroundArrayList.get(selectedPosition).getCardClass(),
                                            backgroundArrayList.get(selectedPosition).getFoodClass(),
                                            backgroundArrayList.get(selectedPosition).getFoodName(),
                                            changeAmount,
                                            String.valueOf(changeGroup1),
                                            String.valueOf(changeGroup2),
                                            String.valueOf(changeGroup3),
                                            String.valueOf(changeGroup4),
                                            String.valueOf(changeGroup5),
                                            String.valueOf(changeGroup6),
                                            String.valueOf(changeTotalExchange),
                                            String.valueOf(changeKcal),
                                            String.valueOf(changeCarbo),
                                            String.valueOf(changeProtein),
                                            String.valueOf(changeFatt),
                                            String.valueOf(changeFiber)
                                    ));
                        }
                        dialog.dismiss();
                    });

                    builder.setNegativeButton(getString(R.string.cancel), (dialog, whichButton) -> {
                        dialog.dismiss();
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }

                break;
        }

    }

    @Override
    public void onSearchItemClick(View v, int position) {

        if (cartListPosition != 1000) {
            // TODO: 2018-08-18  아이템을 선택하여 클릭했을때 처리하는 부분입니다. - 박제창
            foodCardArrayList.set(cartListPosition, new FoodCard(foodCardArrayList.get(cartListPosition).getCardClass(),
                    searchListV2.get(position).getFoodClass(),
                    searchListV2.get(position).getFoodName(),
                    searchListV2.get(position).getFoodAmount(),
                    searchListV2.get(position).getTotalExchange()));

            mAdapter.notifyDataSetChanged();

            try {
                backgroundArrayList.set(cartListPosition,
                        new FoodCard(foodCardArrayList.get(cartListPosition).getCardClass(),
                                searchListV2.get(position).getFoodClass(),
                                searchListV2.get(position).getFoodName(),
                                searchListV2.get(position).getFoodAmount(),
                                searchListV2.get(position).getFoodGroup1(),
                                searchListV2.get(position).getFoodGroup2(),
                                searchListV2.get(position).getFoodGroup3(),
                                searchListV2.get(position).getFoodGroup4(),
                                searchListV2.get(position).getFoodGroup5(),
                                searchListV2.get(position).getFoodGroup6(),
                                searchListV2.get(position).getTotalExchange(),
                                searchListV2.get(position).getKcal(),
                                searchListV2.get(position).getCarbo(),
                                searchListV2.get(position).getFatt(),
                                searchListV2.get(position).getProt(),
                                searchListV2.get(position).getFiber()));

                mixedFoodArrayList.set(cartListPosition,
                        new MixedFood(searchListV2.get(position).getFoodClass(),
                                searchListV2.get(position).getFoodName(),
                                searchListV2.get(position).getFoodAmount(),
                                searchListV2.get(position).getFoodGroup1(),
                                searchListV2.get(position).getFoodGroup2(),
                                searchListV2.get(position).getFoodGroup3(),
                                searchListV2.get(position).getFoodGroup4(),
                                searchListV2.get(position).getFoodGroup5(),
                                searchListV2.get(position).getFoodGroup6(),
                                searchListV2.get(position).getTotalExchange(),
                                searchListV2.get(position).getKcal(),
                                searchListV2.get(position).getCarbo(),
                                searchListV2.get(position).getFatt(),
                                searchListV2.get(position).getProt(),
                                searchListV2.get(position).getFiber()
                        ));

            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }


            Log.e(TAG, "onSearchItemClick: " + searchListV2.get(position).getFoodName());

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            cartListPosition = 1000;


        } else {
            // TODO: 2018-08-18 그냥 검색버튼을 사용자가 눌렀을 때는 아무 반응 없이 검색만 가능해야 합니다.
            cartListPosition = 1000;
        }


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof MealChooseAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
//            String name = foodArrayList.get(viewHolder.getAdapterPosition()).getFoodName();
            // backup of removed item for undo purpose
//            final Food deletedItem = foodArrayList.get(viewHolder.getAdapterPosition());
            String name = foodCardArrayList.get(viewHolder.getAdapterPosition()).getFoodName();
            final FoodCard deletedItem = foodCardArrayList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            //adapterV2.removeItem(viewHolder.getAdapterPosition());
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume: ");
        resultArrayList.clear();
        super.onResume();

    }
}
