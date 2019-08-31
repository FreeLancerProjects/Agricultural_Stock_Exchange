package com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.fragments.fragments_more;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.agriculturalstockexchange.R;
import com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.agriculturalstockexchange.models.PlaceGeocodeData;
import com.creativeshare.agriculturalstockexchange.models.Services_Model;
import com.creativeshare.agriculturalstockexchange.models.UserModel;
import com.creativeshare.agriculturalstockexchange.preferences.Preferences;
import com.creativeshare.agriculturalstockexchange.remote.Api;
import com.creativeshare.agriculturalstockexchange.share.Common;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Add_Order_Serv extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, OnMapReadyCallback , DatePickerDialog.OnDateSetListener{
    private HomeActivity homeActivity;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    private final int loc_req = 1225;
    private double lat = 0.0, lng = 0.0;
    private float zoom = 15.6f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private boolean stop = false;
    private DatePickerDialog datePickerDialog;
    private Marker marker;
    private GoogleMap mMap;
    private Preferences preferences;
    private UserModel userModel;
    private String cuurent_language, formatedaddress,addressfrom;
    private ImageView back_arrow;

    private ImageView back;
    private EditText edt_quantity,edt_desc;
    private View view1,view2;
    private TextView tv_time,tv_address_from,tv_address_to,tv_from,tv_to;
    private Button bt_send;

    public static Fragment_Add_Order_Serv newInstance() {
        return new Fragment_Add_Order_Serv();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_order_serv, container, false);
        updateUI();
        initView(view);
        getGeoDatafrom(Double.parseDouble(Services_Model.getAdversiting_model().getGoogle_lat()),Double.parseDouble(Services_Model.getAdversiting_model().getGoogle_long()));

        CheckPermission();

        return view;
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        fragment.getMapAsync(this);
        if(Services_Model.getServi().equals("2")||Services_Model.getServi().equals("3")){
            fragment.getView().setVisibility(View.GONE);
        }

    }


    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;

        if (marker == null) {
            IconGenerator iconGenerator = new IconGenerator(homeActivity);
            iconGenerator.setBackground(null);
            View view = LayoutInflater.from(homeActivity).inflate(R.layout.search_map_icon, null);
            iconGenerator.setContentView(view);
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()).draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));

        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }


    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        back_arrow = view.findViewById(R.id.arrow_back);

view1=view.findViewById(R.id.view1);
view2=view.findViewById(R.id.view2);
        edt_quantity = view.findViewById(R.id.edt_quantity);
        tv_time=view.findViewById(R.id.tv_time);
        tv_address_from=view.findViewById(R.id.tv_address_from);
        tv_address_to=view.findViewById(R.id.tv_address_to);
        tv_from=view.findViewById(R.id.tv_from);
        tv_to=view.findViewById(R.id.tv_to);
        edt_desc=view.findViewById(R.id.edt_desc);
        bt_send=view.findViewById(R.id.bt_send);
     //   bt_upgrde = view.findViewById(R.id.bt_upgrade);
        if (cuurent_language.equals("en")) {

            back_arrow.setRotation(180);

        }
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });

       bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });
       tv_time.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               datePickerDialog.show(homeActivity.getFragmentManager(), "");
           }
       });
        createDatePickerDialog();
        if(Services_Model.getServi().equals("2")||Services_Model.getServi().equals("3")){
            tv_address_to.setVisibility(View.INVISIBLE);
            tv_address_from.setVisibility(View.INVISIBLE);
            tv_to.setVisibility(View.INVISIBLE);
            tv_from.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);

        }
    }
    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(homeActivity, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(homeActivity, R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(homeActivity, R.color.colorPrimary));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(cuurent_language));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        // order_time_calender.set(Calendar.YEAR,year);
        //order_time_calender.set(Calendar.MONTH,monthOfYear);
        //order_time_calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        Log.e("kkkk", calendar.getTime().getMonth() + "");

        tv_time.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
       //// date=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)<10?"0"+calendar.get(Calendar.MONTH):calendar.get(Calendar.MONTH))+"-"+(calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
        // date = calendar.get(Calendar.YEAR) + "-" + (calendar.getTime().getMonth()+calendar.getTime().getMonth():calendar.getTime().getMonth()) + "-" + (calendar.getTime().getDay()<10?"0"+calendar.getTime().getDay():calendar.getTime().getDay());
      //  Log.e("kkk",date);
    }

   private void checkdata() {
        String quantity = edt_quantity.getText().toString();
        String time=
                tv_time.getText().toString();
        String decc=edt_desc.getText().toString();
        if (TextUtils.isEmpty(quantity) || ((lat == 0.0 || lng == 0.0)&&Services_Model.getServi().equals("1") )|| TextUtils.isEmpty(time)||TextUtils.isEmpty(decc)  || formatedaddress == null) {
            if (TextUtils.isEmpty(quantity)) {
                edt_quantity.setError(getResources().getString(R.string.field_req));
            }
            if (TextUtils.isEmpty(time)) {
                tv_time.setError(getResources().getString(R.string.field_req));
            }
            if (TextUtils.isEmpty(decc)) {
                edt_desc.setError(getResources().getString(R.string.field_req));
            }

            if ((lat == 0.0 || lng == 0.0)&&Services_Model.getServi().equals("1")) {
                Toast.makeText(homeActivity, getResources().getString(R.string.field_req), Toast.LENGTH_LONG).show();
            }

//            Log.e("kklkkl",formatedaddress);

        } else {
            if (formatedaddress == null) {
                getGeoData(lat, lng);
            }
           // Log.e("kklkkl", formatedaddress);
            upgrade(quantity,time, decc, lat, lng, formatedaddress);
        }
    }

    private void upgrade(String quantity, String time,String desc, double lat, double lng, String formatedaddress) {
      //  Log.e("kkkl", formatedaddress);

        final Dialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.show();

//        Log.e("Error", lat + " " + lng + " " + userModel.getUser_id() + "  " + name + "  " + formatedaddress + " " + uri);

        Api.getService().sendservicorder(userModel.getUser_id(), Services_Model.getServi(),quantity ,time,desc, addressfrom, Services_Model.getAdversiting_model().getGoogle_lat(), Services_Model.getAdversiting_model().getGoogle_long(),formatedaddress,lat+"",lng+"",Services_Model.getCompany_id(),Services_Model.getAdversiting_model().getId_advertisement()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                // dialog.dismiss();
                if (response.isSuccessful()) {
                     Common.CreateSignAlertDialog3(homeActivity,getResources().getString(R.string.suc));
                    // preferences = Preferences.getInstance();
                //    Log.e("ss", response.body().getUser_type());

                 //   preferences.create_update_userdata(homeActivity, response.body());
                    // Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.suc));
                   // homeActivity.RefreshActivity(cuurent_language);

                } else {
                    try {
                        Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.failed));
                        Log.e("Error", response.code() + response.message().toString() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + response.errorBody().contentType().toString());
                    }
                    catch (Exception e){

                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                try {
                    Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
                catch (Exception e){

                }
            }
        });

    }



    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(homeActivity, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(homeActivity, new String[]{gps_perm}, gps_req);
        } else {

            initGoogleApiClient();
        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(homeActivity)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(homeActivity, 1255);
                        } catch (Exception e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;

        lng = location.getLongitude();
        lat = location.getLatitude();
        getGeoData(lat, lng);
        AddMarker(lat, lng);

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }


        if (locationCallback != null) {
            LocationServices.getFusedLocationProviderClient(homeActivity).removeLocationUpdates(locationCallback);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(homeActivity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(homeActivity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    getGeoData(lat, lng);

                    AddMarker(lat, lng);
                }
            });

        }
    }

    private void getGeoData(final double lat, final double lng) {

        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, cuurent_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                formatedaddress = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                // address.setText(formatedaddress);
                                //AddMarker(lat, lng);
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                       tv_address_to.setText(formatedaddress);
                             //   Log.e("kkk", formatedaddress);
                            }
                        } else {
                            Log.e("error_code", response.errorBody() + " " + response.code());

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void getGeoDatafrom(final double lat, final double lng) {

        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, cuurent_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                addressfrom = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");

                             tv_address_from.setText(addressfrom);
                                // address.setText(formatedaddress);
                                //AddMarker(lat, lng);
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                                //   Log.e("kkk", formatedaddress);
                            }
                        } else {
                            Log.e("error_code", response.errorBody() + " " + response.code());

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }




}