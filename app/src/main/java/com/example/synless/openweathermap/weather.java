package com.example.synless.openweathermap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


public class weather extends AsyncTask<String,Void,Void>
{
    final String open_URL = "http://api.openweathermap.org/data/2.5/weather?";
    final String open_city_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    final String close_URL = "&APPID=0b1cf22c468951b3ab523efe295449cf";
    final String lang = "&lang=";

    public boolean succesfulRequest = false;
    public boolean succesfulParsing = false;
    public boolean end = false;

    //private String language = "en";

    public String full_weather = "-";
    public String lon = "-";
    public String lat = "-";
    public String description = "-";
    public String icon = "-";
    public String temp = "-";
    public String pressure = "-";
    public String humidity = "-";
    public String country = "-";
    public String name = "-";
    public String windSpeed = "-";

    private weather_app w;
    private String mode = "Manual";
    Bitmap weather_icon;

    weather(weather_app _w, String _mode)
    {
        Log.d("weather", "--------------------------------- WEATHER STARTING ---------------------------------");
        w = _w;
        mode = _mode;

        //String fullLanguage =  Locale.getDefault().getDisplayLanguage();
        //language = fullLanguage.substring(0, 2);
        Log.d("weather", "language : " + weather_app.language);
    }

    @Override
    protected Void doInBackground(String... c)
    {
        StringBuilder response = new StringBuilder();
        try
        {
            URL url = null;
            if (mode.contains("Manual"))
            {
                Log.d("weather", "doInBackground");
                //BUILDING THE URL WITH THE LANGUAGE OPTION
                String urlString = open_city_URL + c[0] + close_URL + lang + weather_app.language;
                Log.d("weather", urlString);
                url = new URL(urlString);
            }
            else if (mode.contains("GPS"))
            {
                Log.d("doInBackground", "doInBackground");
                //BUILDING THE URL WITH THE LANGUAGE OPTION

                String urlString = open_URL + c[0] + close_URL + lang + weather_app.language;
                Log.d("doInBackground", urlString);
                url = new URL(urlString);
            }
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            Log.d("doInBackground", "openConnection");
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                Log.d("doInBackground", "HttpURLConnection");
                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                Log.d("doInBackground", "BufferedReader");
                String strLine = null;
                //GET THE WEATHER INFORMATION AS A STRING
                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                full_weather = response.toString();
                Log.d("doInBackground", response.toString());
                input.close();
            }
            succesfulRequest = true;
        }
        catch (Exception e)
        {
            Log.d("doInBackground", "- Exeption : " + e.toString());
        }


        Log.d("doInBackground", "Done");
        try
        {
            Log.d("doInBackground", "Next");
            if (full_weather.length() > 100)
            {
                //CLEAN THE WEATHER_STRING FROM ALL EXTRA PUNCTUATION AND FORMATTING
                String tmpWeatherClean = full_weather.replace("{", "").replace("}", "").replace("[", "").replace("]", "").replace("}", "").replace("\"", "").replace("coord:", "").replace("weather:", "").replace("main:", "").replace("clouds:", "").replace("sys:", "");
                Log.d("doInBackground", tmpWeatherClean);
                //SPLIT IT IN LINE, CONTAINING ONE INFORMATION PER LINE
                String tmpWeatherLine[] = tmpWeatherClean.split(",");
                for (String tmpWeatherSplit : tmpWeatherLine)
                {
                    Log.d("doInBackground", tmpWeatherSplit);
                    //SWITCHG CASE TO FILL THE DIFFERENT
                    String tmpWeatherChunk[] = tmpWeatherSplit.split(":");
                    if (tmpWeatherChunk.length > 1)
                    {
                        switch (tmpWeatherChunk[0])
                        {
                            case "lon":
                                lon = tmpWeatherChunk[1];
                                break;
                            case "lat":
                                lat = tmpWeatherChunk[1];
                                break;
                            case "description":
                                description = tmpWeatherChunk[1].substring(0, 1).toUpperCase() + tmpWeatherChunk[1].substring(1);
                                break;
                            case "icon":
                                icon = tmpWeatherChunk[1];
                                iconToBMP(icon);
                                break;
                            case "temp":
                                temp = tmpWeatherChunk[1];
                                //ONE DECIMAL FORCING
                                double tmpDouble = (Double.parseDouble(temp) - 273.15) * 10;
                                int tmpInt = (int) Math.round(tmpDouble);
                                double tmpDouble2 = tmpInt;
                                tmpDouble2 /= 10;
                                temp = String.valueOf(tmpDouble2) + "°C";
                                break;
                            case "pressure":
                                pressure = tmpWeatherChunk[1];
                                break;
                            case "humidity":
                                humidity = tmpWeatherChunk[1];
                                break;
                            case "country":
                                country = tmpWeatherChunk[1];
                                break;
                            case "name":
                                name = tmpWeatherChunk[1];
                                break;
                            case "wind":
                                windSpeed = Math.round(Double.parseDouble(tmpWeatherChunk[2])*3.6) + "km/h";
                            default:
                                break;
                        }
                    }
                }
            }
            succesfulParsing = true;
        }
        catch (Exception e)
        {
            Log.d("doInBackground", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void _void)
    {   //end = true;
        w.endRequest();
    }

    private void iconToBMP(String _d)
    {
        try
        {
            URL newurl = new URL("http://openweathermap.org/img/w/" + _d + ".png");
            Bitmap tmp_weather_icon = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            int x = (int)((float)(tmp_weather_icon.getWidth()*2.5)/ weather_app.scale);
            int y = (int)((float)(tmp_weather_icon.getHeight()*2.5)/ weather_app.scale);
            weather_icon = Bitmap.createScaledBitmap(tmp_weather_icon,x,y,false);
        }
        catch (Exception e)
        {
            Log.d("iconToBMP", e.toString());
        }
    }



}