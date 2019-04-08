package com.pdp.b1_20.tachyon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {


    EditText editID, editAadhar, editName,editGender,editDOB,editAddr,editpassword,editcnfpassword;
    Button  btnRegister,btnopencam,btnlogin;
    ProgressBar pbar;

    String URL = "http://192.168.43.22/Api.php?apicall=new";
    String URL2 = "http://192.168.43.22/upload.php";
    Bitmap bitmap=null;
    String _path;
    JSONParser jsonParser = new JSONParser();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        pbar = (ProgressBar)findViewById(R.id.progressBar2);
        pbar.setVisibility(View.GONE);


        editID = (EditText) findViewById(R.id.etUsername);
        editName = (EditText) findViewById(R.id.etFullName);
        editAadhar = (EditText) findViewById(R.id.aadhar);
        editGender = (EditText) findViewById(R.id.gender);
        editDOB = (EditText) findViewById(R.id.dob);
        editAddr = (EditText) findViewById(R.id.address);
        editpassword = (EditText)findViewById(R.id.etPassword);
        editcnfpassword = (EditText)findViewById(R.id.etConfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnopencam =(Button)findViewById(R.id.opencam);
        btnlogin = (Button)findViewById(R.id.btnRegisterLogin);
        btnopencam.setOnClickListener(new Main2Activity.btnTakePhotoClicker());

        // Upload code

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btnRegister.setText("Wait...");
                AttemptRegister attemptregister = new AttemptRegister();

                attemptregister.execute(editID.getText().toString(), editName.getText().toString(), editAadhar.getText().toString(), editGender.getText().toString(), editDOB.getText().toString(), editAddr.getText().toString(),editpassword.getText().toString(),editcnfpassword.getText().toString());

            }
        });


    }


    private class AttemptRegister extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {


            String id = args[0];
            String name = args[1];
            String aadhar = args[2];
            String gender = args[3];
            String dob = args[4];
            String address = args[5];
            String password = args[6];
            String cnfpassword = args[7];
            ArrayList params = new ArrayList();
            if (id.length() > 0 && aadhar.length()>0 && name.length()>0 && gender.length()>0 && dob.length()>0 && address.length()>0 && password.length()>0 && password.equals(cnfpassword)) {
                params.add(new BasicNameValuePair("pid", id));
                params.add(new BasicNameValuePair("id", aadhar));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("gender", gender));
                params.add(new BasicNameValuePair("dob", dob));
                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("password", password));
                JSONObject json1 = jsonParser.makeHttpRequest(URL, "POST", params);
                return json1;
            }
            else if(!password.equals(cnfpassword))
            {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                    btnRegister.setText("Done!");

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to Register\nPlease fill all the details correctly", Toast.LENGTH_LONG).show();
                    btnRegister.setText("Resubmit");


                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }


        }

    }
    //Image extraction begins

    public String baseTo64 (Bitmap bitmapImage){
        String imageString = null;
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int width          = bitmapImage.getWidth();
            int height         = bitmapImage.getHeight();
            bitmapImage = Bitmap.createScaledBitmap(bitmapImage, width/4, height/4, true);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            imageString = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageString;
    }

    private class SendImage extends AsyncTask<Bitmap, Bitmap, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();
            pbar.setVisibility(View.VISIBLE);


        }

        @Override

        protected JSONObject doInBackground(Bitmap ...args) {


            String image = baseTo64(args[0]);
            ArrayList params = new ArrayList();

            //params.add(new BasicNameValuePair("image", image));

            params.add(new BasicNameValuePair("image", image));


            JSONObject json = jsonParser.makeHttpRequest(URL2, "POST", params);

            return json;

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            try {

                JSONObject jo = result.getJSONObject("data");
                if (result != null) {
                    String aadhar = null,name = null,gender = null,dob = null,address = null;

                    try {
                        aadhar = (String) jo.getString("id");
                        name = (String) jo.getString("name");
                        gender = (String) jo.getString("gender");
                        dob = (String) jo.getString("dob");
                        address = (String) jo.getString("address");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pbar.setVisibility(View.GONE);


                    if(!name.equals("NA") && editName.length()==0)
                         editName.setText(name);
                    if(!aadhar.equals("NA") && editAadhar.length()==0)
                        editAadhar.setText(aadhar);
                    if(!gender.equals("NA") && editGender.length()==0)
                        editGender.setText(gender);
                    if(!dob.equals("NA") && editDOB.length()==0)
                        editDOB.setText(dob);
                    if(!address.equals("NA") && editAddr.length()==0)
                        editAddr.setText(address);
                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Exception", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }


        }

    }


    //Image extraction ends

    //Taking second photo begin
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            //bitmap = (Bitmap) data.getExtras().get("data");
            //imgTakenPic.setImageBitmap(bitmap);
            File imgFile = new  File(_path );
            if(imgFile.exists()){
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                if(bitmap!=null) {
                    SendImage sendimage = new SendImage();
                    sendimage.execute(bitmap);
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }
    }

    class btnTakePhotoClicker implements  Button.OnClickListener{

        @Override
        public void onClick(View view) {
            //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(intent,CAM_REQUEST);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            _path = Environment.getExternalStorageDirectory() + File.separator +"simpletext.jpg";
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile( file );

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
            intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

            startActivityForResult( intent, 0 );
        }
    }
    // Taking second photo ends
}