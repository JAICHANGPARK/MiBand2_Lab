package nodomain.knu2018.bandutils.adapter.home;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.writing.WriteBloodActivity;
import nodomain.knu2018.bandutils.activities.writing.WriteDrugActivity;
import nodomain.knu2018.bandutils.activities.writing.WriteFitnessActivity;
import nodomain.knu2018.bandutils.activities.writing.WriteMealChooseActivity;
import nodomain.knu2018.bandutils.activities.writing.WriteMealSelectActivity;
import nodomain.knu2018.bandutils.activities.writing.WriteSleepActivity;

class WriteHorizontalViewHolder extends RecyclerView.ViewHolder {

    private Boolean networkState;

    CircularImageView image;
    TextView title;
    LottieAnimationView lottieAnimationView;
    public WriteHorizontalViewHolder(View itemView, Boolean networkState) {
        super(itemView);
        if (networkState){
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }else {
            lottieAnimationView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }


    }


}

public class WriteHorizontalAdapter extends RecyclerView.Adapter<WriteHorizontalViewHolder> {
    private static final String TAG = "WriteHorizontalAdapter";
    Context context;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> url = new ArrayList<>();
    private Boolean networkState;

    public WriteHorizontalAdapter(Context context, ArrayList<String> titleList, ArrayList<String> url, Boolean networkState) {
        this.context = context;
        this.titleList = titleList;
        this.url = url;
        this.networkState = networkState;
    }

    @NonNull
    @Override
    public WriteHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (networkState) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_write_layout, parent, false);
            return new WriteHorizontalViewHolder(view, networkState);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_write_none_layout, parent, false);
            return new WriteHorizontalViewHolder(view, networkState);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull WriteHorizontalViewHolder holder, int position) {

        if (networkState) {
            Glide.with(context).asBitmap().load(url.get(position)).into(holder.image);
            holder.title.setText(titleList.get(position));
            holder.image.setOnClickListener(v -> {
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_blood_sugar))) {
                    context.startActivity(new Intent(context, WriteBloodActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_fitness))) {
                    context.startActivity(new Intent(context, WriteFitnessActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_drug))) {
                    context.startActivity(new Intent(context, WriteDrugActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_meal))) {
//                    context.startActivity(new Intent(context, WriteMealActivity.class));
                    context.startActivity(new Intent(context, WriteMealChooseActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_meal_photo))) {
                    context.startActivity(new Intent(context, WriteMealSelectActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_sleep))) {
                    context.startActivity(new Intent(context, WriteSleepActivity.class));
                }
                //Log.e(TAG, "onClick: clicked on horizontla image" + titleList.get(position));
                //Toast.makeText(context, titleList.get(position), Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.title.setText(titleList.get(position));
            holder.lottieAnimationView.setOnClickListener(v -> {
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_blood_sugar))) {
                    context.startActivity(new Intent(context, WriteBloodActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_fitness))) {
                    context.startActivity(new Intent(context, WriteFitnessActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_drug))) {
                    context.startActivity(new Intent(context, WriteDrugActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_meal))) {
//                    context.startActivity(new Intent(context, WriteMealActivity.class));
                    context.startActivity(new Intent(context, WriteMealChooseActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_meal_photo))) {
                    context.startActivity(new Intent(context, WriteMealSelectActivity.class));
                }
                if (titleList.get(position).equals(context.getResources().getString(R.string.home_horizontal_sleep))) {
                    context.startActivity(new Intent(context, WriteSleepActivity.class));
                }
                //Log.e(TAG, "onClick: clicked on horizontla image" + titleList.get(position));
                //Toast.makeText(context, titleList.get(position), Toast.LENGTH_SHORT).show();
            });


        }


    }

    @Override
    public int getItemCount() {
        return url.size();
    }
}
