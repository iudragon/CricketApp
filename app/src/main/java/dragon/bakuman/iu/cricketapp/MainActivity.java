package dragon.bakuman.iu.cricketapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private String url = "http://cricapi.com/api/matches?apikey=3qGaApK610aiYV2ubVQLJjhTEYA2";

    private RecyclerView.Adapter mAdapter;

    private List<Model> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelList = new ArrayList<>();

        loadUrlData();
    }

    private void loadUrlData() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();


        // Requesting the data, since we are requesting data  so we'll use GET method.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();

                try {

                    JSONArray jsonArray = new JSONObject(response).getJSONArray("matches");

                    // continue getting/setting data while array is completed
                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {

                            String uniqueId = jsonArray.getJSONObject(i).getString("unique_id");
                            String team1 = jsonArray.getJSONObject(i).getString("team-1");
                            String team2 = jsonArray.getJSONObject(i).getString("team-2");
                            String matchType = jsonArray.getJSONObject(i).getString("type");
                            String matchStatus = jsonArray.getJSONObject(i).getString("matchStarted");

                            if (matchStatus.equals("true")) {

                                matchStatus = "Match Started";
                            } else {

                                matchStatus = "Match not Started";
                            }

                            String dateTimeGMT = jsonArray.getJSONObject(i).getString("dateTimeGMT");

                            // converting data format


                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            format1.setTimeZone(TimeZone.getTimeZone(dateTimeGMT));
                            Date date = format1.parse(dateTimeGMT);


                            // convert to dd/MM/yyyy HH:mm
                            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            format2.setTimeZone(TimeZone.getTimeZone("GMT"));

                            // formatted dateTime
                            String dateTime = format2.format(date);

                            // set data
                            Model model = new Model(uniqueId, team1, team2, matchType, matchStatus, dateTime);

                            modelList.add(model);


                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    // adapter to be set to RecyclerView
                    mAdapter = new MyAdapter(modelList, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // enqueue the request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}




