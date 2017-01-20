package com.agamdogra.weatherwear;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Agam on 2016-11-02.
 */

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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

}