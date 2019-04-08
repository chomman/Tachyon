package com.pdp.b1_20.tachyon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {





    Button register,login;
    ImageView imgTakenPic;
    EditText username,password;
    String URL = "http://192.168.43.22/Api.php?apicall=existing";
    JSONParser jsonParser = new JSONParser();

    Bitmap  bitmap;
    String _path=null;
    private static final int CAM_REQUEST=1313;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},0);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (Button) findViewById(R.id.btnLoginRegister);
        login = (Button)findViewById(R.id.btnLogin);
        imgTakenPic = (ImageView)findViewById(R.id.imageView);
        username = (EditText)findViewById(R.id.etLoginUsername);
        password = (EditText)findViewById(R.id.etLoginPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogin attemptLogin =new AttemptLogin();
                attemptLogin.execute(username.getText().toString(),password.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        username.setText(null);
        password.setText(null);
        super.onResume();
    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {


            String pid = args[0];
            String password = args[1];

            ArrayList params = new ArrayList();
            if (pid.length() > 0 && password.length()>0 ) {
                params.add(new BasicNameValuePair("pid", pid));
                params.add(new BasicNameValuePair("password", password));
                JSONObject json1 = jsonParser.makeHttpRequest(URL, "POST", params);
                return json1;
            }


            return null;
        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {

                    if(result.getBoolean("login")==true)
                    {
                        Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),Hospital_select.class);
                        i.putExtra("username",result.getString("username"));

                        startActivity(i);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Enter Username and Password", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }


        }

    }

}