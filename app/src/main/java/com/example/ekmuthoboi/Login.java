 package com.example.ekmuthoboi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends Activity {

    TextView txt_remember, LogUser,LogPassword, itbl;
    Button btnLogin,btnSignUp2;
    CheckBox remember;
    Animation btnAnim;
    public static  String USER_PASSWORD, USER_EMAIL,USER_MOBILE,USER_ID,USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        itbl =  findViewById(R.id.itbl);
        LogUser= findViewById(R.id.logId);
        LogPassword=  findViewById(R.id.logpass);
        remember=  findViewById(R.id.remember);
        txt_remember=  findViewById(R.id.txt_remember);
        btnLogin=  findViewById(R.id.btnlogin);
        btnSignUp2=  findViewById(R.id.btnsignup2);


        itbl.setTypeface(ResourcesCompat.getFont(this, R.font.kaushan_script));
       // btnLogin.setTypeface(ResourcesCompat.getFont(this, R.font.autour_one));

        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_bounce);

//        SharedPreferences loginPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
//        String checkbox = loginPreferences.getString("remember","");
//
//        if (checkbox.equals("true")){
//            Intent intent = new Intent(Login.this,CartActivity.class);
//            startActivity(intent);
//
//        }else if (checkbox.equals("false")){
//            Toast.makeText(Login.this,"Please Login",Toast.LENGTH_LONG).show();
//
//        }

        txt_remember.setTypeface(ResourcesCompat.getFont(this, R.font.aldrich));
//
        btnLogin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (LogUser.getText().toString().isEmpty() || LogPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill The Empty Field", Toast.LENGTH_LONG).show();

                } else {
                    Fetch task = new Fetch(Login.this);
                    task.execute();
                }

            }

        });



        //Remember me
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(compoundButton.isChecked()){

                    SharedPreferences loginPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = loginPreferences.edit();
                    preferencesEditor.putString("remember", "true");
                    preferencesEditor.apply();
                    Toast.makeText(Login.this,"Checked",Toast.LENGTH_SHORT).show();

                }else if(!compoundButton.isChecked()){

                    SharedPreferences loginPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = loginPreferences.edit();
                    preferencesEditor.putString("remember", "false");
                    preferencesEditor.apply();
                    Toast.makeText(Login.this,"Unchecked",Toast.LENGTH_SHORT).show();

                }

            }
        });


        btnSignUp2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btnSignUp2.setAnimation(btnAnim);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        goToSignUp();
                    }
                },1000);
            }
        });

    }

    private class Fetch  extends AsyncTask<Void,Void,String> {


        private Activity context;

        @SuppressWarnings("unused")
        ProgressDialog pd=null;


        public Fetch(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Login.this, "Login Processing",
                    "Please wait...");

        }

        @Override
        protected String doInBackground(Void... params) {

            String result = "" ;
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
                jsonParam.put("uName", LogUser.getText().toString().trim());
                jsonParam.put("uPass", LogPassword.getText().toString().trim());

                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
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
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONArray jArray = new JSONArray(result.toString());
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    USER_PASSWORD= json_data.getString("USER_PASSWORD");
                    USER_EMAIL = json_data.getString("USER_EMAIL");
                    USER_MOBILE = json_data.getString("USER_MOBILE");
                    USER_ID = json_data.getString("USER_ID");
                    USER_NAME = json_data.getString("USER_NAME");

                }

            }
            catch (Exception e) {
                Log.e("log_tag", "Error in http connection!!" + e.toString());

            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            pd.dismiss();

            SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_ID",USER_ID);
            editor.apply();







            if(LogUser.getText().toString().trim().equals(USER_NAME)){
                if(LogPassword.getText().toString().trim().equals(USER_PASSWORD)){
                    startActivity(new Intent(Login.this,CategoryListView.class));
                } else {
                    dialog("Wrong Password");
                }
            }else {
                dialog("Wrong User Name");
            }
        }
    }



    public void dialog(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

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

    public void goToSignUp(){
        Intent intent = new Intent(this,Registration.class);
        startActivity(intent);
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
