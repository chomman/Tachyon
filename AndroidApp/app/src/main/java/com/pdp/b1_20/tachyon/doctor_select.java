package com.pdp.b1_20.tachyon;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class doctor_select extends AppCompatActivity {

    LinearLayout ll;
    String URL = "http://192.168.43.22/Api.php?apicall=getdoctor";
    String URL1 = "http://192.168.43.22/Api.php?apicall=gettimings";
    String URL2 = "http://192.168.43.22/Api.php?apicall=senddata";
    String URL3 = "http://192.168.43.22/Api.php?apicall=guiding";

    String hid;
    JSONParser jsonParser = new JSONParser();
    AttemptGetData attemptgetdata = new AttemptGetData();
    Spinner spinnert ;
    Button appointbtn,navigate;
    EditText destination;
    String username;
    LinearLayout.LayoutParams lp;
    JSONObject res;
    String gdid;
    ArrayList<String> spinnerArrayt = new ArrayList<String>();
    Boolean spflag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select);
        Intent i = getIntent();
        spflag =false;
        spinnert = new Spinner(this);
        hid =i.getStringExtra("hid");
        username = i.getStringExtra("username");
        Toast.makeText(getApplicationContext(),hid,Toast.LENGTH_LONG).show();
        ll = (LinearLayout)findViewById(R.id.linid);
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ll.setPadding(50,50,50,50);
        attemptgetdata.execute(hid,username);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        navigate = (Button)findViewById(R.id.navigatebtn);
        destination = (EditText)findViewById(R.id.dest);
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptSendDest attemptsenddest = new AttemptSendDest();

                attemptsenddest.execute(destination.getText().toString());
            }
        });




    }
    private class AttemptSendDest extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            String dest = args[0];
            params.add(new BasicNameValuePair("destination", dest));
            JSONObject json1 = jsonParser.makeHttpRequest(URL3, "POST", params);
            return json1;



        }

        protected void onPostExecute(final JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.Company.Charan");
            if (launchIntent != null) {
                startActivity(launchIntent);//null pointer check in case package name was not found
            }

        }

    }

    private class AttemptGetData extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            String hhid = args[0];
            params.add(new BasicNameValuePair("hid", hhid));
            JSONObject json1 = jsonParser.makeHttpRequest(URL, "POST", params);
            return json1;



        }

        protected void onPostExecute(final JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {

                Boolean flag = result.getBoolean("flag");

                if(flag==true)
                {
                    ArrayList<String> spinnerArray = new ArrayList<String>();

                    final JSONArray temp = result.getJSONArray("data");
                    int len = temp.length();
                    spinnerArray.add("Select doctor...");

                    for(int it=0;it<len;it++)
                    {
                        res = temp.getJSONObject(it);

                        spinnerArray.add(res.getString("name")+","+res.getString("spl"));

                    }

                    Spinner spinner = new Spinner(doctor_select.this);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(doctor_select.this,R.layout.support_simple_spinner_dropdown_item,spinnerArray);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinner.setBackgroundColor(getResources().getColor(R.color.White));

                    ll.addView(spinner);
                    ViewGroup.MarginLayoutParams layoutParams =
                            (ViewGroup.MarginLayoutParams) spinner.getLayoutParams();
                    layoutParams.setMargins(10, 10, 10, 10);
                    spinner.requestLayout();

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {

                                if (position != 0){

                                    res = temp.getJSONObject(position-1);
                                    AttemptGetTime attempgettime = new AttemptGetTime();
                                    attempgettime.execute(res.getString("did"), hid);

                                }
                            }
                            catch (JSONException e)
                            {
                                Log.e("Exception","JSON");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });




                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }



        }

    }

    private class AttemptGetTime extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            String hhid = args[1];
            String did = args[0];
            gdid = did;
            params.add(new BasicNameValuePair("did", did));

            params.add(new BasicNameValuePair("hid", hhid));
            JSONObject json1 = jsonParser.makeHttpRequest(URL1, "POST", params);
            return json1;



        }

        protected void onPostExecute(final JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {

                Boolean flag = result.getBoolean("flag");

                if(flag==true)
                {
                    spinnerArrayt.clear();
                    final JSONArray temp = result.getJSONArray("data");
                    int len = temp.length();
                    spinnerArrayt.add("Select time...");
                    JSONObject ress;

                    for(int it=0;it<len;it++)
                    {
                        ress = temp.getJSONObject(it);
                        spinnerArrayt.add(ress.getString("tstart")+" to "+ress.getString("tend"));

                    }
                    if(spflag==false) {

                        ll.addView(spinnert);

                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(doctor_select.this, R.layout.support_simple_spinner_dropdown_item, spinnerArrayt);
                    spinnert.setAdapter(spinnerArrayAdapter);
                    spinnert.setBackgroundColor(getResources().getColor(R.color.White));
                    spinnerArrayAdapter.notifyDataSetChanged();
                    if(spflag==false) {
                        ViewGroup.MarginLayoutParams layoutParams =
                                (ViewGroup.MarginLayoutParams) spinnert.getLayoutParams();
                        layoutParams.setMargins(10, 10, 10, 10);
                        spflag=true;
                    }
                    spinnert.requestLayout();

                    spinnert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                            if(position!=0) {
                                if(appointbtn!=null)
                                    appointbtn.setVisibility(View.GONE);
                                appointbtn = new Button(doctor_select.this);
                                appointbtn.setText("Book Appointment");
                                appointbtn.setBackgroundColor(getResources().getColor(R.color.buttoncolor));
                                appointbtn.setLayoutParams(lp);
                                appointbtn.setGravity(Gravity.CENTER);
                                ViewGroup.MarginLayoutParams layoutParams =
                                        (ViewGroup.MarginLayoutParams) appointbtn.getLayoutParams();
                                layoutParams.setMargins(10, 10, 10, 10);
                                ll.addView(appointbtn);
                                appointbtn.setVisibility(View.VISIBLE);
                                appointbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            AttemptSendData attemptsenddata = new AttemptSendData();
                                            JSONObject ress = temp.getJSONObject(position - 1);

                                            attemptsenddata.execute(gdid, username,ress.getString("tstart"),ress.getString("tend"));
                                        }
                                        catch (JSONException e)
                                        {
                                            Log.e("Exception","JSON");
                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });





                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }



        }

    }

    private class AttemptSendData extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            ArrayList params = new ArrayList();
            String did = args[0];
            String uname = args[1];
            String tstart = args[2];
            String tend = args[3];
            params.add(new BasicNameValuePair("did", did));
            params.add(new BasicNameValuePair("hid", hid));
            params.add(new BasicNameValuePair("username", uname));
            params.add(new BasicNameValuePair("tstart", tstart));
            params.add(new BasicNameValuePair("tend", tend));

            JSONObject json1 = jsonParser.makeHttpRequest(URL2, "POST", params);
            return json1;



        }

        protected void onPostExecute(final JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {

                Boolean flagg = result.getBoolean("flag");

                if(flagg==true)
                {
                    Toast.makeText(getApplicationContext(),"Your Appointment Has been Registered",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }



        }

    }

}
