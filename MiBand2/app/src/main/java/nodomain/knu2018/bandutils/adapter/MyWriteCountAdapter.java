package nodomain.knu2018.bandutils.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.analysis.MyWriteCount;

class MyWriteCountViewHolder extends RecyclerView.ViewHolder{
    public TextView name, score;
    public ImageView imageView;

    public MyWriteCountViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.text_type);
        score = (TextView) itemView.findViewById(R.id.text_count);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
    }
}

public class MyWriteCountAdapter extends RecyclerView.Adapter<MyWriteCountViewHolder>{
    Context context;
    ArrayList<MyWriteCount>  countDays ;

    public MyWriteCountAdapter(Context context, ArrayList<MyWriteCount> countDays) {
        this.context = context;
        this.countDays = countDays;
    }


    @NonNull
    @Override
    public MyWriteCountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_my_count_layout, parent, false);
        return new MyWriteCountViewHolder(itemView);
    }


    /**
     *  result.add(new MyWriteCount("total", String.valueOf(totalCount)));
     result.add(new MyWriteCount("blood_sugar", String.valueOf(bsCount)));
     result.add(new MyWriteCount("fitness", String.valueOf(fitnessCount)));
     result.add(new MyWriteCount("drug", String.valueOf(drugCount)));
     result.add(new MyWriteCount("meal", String.valueOf(mealCount)));
     result.add(new MyWriteCount("sleep", String.valueOf(sleepCount)));
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyWriteCountViewHolder holder, int position) {

        String type = countDays.get(position).getType();
        if (type.equals("total")){
            String message = "총 기록 수";
            holder.name.setText(message);

        }else if (type.equals("blood_sugar")){
            String message = "혈당 : ";
            holder.name.setText(message);
        }
        else if (type.equals("fitness")){
            String message = "운동 : ";
            holder.name.setText(message);
        }
        else if (type.equals("drug")){
            String message = "투약 : ";
            holder.name.setText(message);
        }
        else if (type.equals("meal")){
            String message = "식사 : ";
            holder.name.setText(message);
        }
        else if (type.equals("sleep")){
            String message = "수면 : ";
            holder.name.setText(message);
        }

        String countMessage = countDays.get(position).getCount() + " 개";
        holder.score.setText(countMessage);

    }

    @Override
    public int getItemCount() {
        return countDays.size();
    }
}
