package com.example.ekmuthoboi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class CategoryListView  extends AppCompatActivity {

    CategoryListAdapter categoryListAdapter;
    ListView listView;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    ArrayList<CategoryList> categoryLists=new ArrayList<>();
    SharedPreferences.Editor preferencesEditor;
    EditText inputSearch;
    String userId,catName= "";
    String catId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_category_list);
        listView = findViewById(R.id.table_catlist);
        inputSearch = findViewById(R.id.search_tname);
        categoryListAdapter=new CategoryListAdapter(this,categoryLists);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences Preferences = getSharedPreferences("pref", MODE_PRIVATE);
        userId = Preferences.getString("USER_ID","");

        inputSearch.addTextChangedListener(new TextWatcher() {
            //
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                categoryListAdapter.getFilter().filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override

            public void afterTextChanged(Editable arg0) {

            }
        });

        MeterReaderDBAdapter dbAdapter=new MeterReaderDBAdapter(this);
        dbAdapter.open();
        dbAdapter.close();



        Display task = new Display(CategoryListView.this);
        task.execute();


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
            pd = ProgressDialog.show(CategoryListView.this, " Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params){
            String result = "";

            try{

                int count = 0;



                try {
                    String response = CustomHttpClientGet.execute("");//url
                    result = response.toString();


                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection!!" + e.toString());
                }

                JSONArray jsonArray = new JSONArray(result.toString());

                while (count < jsonArray.length()){

                    JSONObject JO = jsonArray.getJSONObject(count);


                    catName = JO.getString("catagories_NAME");
                    catId = JO.getString("catagories_ID");


                    CategoryList categoryList = new CategoryList(catName,catId);
                    categoryLists.add(categoryList);
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

            categoryListAdapter =new CategoryListAdapter(CategoryListView.this,categoryLists);
            listView.setAdapter(categoryListAdapter);


//            Gson gson = new Gson();
//            String json = sharedPreferences1.getString("categoryLists", null);
//            Type type = new TypeToken<ArrayList<BookList>>() {}.getType();
//            categoryLists = gson.fromJson(json, type);

            sharedPreferences = getSharedPreferences("categoryId", MODE_PRIVATE);
             preferencesEditor = sharedPreferences.edit();


             listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CategoryListView.this, AuthorListView.class);
                    intent.putExtra("catagories_ID",categoryLists.get(position).getCat_id());
                    preferencesEditor.putString("CATAGORIES_ID", categoryLists.get(position).getCat_id());
                    preferencesEditor.commit();


                    SharedPreferences sharedPreferences1 = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(categoryLists);
                    editor.putString("categoryLists", json);
                    editor.apply();

                    startActivity(intent);


                }
            });

        }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(userId!=""){
            inflater.inflate(R.menu.menu_cat_after_login, menu);}
        else{

            inflater.inflate(R.menu.menu_for_category, menu);}

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cart_btnnn){
            Intent intent = new Intent(CategoryListView.this, CartActivity.class);
            startActivity(intent);
        }
        if (id == R.id.cart_btn22){
            Intent intent = new Intent(CategoryListView.this, CartActivity.class);
            startActivity(intent);
        }
        if (id == R.id.login_btnnn1){
            Intent intent = new Intent(CategoryListView.this, Login.class);
            startActivity(intent);
        }
        if (id == R.id.logout_btttn22){
            SharedPreferences preferences = getSharedPreferences("pref", 0);
            preferences.edit().remove("USER_ID").commit();
            Intent intent = new Intent(CategoryListView.this, Login.class);
            startActivity(intent);
            MeterReaderDBAdapter dbAdapter=new MeterReaderDBAdapter(this);
            dbAdapter.open();
            dbAdapter.removeAll();
            dbAdapter.close();


        }
        if (id == R.id.book_req_btttn22){
            Intent intent = new Intent(CategoryListView.this, BookRequest.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }



}








