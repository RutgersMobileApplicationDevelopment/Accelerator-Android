package com.rumad.week3app;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.rumad.week3app.model.WeatherResponse;
import com.rumad.week3app.service.RetrofitClient;
import com.rumad.week3app.service.WeatherService;


public class WeatherQueryFragment extends Fragment implements MainActivity.MyLocationListener {
    WeatherService service;

    EditText editText;
    Button weatherButton;

    TextView textviewCity;
    TextView textviewTemp;

    public WeatherQueryFragment() {
        // Required empty public constructor
    }

    @Override
    public void updateLocation(Location location) {
        updateWeather(location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) getActivity();
        activity.getLocation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void updateWeather(Location location) {
        Call<WeatherResponse> call;
        Log.d(WeatherQueryFragment.class.getName(), location.getLatitude() + " " + location.getLongitude());
        if (location != null) {
            call = service.getCurrentWeather(WeatherService.APP_ID, location.getLatitude(), location.getLongitude());
        } else {
            call = service.getCurrentWeatherFromCity(WeatherService.APP_ID, editText.getText().toString());
        }

        call.enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.body() != null) {
                            Log.d(WeatherListFragment.class.getName(), response.body().toString());
                        }
                        textviewCity.setText(response.body().getName());
                        textviewTemp.setText(Double.toString(response.body().getMain().getTemp()));
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Log.d(WeatherListFragment.class.getName(), t.getMessage());
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        service = RetrofitClient.getService();


        View view = inflater.inflate(R.layout.fragment_weather_query, container, false);
        editText = view.findViewById(R.id.editText);
        weatherButton = view.findViewById(R.id.weather_button);
        textviewCity = view.findViewById(R.id.textview_city);
        textviewTemp = view.findViewById(R.id.textview_temp);

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWeather(null);
            }
        });

        return view;
    }
}
