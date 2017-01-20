package com.agamdogra.weatherwear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



/**
 * Created by Agam on 2016-11-02.
 */

public class WeatherActivity extends AppCompatActivity {

    private SharedPreferences savedState;

    private String apiKey = "0b6beb5cfe18bd300726d79e04655b10";
    private TextView textView;
    private String tempUnit = "";

    private static final Integer[] weatherIcons = {R.drawable.d01, R.drawable.n01, R.drawable.d02, R.drawable.n02
            , R.drawable.d03, R.drawable.n03, R.drawable.d04, R.drawable.n04, R.drawable.d09, R.drawable.n09
            , R.drawable.d10, R.drawable.n10, R.drawable.d11, R.drawable.n11, R.drawable.d13, R.drawable.n13
            , R.drawable.d50, R.drawable.n50};

    private static final String[] weatherIconsString = {"01d", "01n", "02d", "02n"
            , "03d", "03n", "04d","04n", "09d", "09n"
            , "10d", "10n","11d","11n", "13d","13n"
            , "50d", "50n"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_activity);

        textView = (TextView) findViewById(R.id.info );

        Intent intent = getIntent();

        String coordinates, latitude,longitude;

        coordinates = intent.getStringExtra("coordinates");
       // Toast.makeText(getApplicationContext(), "Your Location is -" + coordinates, Toast.LENGTH_LONG).show();
        coordinates = coordinates.substring(10,coordinates.length()-1);
        latitude = coordinates.substring(0,coordinates.indexOf(","));
        longitude = coordinates.substring(coordinates.indexOf(",") + 1, coordinates.length());

        savedState = getSharedPreferences( "settings", MODE_PRIVATE );

        //textView.setText(coordinates);
        String temp = savedState.getString( "temperature", "celsius" );

        if(temp.contains("fahrenheit")) {

            tempUnit = "°F";
            new ReadJSONFeedTask().execute(

                    // API key is required

                    "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=imperial&APPID=" +
                            apiKey
            );
        }
        else {
            tempUnit = "°C";
            new ReadJSONFeedTask().execute(

                    // API key is required

                    "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=metric&APPID=" +
                            apiKey
            );
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar,
                menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent dataIntent;
        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivityForResult(new Intent(this,WelcomeScreen.class),0);
                return true;

            case R.id.menu_settings:
                dataIntent = new Intent(  "com.agamdogra.weatherwear.Settings" );
                startActivity(dataIntent);
                return true;

            case R.id.menu_about:
                dataIntent = new Intent(  "com.agamdogra.weatherwear.About" );
                startActivity(dataIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private String readJSONFeed( String urlString ) {

        StringBuilder stringBuilder = new StringBuilder();

        // 1. HTTP processing
        //    - connect to a web service by using the HTTP GET method

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet( urlString );

        try {

            Log.d("JSON", "HTTPClinet: execute " + urlString);
            HttpResponse response = client.execute( httpGet );

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();

                InputStream content = entity.getContent();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader( content ) );

                String line;

                // 2. Build the JSON string
                while ((line = reader.readLine()) != null) {

                    stringBuilder.append(line);
                }
            } else {
                Log.d( "JSON", "Failed to download file" );
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e ("JSON", stringBuilder.toString() );
        return stringBuilder.toString();
    }

    /* asynchronous processing: AsyncTask */
    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

        // 1. invoked on the background thread (i.e. asynchronous processing)!
        protected String doInBackground( String... urls ) {

            return readJSONFeed( urls[0] ); // get the JSON string by web service and return it
        }

        // 2. post-processing
        // - result: the JSON string returned by doInBackground( )
        // - invoked on the UI thread
        protected void onPostExecute( String result ) {

            String finalResult="";
            String longitude = "", latitude = "" , cityName = "", currentTemperature = "", maxTemperature = "", minTemperature = "", icon = "", description = "", countryCode = "";

            try {

                Log.d( "JSON", result );

                JSONObject jsonObject = new JSONObject( result );

                JSONArray weatherObjectArray = jsonObject.getJSONArray( "weather" );//weather objects
                //weather object
                JSONObject weatherObject = weatherObjectArray.getJSONObject(0);
                description = weatherObject.getString("description");
                icon = weatherObject.getString("icon");
                ////
                cityName = jsonObject.getString("name");   // "name":

                JSONObject sysObject = jsonObject.getJSONObject( "sys" );
                countryCode = sysObject.getString("country");

                JSONObject coordinatesObject = jsonObject.getJSONObject( "coord" ); // "coord":{

                longitude = coordinatesObject.getString("lon");   // "lon":
                latitude = coordinatesObject.getString("lat" );   // "lat":

                JSONObject temperatureObject = jsonObject.getJSONObject( "main" );   // "main":{

                currentTemperature = temperatureObject.getString( "temp" );// "temp":
                minTemperature = temperatureObject.getString("temp_min");
                maxTemperature = temperatureObject.getString("temp_max");

                Log.d( "JSON", longitude );
                Log.d( "JSON", cityName );
                Log.d( "JSON", currentTemperature );

                finalResult += cityName + ", " + countryCode + "\nCurrent temperature: " +
                        currentTemperature + "\nMax Temperature: " + maxTemperature + tempUnit
                        + "\nMin Temperature: " + minTemperature + "\nDescription: " + description
                        + "\n";

            } catch ( Exception e ) { Log.d( "JSON", e.toString() ); }


            int index = 0;

           for (int i = 0; i < weatherIconsString.length; i++) {
               if (weatherIconsString[i].equals(icon))
                   index = i;
           }

            textView.setText( finalResult );

            ///DISPLAY CLOTHING SUGGESTIONS
            ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
            weatherIcon.setImageResource(weatherIcons[index]);

            ImageView clothingTop = (ImageView) findViewById(R.id.clothingTop);


            ImageView clothingBottom = (ImageView) findViewById(R.id.clothingBottom);


            ImageView shoes = (ImageView) findViewById(R.id.shoes);


            ImageView accessory = (ImageView) findViewById(R.id.accessory);


            String sex = savedState.getString( "sex", "male" );

            Double temp = Double.parseDouble(currentTemperature);

            String unit = savedState.getString( "temperature", "celsius" );

            if(unit.contains("fahrenheit"))
                temp = (temp - 32) * (5/9);

            if(temp > 25)
            {
                if(sex.contentEquals("male"))
                {
                    clothingTop.setImageResource(R.drawable.summertopmale);
                    clothingBottom.setImageResource(R.drawable.shortsmale);
                    shoes.setImageResource(R.drawable.summershoesmale);
                }
                else
                {
                    clothingTop.setImageResource(R.drawable.summertopfemale);
                    clothingBottom.setImageResource(R.drawable.summerskirtfemale);
                    shoes.setImageResource(R.drawable.summershoesfemale);
                }
            }
            else if (temp < 25 && temp > 10)
            {
                if(sex.contentEquals("male"))
                {
                    clothingTop.setImageResource(R.drawable.wintertopmale20to10);
                    clothingBottom.setImageResource(R.drawable.jeansmale);
                    shoes.setImageResource(R.drawable.wintershoesmale20to10);
                    accessory.setImageResource(R.drawable.winterjacketmale20to10);
                }
                else
                {
                    clothingTop.setImageResource(R.drawable.wintertopfemale);
                    clothingBottom.setImageResource(R.drawable.jeansfemale);
                    shoes.setImageResource(R.drawable.wintershoesfemale20to10);
                    accessory.setImageResource(R.drawable.winterjacketfemale20to10);
                }
            }
            else if (temp < 10 && temp > 0)
            {
                if(sex.contentEquals("male"))
                {
                    clothingTop.setImageResource(R.drawable.wintertopmale10to0);
                    clothingBottom.setImageResource(R.drawable.sweatpantsmale);
                    shoes.setImageResource(R.drawable.wintershoesmale20to10);
                    accessory.setImageResource(R.drawable.winterjacketmale10to0);
                }
                else
                {
                    clothingTop.setImageResource(R.drawable.wintertopfemale);
                    clothingBottom.setImageResource(R.drawable.leggingsfemale);
                    shoes.setImageResource(R.drawable.wintershoesfemale20to10);
                    accessory.setImageResource(R.drawable.winterjacketfemale20to10);
                }
            }
            else if (temp <= 0)
            {
                if(sex.contentEquals("male"))
                {
                    clothingTop.setImageResource(R.drawable.wintertopmale10to0);
                    clothingBottom.setImageResource(R.drawable.sweatpantsmale);
                    shoes.setImageResource(R.drawable.wintershoesmale);
                    accessory.setImageResource(R.drawable.winterjacketmalesub0);
                }
                else
                {
                    clothingTop.setImageResource(R.drawable.wintertopfemale);
                    clothingBottom.setImageResource(R.drawable.sweatpantsfemale);
                    shoes.setImageResource(R.drawable.wintershoesfemale);
                    accessory.setImageResource(R.drawable.winterjacketfemalesub0);
                }
            }

            if(description.contains("rain"))
            {
                accessory.setImageResource(R.drawable.umbrella);

            }


        }
    } // end class ReadJSONFeedTask

}
