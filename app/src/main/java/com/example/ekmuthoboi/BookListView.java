package com.example.ekmuthoboi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookListView extends AppCompatActivity {

    BookListAdapter bookListAdapter ;
    ListView listView;
    public static TextView tv;
    public Toolbar toolbar;

     ArrayList<BookList> bookLists=new ArrayList<>();

    EditText inputSearch;

    SharedPreferences sharedPreferences;
    String strLoction="";
    String userId,proName,proPrice,proImage,imageUrL= "";
    String proId="",subCatId="";


    Bundle bundle=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist);
        listView = findViewById(R.id.table_booklist);
        inputSearch = findViewById(R.id.search_tname);
        bookListAdapter=new BookListAdapter(this,bookLists);


        SharedPreferences Preferences = getSharedPreferences("pref", MODE_PRIVATE);
        userId = Preferences.getString("USER_ID","");


        sharedPreferences = getSharedPreferences("categoryId", Context.MODE_PRIVATE);
        strLoction = sharedPreferences.getString("CATAGORIES_ID", "");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputSearch.addTextChangedListener(new TextWatcher() {
//
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                bookListAdapter.getFilter().filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override

            public void afterTextChanged(Editable arg0) {

            }
        });


        bundle = getIntent().getExtras();
        if(bundle!=null){
            subCatId = bundle.getString("sub_CATAGORIES_ID");
            Display task = new Display(BookListView.this);
            task.execute();
        }
//
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
            pd = ProgressDialog.show(BookListView.this, " Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params){

            final List<Pair<String, String>> postParameters = new ArrayList<>();
            postParameters.add(new Pair("strLocation", strLoction));

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


                    proName = JO.getString("product_NAME");
                    proId = JO.getString("product_ID");
                    proImage = JO.getString("product_IMAGE");
                    proPrice = JO.getString("product_MRP");

                    proImage= proImage.substring(2);
                    imageUrL="http://103.123.11.173/boi/"+proImage;





                    BookList bookList = new BookList(proId,proName,proPrice,imageUrL);
                    bookLists.add(bookList);
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



            bookListAdapter =new BookListAdapter(BookListView.this,bookLists);
            listView.setAdapter(bookListAdapter);



            SharedPreferences sharedPreferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(bookLists);
            editor.putString("booklists", json);
            editor.apply();


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(BookListView.this, SingleBook.class);
                            intent.putExtra("product_ID",bookLists.get(position).getProductId());
                            startActivity(intent);


                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(userId!=""){
            inflater.inflate(R.menu.menu_for_all, menu);}
        else{

            inflater.inflate(R.menu.menu_confirmation, menu);}

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cat_butt){
            Intent intent = new Intent(BookListView.this, CategoryListView.class);
            startActivity(intent);
        }
        if (id == R.id.cart_btn){
            Intent intent = new Intent(BookListView.this, CartActivity.class);
            startActivity(intent);
        }
        if (id == R.id.login_btn1){
            Intent intent = new Intent(BookListView.this, Login.class);
            startActivity(intent);
        }
        if (id == R.id.logout_btttn){
            SharedPreferences preferences = getSharedPreferences("pref", 0);
            preferences.edit().remove("USER_ID").commit();
            Intent intent = new Intent(BookListView.this, Login.class);
            startActivity(intent);
            MeterReaderDBAdapter dbAdapter=new MeterReaderDBAdapter(this);
            dbAdapter.open();
            dbAdapter.removeAll();
            dbAdapter.close();

        }
        if (id == R.id.book_req_btttn){
            Intent intent = new Intent(BookListView.this, BookRequest.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }




}
