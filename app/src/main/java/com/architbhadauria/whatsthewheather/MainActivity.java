package com.architbhadauria.whatsthewheather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    public void searchCity(View view){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        if (editText.getText().toString() == "") {
            Toast.makeText(getApplicationContext(), "Enter City Name", Toast.LENGTH_LONG).show();
        }else{

        try {
            String encodedCityName =URLEncoder.encode( editText.getText().toString(), "UTF-8");
            ContentDownloader task = new ContentDownloader();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" +encodedCityName+ "&appid=fc360ac00b7a2e766c74843fa4dc5af3");
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG).show();
        }


        // Log.i("Button Tapped", editText.getText().toString());
    }}

    public class ContentDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data= inputStreamReader.read();
                while(data != -1){

                    char current = (char) data;
                    result += current;
                    data=inputStreamReader.read();
                }
                return result;

            } catch (Exception e){

                Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String messsage ="";
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather Content",weatherInfo);
                JSONArray jsonArray =new JSONArray(weatherInfo);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject jsonObject1= jsonArray.getJSONObject(i);

                    String main="";
                    String description="";

                    main= jsonObject1.getString("main");
                    description= jsonObject1.getString("description");
                    if(main != "" && description!= ""){

                        messsage += main + ": " + description + "\r\n";
                    }
                }
                if(messsage != "")
                {
                    textView.setText(messsage);
                }else {
                    Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not find Weather", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView =(TextView) findViewById(R.id.textView2);
    }
}
