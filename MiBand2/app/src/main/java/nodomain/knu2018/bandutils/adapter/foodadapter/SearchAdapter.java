package nodomain.knu2018.bandutils.adapter.foodadapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.initfood.DetailFoodInfoActivity;
import nodomain.knu2018.bandutils.model.foodmodel.Food;

import static nodomain.knu2018.bandutils.Const.IntentConst.SELECT_FOOD_NAME;

class SearchViewHolder extends RecyclerView.ViewHolder {

    TextView name, group;
    CardView container;
    ImageView imageView;


    public SearchViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        group = (TextView) itemView.findViewById(R.id.group);
        container = (CardView) itemView.findViewById(R.id.container);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }
}

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private static final String TAG = "SearchAdapter";

    Context context;
    List<Food> foodList;

    public SearchAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_food_db_item, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        Glide.with(context).load(R.drawable.icon_food).into(holder.imageView);
        holder.name.setText(foodList.get(position).getFoodName());
        holder.group.setText(foodList.get(position).getFoodGroup());

        // TODO: 2018-06-26 현재 Intent를 사용해 새로운 엑티비티로 이동하면  TransactionTooLargeException 오류가 납니다. - 박제창
        holder.container.setOnClickListener(v -> {

            Intent intent = new Intent(context, DetailFoodInfoActivity.class);
            String foodName = foodList.get(position).getFoodName();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        (Activity) context,
                        Pair.create(holder.imageView, "food_image"),
                        Pair.create(holder.name, "food_name"),
                        Pair.create(holder.group, "food_group"));
                intent.putExtra(SELECT_FOOD_NAME, foodName);
                context.startActivity(intent, options.toBundle());
            } else {
                intent.putExtra(SELECT_FOOD_NAME, foodName);
                context.startActivity(intent);
            }





//            String foodName = foodList.get(position).getFoodName();
//            String foodKcal = foodList.get(position).getFoodKcal();
//            String foodAmount = foodList.get(position).getFoodAmount();
//            String foodCarb = foodList.get(position).getFoodCarbo();
//            String foodProtain = foodList.get(position).getFoodProtein();
//            String foodFat = foodList.get(position).getFoodFat();
//
//            String massege = foodName + "\n" +
//                    foodKcal + "\n" +
//                    foodAmount + "\n" +
//                    foodCarb + "\n" +
//                    foodProtain + "\n" +
//                    foodFat + "\n";
//            AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                    .setTitle("Information")
//                    .setMessage(massege)
//                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
//            AlertDialog dialog = builder.create();
//            dialog.show();

            //intent.putExtra(SELECT_FOOD_NAME, foodName);
            //context.startActivity(new Intent(context, DetailFoodInfoActivity.class).putExtra("name",foodName));

            //Toast.makeText(cotext, "name : " + foodList.get(position).getFoodName(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}