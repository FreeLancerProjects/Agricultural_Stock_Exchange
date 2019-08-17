package com.creativeshare.agriculturalstockexchange.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.agriculturalstockexchange.R;
import com.creativeshare.agriculturalstockexchange.models.Adversiment_Comment_Model;
import com.creativeshare.agriculturalstockexchange.share.Time_Ago;

import java.util.List;

public class Adversiment_Comment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<Adversiment_Comment_Model.Data> advertsings;
    private Context context;
    public Adversiment_Comment_Adapter(List<Adversiment_Comment_Model.Data> advertsings, Context context) {

        this.advertsings = advertsings;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.comment_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            Adversiment_Comment_Model.Data data=advertsings.get(position);
            final MyHolder myHolder = (MyHolder) holder;
            myHolder.tv_name.setText(data.getUser_name());
            myHolder.tv_comment.setText(data.getComment_text());
            myHolder.tv_time.setText(Time_Ago.getTimeAgo(Long.parseLong(data.getDate()), context));

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.progBar.setIndeterminate(true);
        }
    }



    @Override
    public int getItemCount() {
        return advertsings.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView  tv_name, tv_time,tv_comment;

        public MyHolder(View itemView) {
            super(itemView);


            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_date);
            tv_comment=itemView.findViewById(R.id.tv_lastcomment);


        }

    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
     Adversiment_Comment_Model.Data advertsing = advertsings.get(position);
        if (advertsing == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
