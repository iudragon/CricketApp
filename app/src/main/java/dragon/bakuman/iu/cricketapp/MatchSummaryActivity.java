package dragon.bakuman.iu.cricketapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MatchSummaryActivity extends AppCompatActivity {

    String url = "http://cricapi.com/api/fantasySummary?apikey=3qGaApK610aiYV2ubVQLJjhTEYA2&unique_id=";

    TextView fieldT1TitleTv, fieldT1DetailTv, fieldT2TitleTv, fieldT2DetailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_summary);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Match Summary");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String uniqueId = intent.getStringExtra("match_id");
        url = url + uniqueId;

        fieldT1TitleTv = findViewById(R.id.fieldT1TitleTv);
        fieldT1DetailTv = findViewById(R.id.fieldT1DetailTv);
        fieldT2TitleTv = findViewById(R.id.fieldT2TitleTv);
        fieldT2DetailTv = findViewById(R.id.fieldT2DetailTv);


        loadData();

    }

    private void loadData() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataObject = jsonObject.getJSONObject("data");

                    JSONArray fieldJArray = dataObject.getJSONArray("fielding");

                    JSONObject field0 = fieldJArray.getJSONObject(0);
                    JSONObject field1 = fieldJArray.getJSONObject(1);

                    String field1Title = field0.getString("title");
                    String field2Title = field1.getString("title");


                    JSONArray field1ScoresJArray = field0.getJSONArray("scores");
                    JSONArray field2ScoresJArray = field1.getJSONArray("scores");

                    fieldT1TitleTv.setText(field1Title);

                    for (int i = 0; i < field1ScoresJArray.length(); i++) {

                        String name = field1ScoresJArray.getJSONObject(i).getString("name");
                        String bowled = field1ScoresJArray.getJSONObject(i).getString("bowled");
                        String catchh = field1ScoresJArray.getJSONObject(i).getString("catch");
                        String lbw = field1ScoresJArray.getJSONObject(i).getString("lbw");
                        String runout = field1ScoresJArray.getJSONObject(i).getString("runout");
                        String stumped = field1ScoresJArray.getJSONObject(i).getString("stumped");

                        fieldT1DetailTv.append("Name: " + name + "\nBowled: " + bowled + "\nCatch: " + catchh + "\nLBW: " + lbw + "\nRunOut: " + runout + "\nStumped: " + stumped + "\n\n");

                    }



                    // for 2

                    fieldT2TitleTv.setText(field2Title);

                    for (int i = 0; i < field2ScoresJArray.length(); i++) {

                        String name = field2ScoresJArray.getJSONObject(i).getString("name");
                        String bowled = field2ScoresJArray.getJSONObject(i).getString("bowled");
                        String catchh = field2ScoresJArray.getJSONObject(i).getString("catch");
                        String lbw = field2ScoresJArray.getJSONObject(i).getString("lbw");
                        String runout = field2ScoresJArray.getJSONObject(i).getString("runout");
                        String stumped = field2ScoresJArray.getJSONObject(i).getString("stumped");

                        fieldT2DetailTv.append("Name: " + name + "\nBowled: " + bowled + "\nCatch: " + catchh + "\nLBW: " + lbw + "\nRunOut: " + runout + "\nStumped: " + stumped + "\n\n");

                    }




                } catch (Exception e) {

                    Toast.makeText(MatchSummaryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MatchSummaryActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
