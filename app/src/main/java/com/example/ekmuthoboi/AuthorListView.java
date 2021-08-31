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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AuthorListView  extends AppCompatActivity {

    AuthorListAdapter authorListAdapter;
    ListView listView;

    ArrayList<AuthorList> authorLists=new ArrayList<>();

    EditText inputSearch;

    SharedPreferences sharedPreferences;
    String strLoction="";
    String author_Name= "";
    String sub_catId="";
    String categoriesID,userId="";
    Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_author_list);
        listView = findViewById(R.id.table_authorlist);
        inputSearch = findViewById(R.id.search_tname);
        authorListAdapter=new AuthorListAdapter(this,authorLists);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences Preferences = getSharedPreferences("pref", MODE_PRIVATE);
        userId = Preferences.getString("USER_ID","");

        inputSearch.addTextChangedListener(new TextWatcher() {
            //
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                authorListAdapter.getFilter().filter(text);

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
            categoriesID = bundle.getString("catagories_ID");
            Display task = new Display(AuthorListView.this);
            task.execute();
        }

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
            pd = ProgressDialog.show(AuthorListView.this, " Processing",
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
                    String response = CustomHttpClientGet.execute("");//Put your url here.
                    result = response.toString();


                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection!!" + e.toString());
                }

                JSONArray jsonArray = new JSONArray(result.toString());

                while (count < jsonArray.length()){

                    JSONObject JO = jsonArray.getJSONObject(count);


                    author_Name = JO.getString("sub_CATAGORIES_NAME");
                    sub_catId = JO.getString("sub_CATAGORIES_ID");




                    AuthorList authorList = new AuthorList(author_Name,sub_catId);
                    authorLists.add(authorList);
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


            authorListAdapter =new AuthorListAdapter(AuthorListView.this,authorLists);
            listView.setAdapter(authorListAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(AuthorListView.this, BookListView.class);
                    intent.putExtra("sub_CATAGORIES_ID",authorLists.get(position).getSub_catId());
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
            Intent intent = new Intent(AuthorListView.this, CategoryListView.class);
            startActivity(intent);
        }
        if (id == R.id.cart_btn){
            Intent intent = new Intent(AuthorListView.this, CartActivity.class);
            startActivity(intent);
        }
        if (id == R.id.book_req_btttn){
            Intent intent = new Intent(AuthorListView.this, BookRequest.class);
            startActivity(intent);
        }
        if (id == R.id.login_btn1){
            Intent intent = new Intent(AuthorListView.this, Login.class);
            startActivity(intent);
        }
        if (id == R.id.logout_btttn){
            SharedPreferences preferences = getSharedPreferences("pref", 0);
            preferences.edit().remove("USER_ID").commit();
            Intent intent = new Intent(AuthorListView.this, Login.class);
            startActivity(intent);
            MeterReaderDBAdapter dbAdapter=new MeterReaderDBAdapter(this);
            dbAdapter.open();
            dbAdapter.removeAll();
            dbAdapter.close();

        }



        return super.onOptionsItemSelected(item);
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


