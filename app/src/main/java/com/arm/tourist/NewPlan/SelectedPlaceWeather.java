package com.arm.tourist.NewPlan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arm.tourist.Models.CustomPlace;
import com.arm.tourist.Models.TourPlan;
import com.arm.tourist.R;
import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import java.util.Date;

public class SelectedPlaceWeather extends AppCompatActivity {

    private static final String TAG = "3DaysWeather";
    private static final String OpenWeatherAPIKey = "e6422ef211669ef1c69fb6cf42119749";

    private Button goBackBtn, contBtn;
    private TextView dateTV1, skyTV1, minTmp1, mxTmp1;
    private TextView dateTV2, skyTV2, minTmp2, mxTmp2;
    private TextView dateTV3, skyTV3, minTmp3, mxTmp3;

    TourPlan myplan;
    CustomPlace mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_place_weather);

        Bundle bundle = this.getIntent().getExtras();
        mPlace = bundle.getParcelable("Place");
        Toast.makeText(this,mPlace.getName()+" "+mPlace.getLat()+" "+mPlace.getLongt(),Toast.LENGTH_LONG).show();

        dateTV1 = findViewById(R.id.dateTV1);
        dateTV2 = findViewById(R.id.dateTV2);
        dateTV3 = findViewById(R.id.dateTV3);

        skyTV1 = findViewById(R.id.skyStatusTV1);
        skyTV2 = findViewById(R.id.skyStatusTV2);
        skyTV3 = findViewById(R.id.skyStatusTV3);

        minTmp1 = findViewById(R.id.minTmpTV1);
        minTmp2 = findViewById(R.id.minTmpTV2);
        minTmp3 = findViewById(R.id.minTmpTV3);

        mxTmp1 = findViewById(R.id.maxTmpTV1);
        mxTmp2 = findViewById(R.id.maxTmpTV2);
        mxTmp3 = findViewById(R.id.maxTmpTV3);

        showWeather();
    }
    private void showWeather()
    {
        OpenWeatherMapHelper openWeatherMapHelper = new OpenWeatherMapHelper();
        openWeatherMapHelper.setApiKey(OpenWeatherAPIKey);
        openWeatherMapHelper.setUnits(Units.METRIC);

        openWeatherMapHelper.getThreeHourForecastByGeoCoordinates(mPlace.getLat(),mPlace.getLongt(),
                new OpenWeatherMapHelper.ThreeHourForecastCallback() {
                    @Override
                    public void onSuccess(ThreeHourForecast threeHourForecast) {

                        //day 1
                        dateTV1.setText(getDateString(threeHourForecast.getThreeHourWeatherArray().get(3).getDt()));
                        skyTV1.setText(threeHourForecast.getThreeHourWeatherArray().get(3).getWeatherArray().get(0).getMain());
                        String minTmp = "" + threeHourForecast.getThreeHourWeatherArray().get(2).getMain().getTempMin() +" °C";

                        minTmp1.setText(minTmp);
                        String mxTmp = "" + threeHourForecast.getThreeHourWeatherArray().get(5).getMain().getTempMax()+" °C";
                        mxTmp1.setText(mxTmp);

                        String tmp1 = minTmp, tmp2 = mxTmp;

                        if(tmp1.compareTo(tmp2)<0)
                        {
                            minTmp1.setText(tmp1);
                            mxTmp1.setText(tmp2);
                        }

                        else
                        {     minTmp1.setText(tmp2);
                            mxTmp1.setText(tmp1);

                        }
                        Log.e(TAG,"1: "+ minTmp+" "+mxTmp);

                        //day 2
                        dateTV2.setText(getDateString(threeHourForecast.getThreeHourWeatherArray().get(11).getDt()));
                        skyTV2.setText(threeHourForecast.getThreeHourWeatherArray().get(15).getWeatherArray().get(0).getMain());
                        minTmp = "" + threeHourForecast.getThreeHourWeatherArray().get(10).getMain().getTempMin()+" °C";
                        minTmp2.setText(minTmp);
                        mxTmp = "" + threeHourForecast.getThreeHourWeatherArray().get(13).getMain().getTempMax()+" °C";
                        mxTmp2.setText(mxTmp);

                        tmp1 = minTmp;
                        tmp2 = mxTmp;

                        if(tmp1.compareTo(tmp2)<0)
                        {
                            minTmp2.setText(tmp1);
                            mxTmp2.setText(tmp2);
                        }

                        else
                        {     minTmp2.setText(tmp2);
                            mxTmp2.setText(tmp1);

                        }
                        Log.e(TAG,"1: "+ minTmp+" "+mxTmp);

                        //day 3
                        dateTV3.setText(getDateString(threeHourForecast.getThreeHourWeatherArray().get(19).getDt()));
                        skyTV3.setText(threeHourForecast.getThreeHourWeatherArray().get(19).getWeatherArray().get(0).getMain());
                        minTmp = "" + threeHourForecast.getThreeHourWeatherArray().get(18).getMain().getTempMin()+" °C";
                        minTmp3.setText(minTmp);
                        mxTmp = "" + threeHourForecast.getThreeHourWeatherArray().get(21).getMain().getTempMax()+" °C";
                        mxTmp3.setText(mxTmp);

                        Log.e(TAG,"1: "+ minTmp+" "+mxTmp);


                        tmp1 = minTmp;
                        tmp2 = mxTmp;

                        if(tmp1.compareTo(tmp2)<0)
                        {
                            minTmp3.setText(tmp1);
                            mxTmp3.setText(tmp2);
                        }

                        else
                        {     minTmp3.setText(tmp2);
                            mxTmp3.setText(tmp1);

                        }

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.v(TAG, throwable.getMessage());
                    }
                });
    }

    public String getDateString(long milisec)
    {
        milisec*=1000;
        Date date = new Date(milisec);
        String dateString = date.toString().substring(0,10)+", "+ date.toString().substring(30,34);

        return dateString;
    }
}
