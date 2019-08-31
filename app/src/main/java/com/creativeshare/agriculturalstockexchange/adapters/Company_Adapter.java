package com.creativeshare.agriculturalstockexchange.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.agriculturalstockexchange.R;
import com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.agriculturalstockexchange.models.Catogry_Model;
import com.creativeshare.agriculturalstockexchange.models.Company_Model;
import com.creativeshare.agriculturalstockexchange.models.Services_Model;
import com.creativeshare.agriculturalstockexchange.models.UserModel;
import com.creativeshare.agriculturalstockexchange.preferences.Preferences;
import com.creativeshare.agriculturalstockexchange.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Company_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Company_Model.Data> advertsings;
    private Context context;
    private Fragment fragment;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;

    public Company_Adapter(List<Company_Model.Data> advertsings, Context context) {

        this.advertsings = advertsings;
        this.context = context;
        // this.fragment = fragment;
        activity = (HomeActivity) context;
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.company_row, parent, false);
        return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        String type = context.getResources().getString(R.string.company);
        final MyHolder myHolder = (MyHolder) holder;
        final Company_Model.Data advertsing = advertsings.get(myHolder.getAdapterPosition());
        ((MyHolder) holder).tv_name.setText(advertsing.getUser_name());
        ((MyHolder) holder).tv_phone.setText(advertsing.getUser_phone());
        ((MyHolder) holder).tv_address.setText(advertsing.getUser_address());
        if (advertsing.getShipping_serv().equals("1")) {
            type +=" "+ context.getResources().getString(R.string.Shipping);

        }
        if (advertsing.getPackaging_serv().equals("1")) {
            type += " "+context.getResources().getString(R.string.Packaging);

        }
        if (advertsing.getStorage_serv().equals("1")) {
            type += " "+context.getResources().getString(R.string.Storage);

        }
        ((MyHolder) holder).tv_type.setText(type);


        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + advertsing.getUser_image())).placeholder(R.drawable.logo).fit().into(((MyHolder) holder).image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Services_Model.setCompany_id(advertsings.get(holder.getLayoutPosition()).getUser_id());
                activity.DisplayFragmentAddorder();
                //   activity.DisplayFragmentAdversimentDetials(advertsings.get(holder.getLayoutPosition()).getId_advertisement());

            }
        });

    }


    @Override
    public int getItemCount() {
        return advertsings.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private RoundedImageView image;
        private TextView tv_name, tv_phone, tv_type, tv_address, tv_time;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.r_im_search);
            tv_name = itemView.findViewById(R.id.tv_name);

            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_address = itemView.findViewById(R.id.tv_address);


        }

    }


}
