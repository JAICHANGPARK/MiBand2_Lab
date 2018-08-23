package nodomain.knu2018.bandutils.adapter.foodadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamwalker.avatarlibrary.LabelView;

import java.util.ArrayList;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.foodmodel.FoodCard;

public class MealChooseAdapter extends RecyclerView.Adapter<MealChooseAdapter.MyViewHolder> {

    Context context;
    ArrayList<FoodCard> foodCardArrayList;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, description, amount, exchange;
        public LabelView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);

            name = view.findViewById(R.id.name_text);
            description = view.findViewById(R.id.description_text);
            amount = view.findViewById(R.id.amount_text);
            exchange = view.findViewById(R.id.exchange_text);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            // TODO: 2018-08-15 클릭 리스너 추가

            name.setOnClickListener(this);
            amount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (listener != null){
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public MealChooseAdapter(Context context, ArrayList<FoodCard> foodCardArrayList) {
        this.context = context;
        this.foodCardArrayList = foodCardArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_write_meal_choose_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        final TestModel item = cartList.get(position);
//        holder.name.setText(item.getName());
//        holder.description.setText(item.getDescription());
//        holder.price.setText("₹" + item.getPrice());

        final FoodCard item = foodCardArrayList.get(position);
//        holder.thumbnail.setTextTitle();
        holder.thumbnail.setTextContent(item.getCardClass());
        holder.name.setText(item.getFoodName());
        holder.description.setText(item.getFoodClass());
        holder.amount.setText(item.getFoodAmount() + "g");
        holder.exchange.setText(item.getTotalExchange() +"unit");
        //holder.thumbnail.setTextTitle();

//        Glide.with(context).load(item.getThumbnail()).into(holder.thumbnail);


        //Glide.with(context).load(imageList.get(position)).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return foodCardArrayList.size();
    }

    public void removeItem(int position) {
        foodCardArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(FoodCard item, int position) {
        foodCardArrayList.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(FoodCard item) {
//        imageList.add(image);
        foodCardArrayList.add(item);
        notifyDataSetChanged();
    }
}
