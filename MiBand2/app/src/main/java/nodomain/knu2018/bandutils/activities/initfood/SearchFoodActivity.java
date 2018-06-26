package nodomain.knu2018.bandutils.activities.initfood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.adapter.foodadapter.SearchAdapter;
import nodomain.knu2018.bandutils.database.fooddb.DBHelper;
import nodomain.knu2018.bandutils.model.foodmodel.Food;

/**
 * The type Search food activity.
 */
public class SearchFoodActivity extends AppCompatActivity {

    private static final String TAG = "SearchFoodActivity";

    private static final int REQUEST_PERMISSION = 1000;

    /**
     * The Recycler View.
     */
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    /**
     * The Material search bar.
     */
    @BindView(R.id.search_bar)
    MaterialSearchBar materialSearchBar;

    /**
     * The Layout manager.
     */
    RecyclerView.LayoutManager layoutManager;
    /**
     * The Adapter.
     */
    SearchAdapter adapter;

    /**
     * The Suggest list.
     */
    List<String> suggestList = new ArrayList<>();
    /**
     * The Food list.
     */
    ArrayList<Food> foodList  = new ArrayList<>();
    /**
     * The Db helper.
     */
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        setTitle(getResources().getString(R.string.search_food_activity_title));

        ButterKnife.bind(this);

        dbHelper = new DBHelper(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //database = new NewDBHelper(this);

        foodList = dbHelper.readSomeDate();

        initSearchBar();
        loadSuggestList();
        setSearchBarListener();


        adapter = new SearchAdapter(SearchFoodActivity.this, foodList);
        recyclerView.setAdapter(adapter);

        //sqliteExport();

    }

    private void initSearchBar(){
        materialSearchBar.setHint(getString(R.string.search_food_activity_search_bar_hint));
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.setMaxSuggestionCount(10);
    }

    private void setSearchBarListener(){

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<>();
                for (String search : suggestList){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(String s) {
        adapter = new SearchAdapter(this, dbHelper.searchDBDate(s));
        recyclerView.setAdapter(adapter);

//        adapter = new SearchAdapter(this, dbHelper.getGroupByName(s));
//        recyclerView.setAdapter(adapter);

    }

    /**
     *
     * 처음 화면이 로딩될때 20개 정도의 데이터를 가져오는 메소드
     *
     * 수정(2018-06-06) :
     * Intent 전달시 TransactionTooLargeException 오류가 발생하는데 여기서 20개 이상의 데이터
     * 약 3만 바이트 이상을 차지하기 하고 있었고
     * 10개를 읽어 오는것으로 수정했다. - 박제창
     *
     * @Author : JAICHANGPARK(DREAMWALKER)
     */
    private void loadSuggestList() {
        Log.e(TAG, "loadSuggestList:  called " );
        suggestList = dbHelper.readNameDate();
        materialSearchBar.setLastSuggestions(suggestList);
    }


    /**
     * Sqlite export.
     */
    public void sqliteExport(){
        //Context ctx = this; // for Activity, or Service. Otherwise simply get the context.
        //String dbname = "mydb.db";
        // dbpath = ctx.getDatabasePath(dbname);
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            Log.e(TAG, "getDataDirectory:  - " + data.toString());
            Log.e(TAG, "getExternalStorageDirectory:  - " + sd.toString());

            if (sd.canWrite()) {
                String currentDBPath = "/data/com.dreamwalker.dbfetchtest/databases/meal_db.db";
                String backupDBPath = "meal_db.sqlite";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                if(backupDB.exists()){
                    Toast.makeText(this, "DB Export Complete!!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The type Background task.
     */
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

            return null;
        }

    }
}
