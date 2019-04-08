package com.pdp.b1_20.tachyon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hospital_select extends AppCompatActivity {

    TextView text[] = new TextView[20];
    CardView c[] = new CardView[20];
    JSONParser jsonParser = new JSONParser();
    String username;
    LinearLayout ll;
    AttemptGetData attemptgetdata = new AttemptGetData();

    String URL = "http://192.168.43.22/Api.php?apicall=hospiget";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_select);
        Intent i = getIntent();
        username  =i.getStringExtra("username");
        ll = findViewById(R.id.llid);
        attemptgetdata.execute();

    }



    private class AttemptGetData extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

                    ArrayList params = new ArrayList();
                    JSONObject json1 = jsonParser.makeHttpRequest(URL, "POST", params);
                    return json1;



        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onPostExecute(final JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {

                Boolean flag = result.getBoolean("flag");

                if(flag==true)
                {
                    JSONArray temp = result.getJSONArray("data");
                    int len = temp.length();
                    JSONObject res;
                    for(int it=0;it<len;it++)
                    {
                        TextView t = new TextView(Hospital_select.this);

                        res = temp.getJSONObject(it);
                        t.setText(Html.fromHtml("\n<h3 >Hospital Id: </h3>"+res.getString("hid")+"<h3>Hospital Name: </h3>"+res.getString("name")+"<h3>Address: </h3>"+res.getString("address")+"<br>",Html.FROM_HTML_MODE_COMPACT));
                        t.setTextSize(20);
                        t.setTextColor(getResources().getColor(R.color.colorPrimary));
                        CardView card = new CardView(Hospital_select.this);
                        t.setPadding(10,10,10,10);
                        card.addView(t);
                        card.setClickable(true);
                        ll.addView(card);
                        ViewGroup.MarginLayoutParams layoutParams =
                                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                        layoutParams.setMargins(10, 10, 10, 20);
                        card.requestLayout();

                        c[it] = card;
                    }
                    for ( int iter = 0; iter < len; iter++) {
                        final int finalI = iter;
                        c[iter].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(getApplicationContext(), doctor_select.class);
                                    JSONArray ja = result.getJSONArray("data");
                                    JSONObject jo = ja.getJSONObject(finalI);
                                    intent.putExtra("username", username);
                                    intent.putExtra("hid", jo.getString("hid"));
                                    startActivity(intent);
                                }
                                catch (JSONException e)
                                {
                                    Toast.makeText(getApplicationContext(),"JSON EXCEPTION",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }



        }

    }
}
