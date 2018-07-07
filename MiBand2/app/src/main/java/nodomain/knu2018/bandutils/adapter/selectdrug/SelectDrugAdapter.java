package nodomain.knu2018.bandutils.adapter.selectdrug;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.selectdrug.SelectDrug;


/**
 * Created by KNU2017 on 2018-02-09.
 */

public class SelectDrugAdapter extends RecyclerView.Adapter<SelectDrugAdapter.SelectDrugViewHolder> {
    List<SelectDrug> people;
    OnItemClickListener mItemClickListener;

    public SelectDrugAdapter(List<SelectDrug> persons) {
        this.people = persons;
    }

    @Override
    public SelectDrugViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_drug_card, viewGroup, false);
        return new SelectDrugViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SelectDrugViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(people.get(i).name);
        personViewHolder.personAge.setText(people.get(i).age);
        personViewHolder.personPhoto.setImageResource(people.get(i).photoId);
        personViewHolder.cbSelected.setChecked(people.get(i).isSelected());

    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position != -1) {
            people.get(position).setSelected(isSelected);
            notifyDataSetChanged();
        }
    }

    public List<SelectDrug> getPeople() {
        return people;
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setPeople(List<SelectDrug> people) {
        this.people = people;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class SelectDrugViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;
        CheckBox cbSelected;

        SelectDrugViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personAge = (TextView) itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            cbSelected = (CheckBox) itemView.findViewById(R.id.cbSelected);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }


}

