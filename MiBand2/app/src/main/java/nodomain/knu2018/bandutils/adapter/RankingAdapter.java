package nodomain.knu2018.bandutils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.model.analysis.Ranks;
import nodomain.knu2018.bandutils.util.VectorDrawableUtils;

class RankingViewHolder extends RecyclerView.ViewHolder {
    public TextView name, score;
    public ImageView rankImage;
    public CardView cardView;

    public RankingViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        score = (TextView) itemView.findViewById(R.id.score);
        rankImage = (ImageView) itemView.findViewById(R.id.rank_image);
        cardView = (CardView) itemView.findViewById(R.id.card_view);

    }
}

public class RankingAdapter extends RecyclerView.Adapter<RankingViewHolder> {

    Context context;
    ArrayList<Ranks.Rank> ranksArrayList;

    public RankingAdapter(Context context, ArrayList<Ranks.Rank> ranksArrayList) {
        this.context = context;
        this.ranksArrayList = ranksArrayList;
        Paper.init(context);
    }


    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_rank_layout, parent, false);
        return new RankingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        String cacheName = Paper.book().read("userName");
        String tmpName = ranksArrayList.get(position).getUserName();
        String userScore = ranksArrayList.get(position).getScore();

        if (tmpName.length() == 3) {
            StringBuilder builder = new StringBuilder(tmpName);
            builder.setCharAt(1, '*');
            String userName = builder.toString();
            holder.name.setText(userName);
        } else if (tmpName.length() > 3) {
            StringBuilder builder = new StringBuilder(tmpName);
            builder.setCharAt(1, '*');
            builder.setCharAt(tmpName.length() - 1, '*');
            String userName = builder.toString();
            holder.name.setText(userName);
        }

        if (cacheName.equals(tmpName)){
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.accent_custom));
        }

        int rank = Integer.valueOf(ranksArrayList.get(position).getRanking());

        if (rank == 1) {
            holder.rankImage.setImageDrawable(VectorDrawableUtils.getDrawable(context, R.drawable.rank_01));
        } else if (rank == 2) {
            holder.rankImage.setImageDrawable(VectorDrawableUtils.getDrawable(context, R.drawable.rank_02));
        } else if (rank == 3) {
            holder.rankImage.setImageDrawable(VectorDrawableUtils.getDrawable(context, R.drawable.rank_03));
        } else {
            holder.rankImage.setImageDrawable(VectorDrawableUtils.getDrawable(context, R.drawable.rank_04));
        }

        String messages = "총 점수 : " + userScore + "점";
        holder.score.setText(messages);
    }

    @Override
    public int getItemCount() {
        return ranksArrayList.size();
    }
}
