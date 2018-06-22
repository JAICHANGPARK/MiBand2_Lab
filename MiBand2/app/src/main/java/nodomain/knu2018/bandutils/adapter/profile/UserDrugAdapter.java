package nodomain.knu2018.bandutils.adapter.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.selectdrug.Drugs;

class UserDrugViewHolder extends RecyclerView.ViewHolder {

    CardView container;

    CircularImageView drugImageView;
    TextView drugNameLabel;
    TextView drugValueLabel;


    public UserDrugViewHolder(View view) {
        super(view);
        container = view.findViewById(R.id.card_view);
        drugImageView = view.findViewById(R.id.drug_image);
        drugNameLabel = view.findViewById(R.id.name);
        drugValueLabel = view.findViewById(R.id.value);
    }
}

public class UserDrugAdapter extends RecyclerView.Adapter<UserDrugViewHolder> {

    private static final String TAG = "UserDrugAdapter";
    Context context;
    private List<Drugs> drugList;
    private ViewGroup parent;

    public UserDrugAdapter(Context context, List<Drugs> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    @NonNull
    @Override
    public UserDrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_drug,parent,false);
        return new UserDrugViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDrugViewHolder holder, int position) {

        Glide.with(parent.getContext()).load(R.drawable.device_syringe).into(holder.drugImageView);

        holder.drugNameLabel.setText(drugList.get(position).getDrugName());
        holder.drugValueLabel.setText(drugList.get(position).getValueUnit());

    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }
}
