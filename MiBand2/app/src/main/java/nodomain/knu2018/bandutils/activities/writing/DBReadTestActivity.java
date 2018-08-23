package nodomain.knu2018.bandutils.activities.writing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.database.meal.WriteMealDatabaseHelper;
import nodomain.knu2018.bandutils.model.foodmodel.FoodCard;

public class DBReadTestActivity extends AppCompatActivity {

    private static final String TAG = "DBReadTestActivity";
    WriteMealDatabaseHelper writeMealDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_read_test);
        writeMealDatabaseHelper = new WriteMealDatabaseHelper(this);

        ArrayList<String> readJson = writeMealDatabaseHelper.readJsonAll();

        Log.e(TAG, "onCreate: " + readJson.size());

        ArrayList<ArrayList<FoodCard>> mapList = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<FoodCard>>() {}.getType();

        for (int i = 0; i < readJson.size(); i++){
            ArrayList<FoodCard> temp = gson.fromJson(readJson.get(i), type);
            for (int j = 0; j < temp.size(); j++){
                Log.e(TAG, "onCreate: " + temp.get(j).getFoodName());
            }
            mapList.add(temp);
        }


    }
}
