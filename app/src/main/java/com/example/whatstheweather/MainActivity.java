package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String APIdata="",location;
    HttpURLConnection httpURLConnection;
    URL Ukurl;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    String UKUrl;

    String main,description;
    public class DownloadWeather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String ukurl = strings[0];
            try {
                Ukurl = new URL(ukurl);
                httpURLConnection = (HttpURLConnection) Ukurl.openConnection();
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    APIdata += current;
                    data = inputStreamReader.read();
                }
                return APIdata;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //fetching data from API
//                String crappyPrefix = "null";
//
//                if(s.startsWith(crappyPrefix)){
//                    s = s.substring(crappyPrefix.length(), s.length());
//                }
                //Log.i("weathercontent", s);
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                //Log.i("weatherinfo", weatherInfo);
                JSONArray weatherinfo= new JSONArray(weatherInfo);
//                for (int x=0;x<weatherinfo.length();x++)
//                {
//                    weatherinfo.getJSONObject(x);
//                }
                main=weatherinfo.getJSONObject(0).getString("main");
                //Log.i("main",main);
                description=weatherinfo.getJSONObject(0).getString("description");
                //Log.i("description",description);
                TextView mainTv=findViewById(R.id.main);
                TextView descriptionTv=findViewById(R.id.description);
                mainTv.setText(main);
                descriptionTv.setText("details: "+description);

            } catch (Exception e) {
                Log.i("Json",e.getMessage());
            }
        }
    }



    public void findweather(View view){
        EditText locationTv=findViewById(R.id.locationTV);
        location=(locationTv.getText().toString());
        if (location.length()>0){
            APIdata="";
            DownloadWeather downloadWeather=new DownloadWeather();
            UKUrl="https://api.openweathermap.org/data/2.5/weather?q="+location+"&appid=0c8f0989e4594ee937dc7a993f75724a";
            downloadWeather.execute(UKUrl);
        }
        else
            Toast.makeText(this, "Enter a valid city", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}