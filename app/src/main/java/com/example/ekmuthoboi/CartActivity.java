 package com.example.ekmuthoboi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;





public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerViewCart;
    public static TextView tv;
    static TextView cartPrice;
    Button checkout;
   static double TotalwithDel = 0.0;
    String bookId,userId,qty,deliveryId,deliveryType = "";
    static  String deliveryType1,delvId,amount,Total="";

    SharedPreferences sharedPreferences;

    MeterReaderDBAdapter dbAdapter=null;

    Spinner delDetail;
    private ArrayList<Delivery> deliveries=new ArrayList<>();
    ArrayList<String> delId=new ArrayList<>();
     ArrayList<String> delType=new ArrayList<>();
    ArrayList<String> delAmount=new ArrayList<>();

   public  ArrayList<CartList> books=new ArrayList<>();
   static   ArrayList<Delivery> totalDEl=new ArrayList<>();
   static ArrayList delType1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbAdapter=new MeterReaderDBAdapter(this);


        deliveryId="0";
        deliveryType="Select an option";
        amount="0";
        Delivery delivery1 = new Delivery(deliveryId,deliveryType,amount);
        deliveries.add(delivery1);

       // delType.add("Select an option");

      Display task = new Display(CartActivity.this);
      task.execute();


        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Your Cart");


        mToolbar.setTitleTextColor(0xFFFFFFFF);



//        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               Intent i = new Intent(CartActivity.this,CategoryListView.class);
//               startActivity(i);
//            }
//        });


        if (books == null) {

            books = new ArrayList<>();
        }

        cartPrice = findViewById(R.id.cart_price);
        checkout = findViewById(R.id.checkOut);
        recyclerViewCart = findViewById(R.id.cart_recycleView);
        delDetail=findViewById(R.id.del_spinner);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.setNestedScrollingEnabled(false);
        recyclerViewCart.setAdapter(new CartAdapter(getApplicationContext(),books));

        SharedPreferences Preferences = getSharedPreferences("pref", MODE_PRIVATE);
         userId = Preferences.getString("USER_ID","");



        dbAdapter.open();
        String[][] messageData=dbAdapter.getDataToRead();
        dbAdapter.close();

        for(int i=0;i<messageData[0].length;i++){
            CartList cart=new CartList(messageData[0][i],messageData[1][i],messageData[2][i],messageData[6][i],messageData[7][i],userId);
            books.add(cart);
            bookId =messageData[0][i];
            qty=messageData[7][i];
        }

        CartAdapter cartAdapter=new CartAdapter( CartActivity.this,books);
        recyclerViewCart.setAdapter(cartAdapter);


        SharedPreferences sharedPreferences1 = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        Gson gson = new Gson();
        String json = gson.toJson(books);
        editor1.putString("books", json);
        editor1.apply();


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId != "" && Integer.parseInt(amount)!=0 && books.size()!=0){
                    Intent i = new Intent(CartActivity.this,OrderActivity.class);
                    startActivity(i);

                }
                else {

                     if (books.size()==0){
                        Toast.makeText(CartActivity.this,"Please order book to checkout",Toast.LENGTH_SHORT).show();
                    }

                    else if (userId==""){
                        Toast.makeText(CartActivity.this,"Please Login to checkout",Toast.LENGTH_SHORT).show();
                    }
                     else if(Integer.parseInt(amount)==0 ){

                         Toast.makeText(CartActivity.this,"Please select a delivery option",Toast.LENGTH_SHORT).show();
                     }
                    else{
                        Toast.makeText(CartActivity.this,"Sorry! try again properly!",Toast.LENGTH_SHORT).show();
                    }
                }

                }

        });



    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        if (userId != "") {
            getMenuInflater().inflate(R.menu.menu_after_login, menu);
        }


 else{
        getMenuInflater().inflate(R.menu.menu_main, menu);
 }


        MenuItem item = menu.findItem(R.id.action_addcart);
        MenuItemCompat.setActionView(item, R.layout.cart_count_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        View view = notifCount.findViewById(R.id.hotlist_bell);

        tv = notifCount.findViewById(R.id.hotlist_hot);
        cartUpdate();
        if (tv != null && books != null)
            tv.setText(Integer.toString(books.size()));

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.Login_button){
            Intent intent = new Intent(CartActivity.this, Login.class);
            startActivity(intent);
        }

        if (id == R.id.category_button){
            Intent intent = new Intent(CartActivity.this, CategoryListView.class);
            startActivity(intent);
        }

        if (id == R.id.logout){
            SharedPreferences preferences = getSharedPreferences("pref", 0);
            preferences.edit().remove("USER_ID").commit();
            Intent intent = new Intent(CartActivity.this, Login.class);
            startActivity(intent);
            MeterReaderDBAdapter dbAdapter=new MeterReaderDBAdapter(this);
            dbAdapter.open();
            dbAdapter.removeAll();
            dbAdapter.close();

        }

        if (id == R.id.cat_butt){
            Intent intent = new Intent(CartActivity.this, CategoryListView.class);
            startActivity(intent);
        }

        if (id == R.id.book_req_btn){
            Intent intent = new Intent(CartActivity.this, BookRequest.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }



    public  void cartUpdate() {
        if (tv != null && books != null)
            tv.setText(Integer.toString(books.size()));
    }

    public  void priceAdjust(){
        cartPrice.setText(Double.toString(grandTotal(books)));
    }


    public class Display extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("unused")
        private Activity context;

        @SuppressWarnings("unused")
        ProgressDialog pd=null;

        public Display(Activity context){
            this.context = context;
        }

        @Override
        protected void onPreExecute(){
            pd = ProgressDialog.show(CartActivity.this, " Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params){
            String result = "";

            try{

                int count = 0;



                try {
                    String response = CustomHttpClientGet.execute("");//URL
                    result = response.toString();


                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection!!" + e.toString());
                }

                JSONArray jsonArray = new JSONArray(result.toString());

                while (count < jsonArray.length()){

                    JSONObject JO = jsonArray.getJSONObject(count);


                    deliveryId = JO.getString("dlv_ID");
                    amount = JO.getString("dlv_AMT");
                    deliveryType = JO.getString("dlv_NAME");




                     Delivery delivery = new Delivery(deliveryId,deliveryType,amount);
                    deliveries.add(delivery);
                    count++;
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


            for(int i=0;i<deliveries.size();i++){
                delId.add(deliveries.get(i).getDelId());
                delAmount.add(deliveries.get(i).getDelAmount());
                delType.add(deliveries.get(i).getDelType());

            }
            totalDEl.addAll(deliveries);
            delType1.addAll(delType);
            ArrayAdapter aa = new ArrayAdapter(CartActivity.this,android.R.layout.simple_spinner_item,delType);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            delDetail.setAdapter(aa);

            delDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    deliveryType= delType.get(position);
                    deliveryType1=delType.get(position);
                    amount = deliveries.get(position).getDelAmount();
                    delvId=deliveries.get(position).getDelId();


                    if(delvId.equals("2")){
                        TotalwithDel=Double.parseDouble(amount)+grandTotalDoller(books);
                        cartPrice.setText("$"+Double.toString(TotalwithDel));
                        Total = String.valueOf(TotalwithDel);}
                    else {
                        TotalwithDel=Double.parseDouble(amount)+grandTotal(books);
                    cartPrice.setText(Double.toString(TotalwithDel)+ " Tk.");
                    Total = String.valueOf(TotalwithDel);}


//
//
                    SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("cartPrice", Total);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }}



    public static double grandTotal(ArrayList<CartList> books){

        double totalPrice = 0.0;
        for(int i = 0 ; i < books.size(); i++) {
            totalPrice =totalPrice+Double.parseDouble(books.get(i).getBookTotalPrice());
        }
        return totalPrice;
    }

    public static int grandTotalDoller(ArrayList<CartList> books){

        int totalPrice = 0;
        for(int i = 0 ; i < books.size(); i++) {
            totalPrice =totalPrice+Integer.parseInt(books.get(i).getBookDollerPrice());
        }
        return totalPrice;
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





