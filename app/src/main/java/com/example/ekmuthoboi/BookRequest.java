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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BookRequest extends AppCompatActivity {
    EditText bName,mb1,mb2,email,date,catName;
    Button request;
    String str_bnmae,str_mb1,str_mb2,str_email,str_date,userId,strCon_type1,strCat_type1,CatNameStr;
    Spinner CatType,ConType;
    private  ArrayList<CategoryList> categoryList;
    ArrayList<String> strCat_id=new ArrayList<>();
    ArrayList<String> strCat_name=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);


        email = findViewById(R.id.email);
        bName = findViewById(R.id.booknameR);
        mb1 = findViewById(R.id.mb1);
        mb2 = findViewById(R.id.mb2);
        request = findViewById(R.id.request);
        catName = findViewById(R.id.insertCatName);
        CatType=findViewById(R.id.cat_type);
        ConType=findViewById(R.id.con_type);

         SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");



         SharedPreferences sharedPreferences1 = getSharedPreferences("pref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences1.getString("categoryLists", null);
        Type type = new TypeToken<ArrayList<CategoryList>>() {}.getType();
        categoryList = gson.fromJson(json, type);


        final ArrayList<String> strCon_type=new ArrayList<>();
        strCon_type.add("Select Contact Type");
        strCon_type.add("via Email");
        strCon_type.add("via Phone");

       for(int i=0;i<categoryList.size();i++){
           strCat_id.add(categoryList.get(i).getCat_id());
           strCat_name.add(categoryList.get(i).getCat_name());
       }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,strCat_name);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CatType.setAdapter(aa);

        CatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCat_type1= strCat_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,strCon_type);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ConType.setAdapter(aa1);

        ConType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCon_type1= strCon_type.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        request.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                str_email=email.getText().toString().trim();
                str_bnmae=bName.getText().toString().trim();
                str_mb1=mb1.getText().toString().trim();
                str_mb2=mb2.getText().toString().trim();
                str_date=date.getText().toString().trim();
                CatNameStr=catName.getText().toString().trim();


                if (str_bnmae.isEmpty() || str_mb1.isEmpty() ||  str_email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill The Empty Field", Toast.LENGTH_LONG).show();

                }
                else{

                   BookReq task = new BookReq(BookRequest.this);
                    task.execute();
                }
            Intent i = new Intent(BookRequest.this,CategoryListView.class);
                startActivity(i);
            }
        });
    }

    private class BookReq extends AsyncTask<Void,Void,String> {
        private Activity context;

        @SuppressWarnings("unused")
        ProgressDialog pd=null;

        public BookReq(Activity context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(BookRequest.this, " Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "0" ;
            BufferedReader reader = null;
            StringBuilder stringBuilder ;

            try {
                URL url = new URL("");//URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("USER_ID",userId );
                jsonParam.put("CATAGORIES_ID",strCat_type1 );
                jsonParam.put("CATAGORIES_NAME",CatNameStr );
                jsonParam.put("BOOK_NAME",str_bnmae );
                jsonParam.put("CON_MOBILE1", str_mb1);
                jsonParam.put("CON_MOBILE2", str_mb2);
                jsonParam.put("EMAIL", str_email);
                jsonParam.put("CON_TYPE", strCon_type1);
               // jsonParam.put("REQ_DATE", str_date);




            

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

                dialog("Message","Request successful!!!");
                startActivity(new Intent(BookRequest.this,Login.class));
            }else{

                dialog1("Failed! try again");
            }
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
                        startActivity(new Intent(BookRequest.this,Login.class));
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
}