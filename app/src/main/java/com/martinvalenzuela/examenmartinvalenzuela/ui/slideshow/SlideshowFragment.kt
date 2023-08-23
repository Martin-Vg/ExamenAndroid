package com.martinvalenzuela.examenmartinvalenzuela.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.martinvalenzuela.examenmartinvalenzuela.WeatherApiClient
import com.martinvalenzuela.examenmartinvalenzuela.WeatherResponse
import com.martinvalenzuela.examenmartinvalenzuela.databinding.FragmentSlideshowBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlideshowFragment : Fragment() {
    var city = ""
    val apiKey = "6803232807d2db181ffffe13a6e68f31"
    lateinit var textView:TextView
    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textView= binding.textSlideshow
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = this?.activity?.getPreferences( Context.MODE_PRIVATE)?:return
        city  = sharedPref.getString("city", "Tijuana").toString()
        search_weather()
    }

    private fun search_weather(){
        val call = WeatherApiClient.weatherService.getCurrentWeather(city, apiKey)

        call.enqueue(object: Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ){
                Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()
                if(response.isSuccessful){
                    val weather = response.body()
                    textView.text = city +
                        "\n\nTemperature: ${weather?.main?.temp}°C \n" +
                        "Maxima: ${weather?.main?.temp_max}°C \n" +
                        "Minima: ${weather?.main?.temp_min}°C \n" +
                        "Humedad: ${weather?.main?.humidity} \n"
                }else{
                    Log.e("MainActivity", response.message())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(activity,"Ocurrio un error", Toast.LENGTH_LONG).show()
            }
        })
    }
}