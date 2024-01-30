package com.myApplicationWeather.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView

import com.myApplicationWeather.weatherapp.databinding.ActivityHomeBinding
import com.myApplicationWeather.weatherapp.ui.DataClass.weatherApp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class homeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()
        supportActionBar?.hide()

        //add live location code here

        fetchWeatherData("pune")
        sarchCity()
    }
    private fun sarchCity() {
        val searchView=binding.searchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                //updateQueryTextColor(newText)
                return true
            }
        })
    }

    private fun fetchWeatherData(city:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response=retrofit.getWeatherData(
            city,"d42c3df589250d80aa99ff00fdb16a3f","metric"
        )
        response.enqueue(object :Callback<weatherApp>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<weatherApp>, response: Response<weatherApp>) {
                val responseBody=response.body()
                if(response.isSuccessful && responseBody!=null)
                {
                    val temprature= responseBody!!.main.temp
                    val humidity= responseBody!!.main.humidity
                    val windspeed= responseBody!!.wind.speed
                    val sunrise= responseBody!!.sys.sunrise.toLong()
                    val sunset= responseBody!!.sys.sunset.toLong()
                    val sealevel= responseBody!!.main.pressure
                    val condition= responseBody!!.weather.firstOrNull()?.main?:"unknown"
                    val maxTemp= responseBody!!.main.temp_max
                    val minTemp= responseBody!!.main.temp_min
                    binding.temp.text="$temprature °C"
                    binding.weather.text="$condition"
                    binding.maxTemp.text="$maxTemp °C"
                    binding.minTemp.text="$minTemp °C"
                    binding.humidity.text="$humidity %"
                    binding.wind.text="$windspeed m/s"
                    binding.sunrise.text="${setTime(sunrise)}"
                    binding.sunset.text="${setTime(sunset)}"
                    binding.sea.text="$sealevel hPa"
                    binding.cityName.text="$city"
                    binding.date.text=setDate()
                    binding.day.text=setDay(System.currentTimeMillis())
                    changeImageCondition(condition)
                }
            }
            override fun onFailure(call: Call<weatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun changeImageCondition(condition:String) {
        when(condition){
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView2.setAnimation(R.raw.animation_sun3)
            }
            "Partly Clouds","Clouds","Overcast","Mist","Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView2.setAnimation(R.raw.animation_cloud)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView2.setAnimation(R.raw.animation_rain2)
            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView2.setAnimation(R.raw.animation_snow)
            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView2.setAnimation(R.raw.animation_sun3)
            }
        }
        binding.lottieAnimationView2.playAnimation()
    }
    private fun setDate(): String {
        val simpleDateFormat=SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return simpleDateFormat.format((Date()))
    }
    private fun setTime(currentTimeMillis: Long): String {
        val simpleDateFormat=SimpleDateFormat("HH:MM", Locale.getDefault())
        return simpleDateFormat.format(Date(currentTimeMillis*1000))
    }

    private fun setDay(currentTimeMillis: Long):String {
        val simpleDateFormat=SimpleDateFormat("EEEE", Locale.getDefault())
        return simpleDateFormat.format((Date()))
    }
}