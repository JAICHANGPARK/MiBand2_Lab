package nodomain.knu2018.bandutils.adapter.foodadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.foodmodel.Food;

class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView name,group;

    public SearchViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.name);
        group = (TextView)itemView.findViewById(R.id.group);

    }
}

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{

    private Context cotext;
    private List<Food> foodList;

    public SearchAdapter(Context cotext, List<Food> foodList) {
        this.cotext = cotext;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_food_db_item,parent,false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.name.setText(foodList.get(position).getFoodName());
        holder.group.setText(foodList.get(position).getFoodGroup());

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}