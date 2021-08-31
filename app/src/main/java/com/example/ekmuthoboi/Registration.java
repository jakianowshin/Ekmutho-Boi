package com.example.ekmuthoboi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Registration extends AppCompatActivity {


    String json_string;



    EditText email, pass, userName, phone_num ;
    Button signup;
    TextView header;
    String strEmail, strPass,strUname,strPhone;

    @SuppressLint("WrongThread")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Registration Form
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        userName = findViewById(R.id.username);
        phone_num = findViewById(R.id.phone);
        signup = findViewById(R.id.btn_signup);
        header = findViewById(R.id.txtreg);

        header.setTypeface(ResourcesCompat.getFont(this, R.font.kaushan_script));
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkDataEntered();

                strEmail=email.getText().toString().trim();
                strPass=pass.getText().toString().trim();
                strUname=userName.getText().toString().trim();
                strPhone=phone_num.getText().toString().trim();


                if (strEmail.isEmpty() || strUname.isEmpty() ||  strPass.isEmpty()||  strPhone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill The Empty Field", Toast.LENGTH_LONG).show();

                }
                else{

                Signup task = new Signup(Registration.this);
                task.execute();
            }}
        });
    }

    private class Signup extends AsyncTask<Void,Void,String> {


        private Activity context;

        @SuppressWarnings("unused")
        ProgressDialog pd=null;

        public Signup(Activity context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Registration.this, "Signup Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params) {
                  /*final List<Pair<String, String >> postParameters = new ArrayList<>();
                  postParameters.add (new Pair("id", LogEmail.getText().toString().trim()));
                  postParameters.add (new Pair("pass", LogPassword.getText().toString().trim()));*/

            String result = "0" ;
            BufferedReader reader = null;
            StringBuilder stringBuilder ;

            try {
                URL url = new URL("");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("USER_EMAIL",strEmail );
                jsonParam.put("USER_PASSWORD", strPass);
                jsonParam.put("USER_NAME", strUname);
                jsonParam.put("USER_MOBILE", strPhone);


                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line + "\n");
                }
                result=stringBuilder.toString();
                result=result.replaceAll("\n","");
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            pd.dismiss();
            int r= Integer.parseInt(result);
            if(r>0){

                dialog("Message","registration successful!!!");
                startActivity(new Intent(Registration.this,Login.class));
            }else{

                dialog1("Failed! try again");
            }
        }
    }



    //Registration Validation
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    private void checkDataEntered() {

        if (isEmpty(userName)) {
            Toast t = Toast.makeText(this, "Enter User Name To Sign Up", Toast.LENGTH_SHORT);
            t.show();
        }

        if (isEmail(email) == false){
            email.setError("Enter A Valid Email!");
        }

        if (isEmpty(phone_num)) {
            Toast t = Toast.makeText(this, "Enter Phone Number To Sign Up", Toast.LENGTH_SHORT);
            t.show();
        }
        if (isEmpty(pass) == false){
            email.setError("Enter A password!");
        }

    }



    public void dialog(String message,String title){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(Registration.this,Login.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void dialog1(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
