package nodomain.knu2018.bandutils.adapter.selectdrug;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.events.selectdrug.SelectDrugEvent;

/**
 * Created by KNU2017 on 2018-02-09.
 * @author 박제창 (Dreamwalker)
 * 투약 단위 입력 Adapter View Holder
 */
class MyViewHolder extends RecyclerView.ViewHolder {
    TextView drugNameTextView;
    StepperTouch stepperTouch;

    public MyViewHolder(View itemView) {
        super(itemView);
        drugNameTextView = (TextView) itemView.findViewById(R.id.drugNameTextView);
        stepperTouch = (StepperTouch) itemView.findViewById(R.id.stepperTouch);
    }
}

public class SelectDrugUnitAdapter extends RecyclerView.Adapter<MyViewHolder> {

    ArrayList<String> arrayList;
    Context context;
    // TODO: 2018-06-23 Adapter에 EventBus를 등록합니다. - 박제창 (Dreamwalker)
    EventBus bus = EventBus.getDefault();

    public SelectDrugUnitAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_drug_unit_card, parent, false);
        MyViewHolder myViewHoler = new MyViewHolder(view);
        return myViewHoler;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.drugNameTextView.setText(arrayList.get(position));

        holder.stepperTouch.stepper.setValue(1);
        holder.stepperTouch.stepper.setMin(1);
        holder.stepperTouch.stepper.setMax(30);
        holder.stepperTouch.stepper.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int i, boolean b) {
                bus.post(new SelectDrugEvent(i,arrayList.get(position), position));
                Log.e("onBindViewHolder", "onStep: " + i  + arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
