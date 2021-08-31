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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.ekmuthoboi.CartActivity.delvId;

public class OrderActivity extends AppCompatActivity {

    EditText name,address,city,zip,email,phone,total_price;
    Button order;
    Spinner paymentTypes;
    String name1,add1,city1,zip1,email1,phone1,price1,paystatus,orderStatus,userId,payMentType,paymenttype2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        total_price = findViewById(R.id.total);
        city = findViewById(R.id.city);
        zip = findViewById(R.id.zip);
        paymentTypes = findViewById(R.id.payment_type);
        order = findViewById(R.id.btn_orderConfirm);

        SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "");

        SharedPreferences Preferences = getSharedPreferences("pref", MODE_PRIVATE);
        price1 = Preferences.getString("cartPrice","");

        final ArrayList<String> strPay_type=new ArrayList<>();
        strPay_type.add("Select payment Type");
        strPay_type.add("Credit Card");
        strPay_type.add("Bkash");
        strPay_type.add("Cash on delivery");


        final ArrayList<String> strPay_type1=new ArrayList<>();
        strPay_type1.add("Paypal");

        if(delvId.equals("2")){
        ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,strPay_type1);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTypes.setAdapter(aa1);

        paymentTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payMentType= strPay_type1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });}
        else{
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,strPay_type);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            paymentTypes.setAdapter(aa);

            paymentTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    payMentType= strPay_type.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }






        total_price.setText(price1);


        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                name1=name.getText().toString().trim();
                add1=address.getText().toString().trim();
                email1=email.getText().toString().trim();
                phone1=phone.getText().toString().trim();
                city1=city.getText().toString().trim();
                zip1=zip.getText().toString().trim();
                paystatus ="P";
                orderStatus="P";




                if (name1.isEmpty() || add1.isEmpty() ||  email1.isEmpty()||  phone1.isEmpty() || city1.isEmpty() || zip1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill The Empty Field", Toast.LENGTH_LONG).show();

                }
                else{

                    Order task = new Order(OrderActivity.this);
                    task.execute();
                }}
        });
    }



    private class Order extends AsyncTask<Void,Void,String> {


        private Activity context;

        @SuppressWarnings("unused")
        ProgressDialog pd=null;

        public Order(Activity context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(OrderActivity.this, "Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params) {
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
                jsonParam.put("NAME",name1 );
                jsonParam.put("ADDRESS", add1);
                jsonParam.put("CITY", city1);
                jsonParam.put("ZIP", zip1);
                jsonParam.put("EMAIL", email1);
                jsonParam.put("PHONE", phone1);
                jsonParam.put("PAYMENT_TYPE", payMentType);
                jsonParam.put("TOTAL_PRICE",price1);
                jsonParam.put("PAYMENT_STATUS", paystatus);
                jsonParam.put("ORDER_STATUS", orderStatus);
                jsonParam.put("USER_ID", userId);




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

            int orderId= Integer.parseInt(result);
            if(orderId>0){

                SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("orderId", String.valueOf(orderId));
                editor.apply();

                dialog("","Order successful!!!");
                startActivity(new Intent(OrderActivity.this,OrderDetails.class));
            }else{

                dialog1("Failed! try again");
            }


        }
    }


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }






    public void dialog(String message,String title){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(title);



                        startActivity(new Intent(OrderActivity.this,BookRequest.class));
                        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void dialog1(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
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

