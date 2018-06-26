package nodomain.knu2018.bandutils.adapter.foodadapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nodomain.knu2018.bandutils.R;

class DetailFoodViewHolder extends RecyclerView.ViewHolder {

    TextView title, description;
    CardView container;


    public DetailFoodViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        container = (CardView) itemView.findViewById(R.id.container);
    }
}

public class DetailFoodAdapter extends RecyclerView.Adapter<DetailFoodViewHolder> {
    Context context;
    ArrayList<String> titleList;
    ArrayList<String> valueList;

    public DetailFoodAdapter(Context context, ArrayList<String> titleList, ArrayList<String> valueList) {
        this.context = context;
        this.titleList = titleList;
        this.valueList = valueList;
    }


    @NonNull
    @Override
    public DetailFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_food, parent, false);
        return new DetailFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailFoodViewHolder holder, int position) {
        String title = titleList.get(position);
        String description = valueList.get(position);

        if (title == null) {
            title = "정보 없음";
            holder.title.setText(title);
        } else {
            holder.title.setText(title);
        }

        if (description == null) {
            description = "정보 없음";
            holder.description.setText(description);
        } else {
            holder.description.setText(description);
        }
    }


    @Override
    public int getItemCount() {
        return titleList.size();
    }
}
