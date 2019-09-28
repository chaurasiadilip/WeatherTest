package com.samayteck.weathertest.retrofit;

/**
 * Created by dileep on 08/01/17.
 */
import com.samayteck.weathertest.Model.Forecast;
import com.samayteck.weathertest.Model.WeatherData;

import retrofit.Call;


import retrofit.http.GET;
import retrofit.http.Query;
public interface RestApi {
    @GET("/data/2.5/weather?q=Mumbai&appid=e419c651eb892b2525f74910ff5b8f94&units=metric")
    Call<WeatherData> getWeatherInfo ();

    @GET("/data/2.5/forecast?q=Mumbai&appid=e419c651eb892b2525f74910ff5b8f94&units=metric")
    Call<Forecast> getForecastWeatherInfo ();

}