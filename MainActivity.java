package com.example.test5_tudose_razvan_1101;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.tudose_razvan_listViewJson);

        String jsonUrl = "https://pastebin.com/raw/q3e86QyH";

        new Thread(() -> {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(jsonUrl).openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                int data = inputStreamReader.read();
                while (data != -1) {
                    stringBuilder.append((char) data);
                    data = inputStreamReader.read();
                }
                inputStreamReader.close();

                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                ArrayList<ContBancar> conturi = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ContBancar cont = new ContBancar(
                            jsonObject.getString("numeDetinator"),
                            jsonObject.getLong("iban"),
                            jsonObject.getInt("anExpirare"),
                            (float) jsonObject.getDouble("sumaBani")
                    );
                    conturi.add(cont);
                }

                runOnUiThread(() -> {
                    ArrayAdapter<ContBancar> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            conturi
                    );
                    listView.setAdapter(adapter);
                });
            } catch (Exception e) {
                Log.e("MainActivity", "Eroare la descarcarea JSON", e);
            }
        }).start();
    }
}
