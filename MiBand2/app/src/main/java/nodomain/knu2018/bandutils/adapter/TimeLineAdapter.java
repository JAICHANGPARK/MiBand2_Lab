package nodomain.knu2018.bandutils.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.pattern.PatternGlobal;
import nodomain.knu2018.bandutils.util.VectorDrawableUtils;


class TimeLineViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_timeline_date)
    TextView mDate;
    @BindView(R.id.text_timeline_title)
    TextView mMessage;
    @BindView(R.id.text_timeline_sub)
    TextView mSubMessage;
    @BindView(R.id.time_marker)
    TimelineView mTimelineView;



    public TimeLineViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        //mTimelineView.initLine(viewType);
    }

}


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    Context context;
    ArrayList<PatternGlobal> globals;

    public TimeLineAdapter(Context context, ArrayList<PatternGlobal> globals) {
        this.context = context;
        this.globals = globals;
    }


    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_timeline_line_padding,parent,false);
        TimeLineViewHolder viewHolder = new TimeLineViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {

        String date = globals.get(position).getDate();
        String time = globals.get(position).getTime();
        String dateTime = date + " " + time;

        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(context,
                R.drawable.ic_marker_active,
                R.color.colorPrimary));

        holder.mDate.setText(dateTime);
        holder.mMessage.setText(globals.get(position).getType());
        holder.mSubMessage.setText(globals.get(position).getValue());


    }

    @Override
    public int getItemCount() {
        return globals.size();
    }
}
