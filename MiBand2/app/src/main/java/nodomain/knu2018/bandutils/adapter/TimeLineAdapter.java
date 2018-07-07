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


public class
TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

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
        View itemView = inflater.inflate(R.layout.item_timeline_line_padding, parent, false);
        TimeLineViewHolder viewHolder = new TimeLineViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        String date = null;
        String time = null;
        String dateTime = null;
        String titleMessage = null;
        String subMessage = null;

        if (globals.get(position).getKind().equals("0")) {
            date = globals.get(position).getDate();
            time = globals.get(position).getTime();
            dateTime = date + " " + time;

            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(context,
                    R.drawable.ic_marker_active,
                    R.color.time_line_color_1));

            holder.mDate.setText(dateTime);
            titleMessage = "구분 : " + globals.get(position).getType();
            subMessage = globals.get(position).getValue() + " mg/dL";
            holder.mMessage.setText(titleMessage);
            holder.mSubMessage.setText(subMessage);
        } else if (globals.get(position).getKind().equals("1")) {
            date = globals.get(position).getDate();
            time = globals.get(position).getTime();
            dateTime = date + " " + time;

            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(context,
                    R.drawable.ic_marker_active,
                    R.color.time_line_color_2));

            holder.mDate.setText(dateTime);
            titleMessage = "구분 : " + globals.get(position).getType();
            subMessage = globals.get(position).getValue() + " 분";
            holder.mMessage.setText(titleMessage);
            holder.mSubMessage.setText(subMessage);
        }
        else if (globals.get(position).getKind().equals("2")) {
            date = globals.get(position).getDate();
            time = globals.get(position).getTime();
            dateTime = date + " " + time;

            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(context,
                    R.drawable.ic_marker_active,
                    R.color.time_line_color_3));

            holder.mDate.setText(dateTime);
            titleMessage = "인슐린 : " + globals.get(position).getType();
            subMessage = globals.get(position).getValue() + " Unit";
            holder.mMessage.setText(titleMessage);
            holder.mSubMessage.setText(subMessage);
        }
        else if (globals.get(position).getKind().equals("3")) {
            date = globals.get(position).getDate();
            time = globals.get(position).getTime();
            dateTime = date + " " + time;

            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(context,
                    R.drawable.ic_marker_active,
                    R.color.time_line_color_4));

            holder.mDate.setText(dateTime);
            titleMessage = "구분 : " + globals.get(position).getType();
            subMessage = globals.get(position).getValue() + " KCal";
            holder.mMessage.setText(titleMessage);
            holder.mSubMessage.setText(subMessage);
        }
        else if (globals.get(position).getKind().equals("4")) {
            date = globals.get(position).getDate();
            time = globals.get(position).getTime();
            dateTime = date + " " + time;

            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(context,
                    R.drawable.ic_marker_active,
                    R.color.time_line_color_5
            ));

            holder.mDate.setText(dateTime);
            titleMessage = "구분 : " + globals.get(position).getType();
            subMessage = globals.get(position).getValue() + " 분";
            holder.mMessage.setText(titleMessage);
            holder.mSubMessage.setText(subMessage);
        }

//        switch (globals.get(position).getKind()) {
//            case "0":
//                break;
//
//            case "1":
//
//
//                break;
//
//            case "2": // 투약
//
//                break;
//
//            case "3":
//
//
//                break;
//
//            case "4":
//
//
//                break;
//        }


    }

    @Override
    public int getItemCount() {
        return globals.size();
    }
}
