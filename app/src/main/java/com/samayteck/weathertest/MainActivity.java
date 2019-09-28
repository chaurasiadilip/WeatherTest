package com.samayteck.weathertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.pavlospt.CircleView;
import com.samayteck.weathertest.Adapter.RecyclerViewAdapter;
import com.samayteck.weathertest.Model.Forecast;
import com.samayteck.weathertest.Model.WeatherData;
import com.samayteck.weathertest.retrofit.RestApi;
import com.samayteck.weathertest.utility.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private TextView cityCountry;

    private TextView currentDate;

    private ImageView weatherImage;

    private CircleView circleTitle;

    private TextView windResult;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private RestApi service;

    private RecyclerViewAdapter recyclerViewAdapter;

    private TextView humidityResult;
    private static final String URL = "http://api.openweathermap.org";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityCountry = (TextView)findViewById(R.id.city_country);
        currentDate = (TextView)findViewById(R.id.current_date);
        weatherImage = (ImageView)findViewById(R.id.weather_icon);
        circleTitle = (CircleView)findViewById(R.id.weather_result);
        windResult = (TextView)findViewById(R.id.wind_result);
        humidityResult = (TextView)findViewById(R.id.humidity_result);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 4);

        recyclerView = (RecyclerView)findViewById(R.id.weather_daily_list);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

         retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

       service = retrofit.create(RestApi.class);
        Call<WeatherData> call=service.getWeatherInfo();
        call.enqueue(callback);
    }

    private String getTodayDateInStringFormat(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
        return df.format(c.getTime());
    }

    private Callback<WeatherData> callback = new Callback<WeatherData>() {

        String city,status,todayDate,weatherTemp,weatherDescription,windSpeed,humidityValue;
        @Override
        public void onResponse(Response<WeatherData> response, Retrofit retrofit) {
System.out.println("Start");
            String city = response.body().getName()+ ", " + response.body().getSys().getCountry();
            String todayDate = getTodayDateInStringFormat();
            Long tempVal = Math.round(Math.floor(response.body().getMain().getTemp()));
            String weatherTemp = String.valueOf(tempVal) + "°";
            String weatherDescription = Helper.capitalizeFirstLetter(response.body().getWeather().get(0).getDescription());
            String windSpeed = response.body().getWind().getSpeed().toString();
            String humidityValue = response.body().getMain().getHumidity().toString();
            cityCountry.setText(Html.fromHtml(city));
            currentDate.setText(Html.fromHtml(todayDate));
            circleTitle.setTitleText(Html.fromHtml(weatherTemp).toString());
            circleTitle.setSubtitleText(Html.fromHtml(weatherDescription).toString());
            windResult.setText(Html.fromHtml(windSpeed) + " m/s");
            humidityResult.setText(Html.fromHtml(humidityValue) + " %");

            Call<Forecast> call=service.getForecastWeatherInfo();
            call.enqueue(callback4Forecast);


            }

        @Override
        public void onFailure(Throwable t) {
System.out.println("Errors="+t);
        }
 };
    private Callback<Forecast> callback4Forecast = new Callback<Forecast>() {

        int[] everyday = new int[]{0,0,0,0,0,0,0};
        final List<ForecastObject> daysOfTheWeek = new ArrayList<ForecastObject>();
        @Override
        public void onResponse(Response<Forecast> response, Retrofit retrofit) {

           List<com.samayteck.weathertest.Model.List> weatherInfo = response.body().getList();
            if(weatherInfo!=null){
                for(int i=0;i<weatherInfo.size();i++){
                    String time = weatherInfo.get(i).getDtTxt();
                    String shortDay = Helper.convertTimeToDay(time);
                    String temp = weatherInfo.get(i).getMain().getTemp().toString();
                    String tempMin = weatherInfo.get(i).getMain().getTempMin().toString();

                    if(Helper.convertTimeToDay(time).equals("Mon") && everyday[0] < 1){
                        daysOfTheWeek.add(new ForecastObject(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                        everyday[0] = 1;
                    }
                    if(Helper.convertTimeToDay(time).equals("Tue") && everyday[1] < 1){
                        daysOfTheWeek.add(new ForecastObject(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                        everyday[1] = 1;
                    }
                    if(Helper.convertTimeToDay(time).equals("Wed") && everyday[2] < 1){
                        daysOfTheWeek.add(new ForecastObject(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                        everyday[2] = 1;
                    }
                    if(Helper.convertTimeToDay(time).equals("Thu") && everyday[3] < 1){
                        daysOfTheWeek.add(new ForecastObject(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                        everyday[3] = 1;
                    }
                    if(Helper.convertTimeToDay(time).equals("Fri") && everyday[4] < 1){
                        daysOfTheWeek.add(new ForecastObject(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                        everyday[4] = 1;
                    }
                    if(Helper.convertTimeToDay(time).equals("Sat") && everyday[5] < 1){
                        daysOfTheWeek.add(new ForecastObject(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                        everyday[5] = 1;
                    }
                    if(Helper.convertTimeToDay(time).equals("Sun") && everyday[6] < 1){
                        daysOfTheWeek.add(new ForecastObject(shortDay, R.drawable.small_weather_icon, temp, tempMin));
                        everyday[6] = 1;
                    }
                    recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, daysOfTheWeek);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
            }
        }

        @Override
        public void onFailure(Throwable t) {
         System.out.println("error="+t);
        }
    };
}
