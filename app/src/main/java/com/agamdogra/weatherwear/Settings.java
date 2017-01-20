package com.agamdogra.weatherwear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Agam on 2016-11-02.
 */

public class Settings extends AppCompatActivity {

    private SharedPreferences savedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        savedState = getSharedPreferences( "settings", MODE_PRIVATE );
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

    public void onRadioButtonClickedTemperature(View view) {

        SharedPreferences.Editor editor = savedState.edit();
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.celsiusRadio:
                if (checked)
                    editor.putString( "temperature", "celsius");
                Toast.makeText(this,
                        "Temperature preference changed to Celsius", Toast.LENGTH_SHORT).show();
                break;

            case R.id.fahrenheitRadio:
                if (checked)
                    editor.putString( "temperature", "fahrenheit");
                Toast.makeText(this,
                        "Temperature preference changed to Fahrenheit", Toast.LENGTH_SHORT).show();

                break;
        }

        editor.apply();
    }

    public void onRadioButtonClickedSex(View view) {

        SharedPreferences.Editor editor = savedState.edit();
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.femaleRadio:
                if (checked)
                    editor.putString( "sex", "female");
                Toast.makeText(this,
                        "Clothing preference changed to Female", Toast.LENGTH_SHORT).show();
                    break;

            case R.id.maleRadio:
                if (checked)
                    editor.putString( "sex", "male");
                Toast.makeText(this,
                        "Clothing preference changed to Male", Toast.LENGTH_SHORT).show();
                break;
        }

        editor.apply();
    }

}


