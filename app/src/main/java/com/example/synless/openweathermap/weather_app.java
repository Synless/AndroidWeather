package com.example.synless.openweathermap;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class weather_app extends Activity implements LocationListener
{
    weather mainWeather;
    LocationManager locationManager;
    String provider;
    String lat = "";
    String lon = "";
    List<weather> weather_list = new ArrayList<weather>();
    List<String> weather_list_string = new ArrayList<String>();
    Spinner spinner;
    int width = 1200;
    int height = 1824;
    public static float scale = 1;
    public static String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app);
        locationInit();
        spinnerInit();
        rescale();
        language = Locale.getDefault().getDisplayLanguage().substring(0, 2);
        final String language2 = new String(language);
        final EditText edt = (EditText) findViewById(R.id.editTextCity);
        edt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){ }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)  { }

            public void afterTextChanged(Editable s)
            {
                //Log.d("TextWatcher", s.toString());
                String language = Locale.getDefault().getDisplayLanguage().substring(0, 2);
                Button btnWeather = (Button) findViewById(R.id.btnGetWeather);
                if(language2.equals("fr"))
                {
                    btnWeather.setText("Météo de " + edt.getText().toString());
                }
                else
                {
                    btnWeather.setText("Get weather from " + edt.getText().toString());
                }
            }
        });
    }

    private void locationInit()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria critere = new Criteria();
        critere.setAccuracy(Criteria.ACCURACY_LOW);
        critere.setAltitudeRequired(true);
        critere.setBearingRequired(true);
        critere.setCostAllowed(false);
        critere.setPowerRequirement(Criteria.POWER_LOW);
        critere.setSpeedRequired(true);
        provider = locationManager.getBestProvider(critere, true);
        locationManager.requestLocationUpdates(provider, 30000, 250, new LocationListener()
        {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
            @Override
            public void onLocationChanged(Location location)
            {
                Log.d("GPS", "Latitude " + location.getLatitude() + " and longitude " + location.getLongitude());
                lat = Double.toString(location.getLatitude());
                lon = Double.toString(location.getLongitude());
            }
        });
        Log.d("LocationInit", "Best provider -> " + provider);

    }
    private void spinnerInit()
    {
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {   Spinner spinner = (Spinner)findViewById(R.id.spinner);
                String selectedText = spinner.getSelectedItem().toString();
                Log.d("weather_app","You clicked " + selectedText);
                for(weather tmpWeather : weather_list)
                {   if(tmpWeather.name.contains(selectedText))
                {  changeView(tmpWeather);
                    break;
                }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
    private void rescale()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int _width = size.x;
        int _height = size.y;
        float scaleX = ((float) (_width) / (float) (width));
        float scaleY = ((float) (_height) / (float) (height));
        Log.d("Size", String.valueOf(scaleX));
        Log.d("Size", String.valueOf(scaleY));
        DisplayMetrics metrics;
        metrics = getApplicationContext().getResources().getDisplayMetrics();
        scale = metrics.density;
        float f = 1;

        // -----------------------

        TextView _city2 = (TextView) findViewById(R.id._textViewCity);
        f = _city2.getTextSize()/scale;
        _city2.setTextSize(f*scaleY);

        TextView _viewWeather = (TextView) findViewById(R.id._textViewWeather);
        f = _viewWeather.getTextSize()/scale;
        _viewWeather.setTextSize(f*scaleY);

        TextView _viewTemperature = (TextView) findViewById(R.id._textViewTemperature);
        f = _viewTemperature.getTextSize()/scale;
        _viewTemperature.setTextSize(f*scaleY);

        TextView _viewWindSpeed = (TextView) findViewById(R.id._textViewWindSpeed);
        f = _viewWindSpeed.getTextSize()/scale;
        _viewWindSpeed.setTextSize(f*scaleY);

        TextView _viewCountry = (TextView) findViewById(R.id._textViewCountry);
        f = _viewCountry.getTextSize()/scale;
        _viewCountry.setTextSize(f*scaleY);

        TextView _viewLongitude = (TextView) findViewById(R.id._textViewLongitude);
        f = _viewLongitude.getTextSize()/metrics.density;
        _viewLongitude.setTextSize(f*scaleY);

        TextView _viewLatitude = (TextView) findViewById(R.id._textViewLatitude);
        f = _viewLatitude.getTextSize()/scale;
        _viewLatitude.setTextSize(f*scaleY);

        TextView city2 = (TextView) findViewById(R.id.textViewCity);
        f = city2.getTextSize()/scale;
        city2.setTextSize(f*scaleY);

        TextView viewWeather = (TextView) findViewById(R.id.textViewWeather);
        f = viewWeather.getTextSize()/scale;
        viewWeather.setTextSize(f*scaleY);

        TextView viewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        f = viewTemperature.getTextSize()/scale;
        viewTemperature.setTextSize(f*scaleY);

        TextView viewWindSpeed = (TextView) findViewById(R.id.textViewWindSpeed);
        f = viewWindSpeed.getTextSize()/metrics.density;
        viewWindSpeed.setTextSize(f*scaleY);

        TextView viewCountry = (TextView) findViewById(R.id.textViewCountry);
        f = viewCountry.getTextSize()/scale;
        viewCountry.setTextSize(f*scaleY);

        TextView viewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        f = viewLongitude.getTextSize()/scale;
        viewLongitude.setTextSize(f*scaleY);

        TextView viewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        f = viewLatitude.getTextSize()/scale;
        viewLatitude.setTextSize(f*scaleY);

        EditText editText = (EditText) findViewById(R.id.editTextCity);
        f = editText.getTextSize()/scale;
        editText.setTextSize(f*scaleY);

        CheckBox checkBoxMail = (CheckBox) findViewById(R.id.checkBoxMail);
        f = checkBoxMail.getTextSize()/scale;
        checkBoxMail.setTextSize(f*scaleY);

        TextView _textViewFavorite = (TextView) findViewById(R.id._textViewFavorite);
        f = _textViewFavorite.getTextSize()/scale;
        _textViewFavorite.setTextSize(f*scaleY);

        Button btnGetWeather = (Button) findViewById(R.id.btnGetWeather);
        f = btnGetWeather.getTextSize()/scale;
        btnGetWeather.setTextSize(f*scaleY);

        Button btnGetWeatherGPS = (Button) findViewById(R.id.btnGetWeatherGPS);
        f = btnGetWeatherGPS.getTextSize()/scale;
        btnGetWeatherGPS.setTextSize(f*scaleY);

        Button btnFav = (Button) findViewById(R.id.btnFav);
        f = btnFav.getTextSize()/scale;
        btnFav.setTextSize(f*scaleY);

        LinearLayout linear2 = (LinearLayout) findViewById(R.id.linear2);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)linear2.getLayoutParams();
        params2.setMargins(0, (int)((float)(params2.topMargin)/scale), 0, 0);
        linear2.setLayoutParams(params2);
        btnFav.setTextSize(f*scaleY);

        LinearLayout linear3 = (LinearLayout) findViewById(R.id.linear3);
        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams)linear2.getLayoutParams();
        params3.setMargins(0, (int)((float)(params3.topMargin)/scale), 0, 0);
        linear2.setLayoutParams(params3);
        btnFav.setTextSize(f*scaleY);
    }

    //BUTTON HANDLERS
    public void btnGetWeather_clicked(View v)
    {
        //CREATE A NEW WEATHER
        mainWeather = new weather(this, "Manual");

        EditText city = (EditText) findViewById(R.id.editTextCity);
        String stringCity = city.getText().toString();
        Log.d("btnGetWeather", stringCity);

        //EXECUTE THE ASYNCHRONOUS TASK -> GET THE WEATHER DATA FROM THE EDIT_TEXT CITY
        mainWeather.execute(stringCity);
        //ALTERNATIVE WAY WHEN LATE
        //mainWeather.name = stringCity;
        //mainWeather.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //while(!mainWeather.end){}
        //endRequest();
        //FASTER BUT "MEH..."
    }
    public void btnGetWeatherGPS_clicked(View v)
    {

        //CREATE A NEW WEATHER
        mainWeather = new weather(this, "GPS");
        Log.d("btnGetWeatherGPS", "GPS");
        try
        {
            String tmp = locationManager.getLastKnownLocation(provider).toString();
            mainWeather.execute("lat=" + lat + "&lon=" + lon);
            Log.d("btnGetWeatherGPS", "lastLocation -> " + tmp);
        }
        catch (Exception e)
        {
            Log.d("btnGetWeatherGPS", e.toString());
            Log.d("btnGetWeatherGPS", "GPS not in sync.");
            Log.d("btnGetWeatherGPS", "Trying with default values -> Latitude : 48.5491582 & Longitude : 7.73243734");
            if(language.equals("fr"))
            {
                Toast.makeText(getApplicationContext(),"Pas de signal GPS, utilisation des valeurs par defaut", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Can't reach GPS signal, using default values", Toast.LENGTH_LONG).show();
            }
            mainWeather.execute("lat=48.5491582&lon=7.73243734");
        }
    }
    public void btnAddFav_clicked(View v)
    {
        if(mainWeather!=null)
        {
            if (mainWeather.succesfulRequest && mainWeather.succesfulParsing)
            {
                Spinner s = (Spinner) findViewById(R.id.spinner);
                boolean isAlready = true;
                for (String favCity : weather_list_string) {
                    if (favCity.equals(mainWeather.name)) {
                        Log.d("btnAddFav", mainWeather.name + " is already in  weather_list_string");
                        isAlready = false;
                        break;
                    } else {
                        Log.d("btnAddFav", mainWeather.name + " is not in  " + favCity);
                    }
                }
                if (isAlready) {
                    weather_list.add(mainWeather);
                    weather_list_string.add(mainWeather.name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weather_list_string);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner = (Spinner) findViewById(R.id.spinner);
                    spinner.setAdapter(adapter);
                }
            }
        }
        else
        {
            if(language.equals("fr"))
            {
                Toast.makeText(this, "Vous n'avez pas encore de météo à stocker", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "You don't have a weather to store yet", Toast.LENGTH_LONG).show();
            }
        }
    }

    //FUNCTIONS CALLED AT ASYNCTASK ENDING
    public void endRequest()
    {
        if (mainWeather.succesfulParsing)
        {
            changeView(mainWeather);
            if (((CheckBox) findViewById(R.id.checkBoxMail)).isChecked())
            {
                sendMail();
            }
            Log.d("endRequest", "Succes");
        }
        else
        {
            Log.d("endRequest", "Fail");
        }
    }
    private void changeView(weather _w)
    {
        Log.d("changeView", "Changing view controls");
        TextView city2 = (TextView) findViewById(R.id.textViewCity);
        city2.setText(_w.name);
        TextView viewWeather = (TextView) findViewById(R.id.textViewWeather);
        viewWeather.setText(_w.description);
        TextView viewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        viewTemperature.setText(_w.temp);
        TextView viewWindSpeed = (TextView) findViewById(R.id.textViewWindSpeed);
        viewWindSpeed.setText(_w.windSpeed);
        TextView viewCountry = (TextView) findViewById(R.id.textViewCountry);
        viewCountry.setText(_w.country);
        TextView viewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        viewLongitude.setText(_w.lon);
        TextView viewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        viewLatitude.setText(_w.lat);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewIcon);
        imageView.setImageBitmap(mainWeather.weather_icon);
        Log.d("changeView", "Done changing the controls");
    }
    private void sendMail()
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"cyril.vermot@live.fr"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Weather Report");
        email.putExtra(Intent.EXTRA_TEXT, "City : " + mainWeather.name + "\n\r" + "Country : \t" + mainWeather.country + "\n\r" + "Weather : \t" + mainWeather.description + "\n\r" + "Temperature \t: " + mainWeather.temp + "\n\r" + "Wind speed \t: " + mainWeather.windSpeed + "\n\r" + "Latitude \t: " + mainWeather.lat + "\n\r" + "Longitude \t: " + mainWeather.lon);
        email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try
        {
            startActivity(Intent.createChooser(email, "Sending mail..."));
        }
        catch (ActivityNotFoundException ex)
        {
            if(language.equals("fr"))
            {
                Toast.makeText(this, "Pas de client mail installé.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "No email clients installed.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) { }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) { }
    @Override
    public void onProviderEnabled(String s) { }
    @Override
    public void onProviderDisabled(String s) { }
}