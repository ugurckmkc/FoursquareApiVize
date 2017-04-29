package com.ugurckmkc.foursquareapivize;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private RecyclerView placePicker;
    private LinearLayoutManager placePickerManager;
    private RecyclerView.Adapter secondActivityAdapter;

    private String foursquareBaseURL = "https://api.foursquare.com/v2/";
    private String foursquareClientID;
    private String foursquareClientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        placePicker = (RecyclerView)findViewById(R.id.coffeeList);
        placePicker.setHasFixedSize(true);

        placePickerManager = new LinearLayoutManager(this);
        placePicker.setLayoutManager(placePickerManager);
        placePicker.addItemDecoration(new DividerItemDecoration(placePicker
                .getContext(), placePickerManager.getOrientation()));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        foursquareClientID = getResources().getString(R.string.foursquare_client_id);
        foursquareClientSecret = getResources().getString(R.string.foursquare_client_secret);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices
                    .FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                String userLL = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                double userLLAcc = mLastLocation.getAccuracy();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(foursquareBaseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                FoursquareService foursquare = retrofit.create(FoursquareService.class);

                Call<FoursquareJSON> coffeeCall = foursquare.searchCoffee(
                        foursquareClientID,
                        foursquareClientSecret,
                        userLL,
                        userLLAcc);
                coffeeCall.enqueue(new Callback<FoursquareJSON>() {
                    @Override
                    public void onResponse(Call<FoursquareJSON> call, Response<FoursquareJSON> response) {

                        FoursquareJSON fjson = response.body();
                        FoursquareResponse fr = fjson.response;
                        FoursquareGroup fg = fr.group;
                        List<FoursquareResults> frs = fg.results;

                        secondActivityAdapter = new SecondActivityAdapter(getApplicationContext(), frs);
                        placePicker.setAdapter(secondActivityAdapter);
                    }

                    @Override
                    public void onFailure(Call<FoursquareJSON> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Bağlantı Başarısız!(Foursquare)", Toast.LENGTH_LONG).show();
                        finish();
                    }

                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();

        mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Bağlantı Başarısız!(Google)", Toast.LENGTH_LONG).show();
        finish();
    }
}