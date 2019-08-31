package com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.fragments.fragments_more;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.agriculturalstockexchange.R;
import com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.agriculturalstockexchange.adapters.Adversiment_Adapter;
import com.creativeshare.agriculturalstockexchange.adapters.Company_Adapter;
import com.creativeshare.agriculturalstockexchange.models.Adversiment_Model;
import com.creativeshare.agriculturalstockexchange.models.Catogry_Model;
import com.creativeshare.agriculturalstockexchange.models.Company_Model;
import com.creativeshare.agriculturalstockexchange.models.Services_Model;
import com.creativeshare.agriculturalstockexchange.models.UserModel;
import com.creativeshare.agriculturalstockexchange.preferences.Preferences;
import com.creativeshare.agriculturalstockexchange.remote.Api;
import com.creativeshare.agriculturalstockexchange.share.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Company extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;

    private ImageView  arrow_back;

    private RecyclerView rec_search;
    private ProgressBar progBar;
private LinearLayout ll_no_order;
    //  private List<Catogry_Model.Categories> categories2;


    private GridLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private List<Company_Model.Data> data;
    // private List<Catogry_Model.Categories> categories;

    private Company_Adapter company_adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compay, container, false);
        initView(view);
        getlostcar();
        return view;
    }

    private void initView(View view) {
     data= new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        rec_search = view.findViewById(R.id.rec_search);
        progBar = view.findViewById(R.id.progBar);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        arrow_back = view.findViewById(R.id.arrow_back);
        if (cuurent_language.equals("en")) {

            arrow_back.setRotation(180);
        }
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });
        //cities_models = new ArrayList<>();
        //   categories2 = new ArrayList<>();


        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(homeActivity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBar.setVisibility(View.GONE);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
      company_adapter= new Company_Adapter(data, homeActivity);
        rec_search.setDrawingCacheEnabled(true);
        rec_search.setItemViewCacheSize(25);
        rec_search.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        manager = new GridLayoutManager(homeActivity, 1);
        rec_search.setLayoutManager(manager);

        rec_search.setAdapter(company_adapter);




    }




    public static Fragment_Company newInstance() {
        return new Fragment_Company();
    }

    public void getlostcar() {
        progBar.setVisibility(View.VISIBLE);
        ll_no_order.setVisibility(View.GONE);
        Api.getService()
                .Showcompany(Services_Model.getServi())
                .enqueue(new Callback<Company_Model>() {
                    @Override
                    public void onResponse(Call<Company_Model> call, Response<Company_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            data.clear();
                            data.addAll(response.body().getData());

                            if (data.size() > 0) {
                                ll_no_order.setVisibility(View.GONE);
                                company_adapter.notifyDataSetChanged();

                            } else {
                                ll_no_order.setVisibility(View.VISIBLE);

                            }
                        } else {

                            try {
                                Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Company_Model> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }




}
