package com.example.ekmuthoboi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import static com.example.ekmuthoboi.CartActivity.amount;
import static com.example.ekmuthoboi.CartActivity.delvId;

public class OrderDetails extends AppCompatActivity {
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    OrderDetailAdapter orderDetailAdapter;
    ListView listView;

    ArrayList<OrderDetailModel> orderDetailModels=new ArrayList<>();
    String total,orderId,productId,productName,dollersingle,dollerTotal,proQty,singleTotal,singlePrice,dis_price="";
    MeterReaderDBAdapter dbAdapter=null;
    TextView total1,orderId1,deliveryCharge1;
    Button goToCategory;
    String userId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_list);
        listView =  findViewById(R.id.table_order_detaillist);

        total1 = (TextView)findViewById(R.id.grand_total) ;
        orderId1 = (TextView)findViewById(R.id.order_no) ;
        deliveryCharge1 = (TextView)findViewById(R.id.delivery_Charge) ;
        goToCategory = (Button) findViewById(R.id.gotoCategory) ;

        dbAdapter=new MeterReaderDBAdapter(this);
        if(delvId.equals("2")){
        deliveryCharge1.setText("$"+amount);}
        else{
            deliveryCharge1.setText(amount+" Tk.");

        }




        SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        orderId = sharedPreferences.getString("orderId", "");
        orderId1.setText(orderId);


        SharedPreferences sharedPreferences1 = getSharedPreferences("pref", Context.MODE_PRIVATE);
        total = sharedPreferences1.getString("cartPrice", "");

        if(delvId.equals("2")){
            total1.setText("$"+total);}
        else{
            total1.setText(total+" Tk.");

        }

        SharedPreferences Preferences = getSharedPreferences("pref", MODE_PRIVATE);
        userId = Preferences.getString("USER_ID","");


        dbAdapter.open();
        String[][] messageData=dbAdapter.getDataToRead();
        dbAdapter.close();
        for(int i=0;i<messageData[0].length;i++){
            OrderDetailModel order=new OrderDetailModel(messageData[0][i],messageData[1][i],messageData[2][i],messageData[3][i],
                    messageData[4][i],messageData[5][i],messageData[6][i],messageData[7][i],orderId);
            orderDetailModels.add(order);
            productId =messageData[0][i];
            productName =messageData[1][i];
            singleTotal=messageData[2][i];
            singlePrice=messageData[3][i];
            dis_price=messageData[4][i];
            dollersingle=messageData[5][i];
            dollerTotal=messageData[6][i];
            proQty=messageData[7][i];

        }

        orderDetailAdapter =new OrderDetailAdapter(OrderDetails.this,orderDetailModels);
        listView.setAdapter(orderDetailAdapter);

        Order task = new Order(OrderDetails.this);
        task.execute();

        goToCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderDetails.this,CategoryListView.class);
                startActivity(i);
                dbAdapter.open();
                dbAdapter.removeAll();
                dbAdapter.close();
            }
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
            pd = ProgressDialog.show(OrderDetails.this, "Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "0" ;
            BufferedReader reader = null;
            StringBuilder stringBuilder ;
            int count = 0;
            try {
                for(int i=0;i<orderDetailModels.size();i++){
                URL url = new URL("");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                    String c;

                    String z =orderId;
                    String a= orderDetailModels.get(i).getProductId();
                    String b = orderDetailModels.get(i).getQty();
                    if(delvId.equals("2")){
                     c =  orderDetailModels.get(i).getDollerTotal() ;}
                    else{
                         c =  orderDetailModels.get(i).getSingle_total();

                    }


                JSONObject jsonParam = new JSONObject();
                jsonParam.put("ORDER_ID", z );
                jsonParam.put("PRODUCT_ID", a);
                jsonParam.put("PRODUCT_QTY", b);
                jsonParam.put("PRODUCT_PRICE",c);
                jsonParam.put("DLV_ID",delvId);
               




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
            }
            } catch (Exception e) {
                e.printStackTrace();
            }              



            return result;
        }


            @Override
        protected void onPostExecute(String result) {

            pd.dismiss();

        }}

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
        dbAdapter.open();
        dbAdapter.removeAll();
        dbAdapter.close();


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
