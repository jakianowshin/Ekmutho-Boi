   package com.example.ekmuthoboi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;




import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;




public class SingleBook extends AppCompatActivity {

    String bName1,author1,catName1,sDescription1,total1,dis_price1,qty1,doller1,image1,imageUrL,detailDescription=" ";

    public Toolbar toolbar;
    String setnewPrice,setPriceDoller = "";
    String productId,cartBookId="";
    String setQuantity="";
    Bundle bundle = null;
    MeterReaderDBAdapter dbAdapter=null;
    TextView bName,catName,author,sDescription,total,dis_price,stock,quantity,doller;
    Button detailDes,addCart;
    ImageButton addbutton,minusbutton;
    ImageView image;
    Bitmap myBitmap;
    String userId = " ";
    public static TextView tv;
     int q=0;
    

    private static ArrayList<BookList> bookLists;
    private static ArrayList<BookList>books;
    private static ArrayList<CartList>cartBooks;
    private static ArrayList<CartList>cartBooks1;
    BookList book = new BookList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);
        dbAdapter=new MeterReaderDBAdapter(this);

        SharedPreferences Preferences = getSharedPreferences("pref", MODE_PRIVATE);
        userId = Preferences.getString("USER_ID","");


        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("books", null);
        Type type = new TypeToken<ArrayList<BookList>>() {}.getType();
        books = gson.fromJson(json, type);
        if (books == null) {
            books = new ArrayList<>();
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setTitle(" Book Details");




        if (bookLists == null) {

            bookLists = new ArrayList<>();
        }
        image=(ImageView) findViewById(R.id.book_image);
        bName = (TextView) findViewById(R.id.bName);
        catName = (TextView) findViewById(R.id.catName);
        author = (TextView)findViewById(R.id.subCat);
        sDescription = (TextView)findViewById(R.id.sDiscription);
        total = (TextView)findViewById(R.id.totaltaka);
        dis_price = (TextView)findViewById(R.id.dis_price);
        stock = (TextView)findViewById(R.id.stock);
        detailDes = (Button) findViewById(R.id.detail_btn);
        addCart = (Button) findViewById(R.id.addCart);
        quantity = (TextView) findViewById(R.id.quantity);
        addbutton = (ImageButton) findViewById(R.id.plus);
        minusbutton = (ImageButton) findViewById(R.id.subquantity);
        doller = (TextView) findViewById(R.id.totaldoller);

        bundle = getIntent().getExtras();
        if(bundle!=null) {
            productId = bundle.getString("product_ID");

            SharedPreferences shared1 = getSharedPreferences("pref", MODE_PRIVATE);
            Gson gson2 = new Gson();
            String json2 = shared1.getString("books", null);
            Type type2 = new TypeToken<ArrayList<CartList>>() {}.getType();
            cartBooks1 = gson2.fromJson(json2, type2);
            if (cartBooks1 == null) {
                cartBooks1 = new ArrayList<>();
            }
            for(int i=0;i<cartBooks1.size();i++){
                if(cartBooks1.get(i).getBookId().equals(productId)){
                    q=Integer.parseInt(cartBooks1.get(i).getQuantity());
                    quantity.setText(String.valueOf(q));


                }}


            Display task = new Display(SingleBook.this);
            task.execute();

            addbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                if(Double.parseDouble(dis_price1)>0){
                    double baseprice1 = Double.parseDouble(dis_price1);
                    int  baseDoller = Integer.parseInt(doller1);
                    q++;
                    quantity.setText(String.valueOf(q));
                    setQuantity=String.valueOf(q);
                    double bookPrice = baseprice1 * q;
                    int bookPriceDoller = baseDoller * q;
                    setPriceDoller = String.valueOf(bookPriceDoller);
                    doller.setText("$"+setPriceDoller);
                    setnewPrice = String.valueOf(bookPrice);
                     dis_price.setText(" Tk. "+setnewPrice);
                }



                 else{
                    int baseprice = Integer.parseInt(total1);
                    int  baseDoller = Integer.parseInt(doller1);
                    q++;
                    quantity.setText(String.valueOf(q));
                    setQuantity=String.valueOf(q);
                    int bookPrice = baseprice * q;
                    int bookPriceDoller = baseDoller * q;
                    setPriceDoller = String.valueOf(bookPriceDoller);
                    doller.setText("$"+setPriceDoller);
                    setnewPrice = String.valueOf(bookPrice);
                    total.setText(" Tk. "+ setnewPrice);}

                }
            });


            minusbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (q == 0) {
                        Toast.makeText(SingleBook.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                    } else {
                        if(Double.parseDouble(dis_price1)>0){
                            double baseprice1 = Double.parseDouble(dis_price1);
                            int  baseDoller = Integer.parseInt(doller1);
                        q--;
                        setQuantity=String.valueOf(q);
                        quantity.setText(String.valueOf(q));
                            double bookPrice = baseprice1 * q;
                            int bookPriceDoller = baseDoller * q;
                            setPriceDoller = String.valueOf(bookPriceDoller);
                            doller.setText("$"+setPriceDoller);
                         setnewPrice = String.valueOf(bookPrice);
                        dis_price.setText("Tk. "+setnewPrice);

                    }
                    else{
                        int basePrice = Integer.parseInt(total1);
                            int  baseDoller = Integer.parseInt(doller1);
                        q--;
                        setQuantity=String.valueOf(q);
                        quantity.setText(String.valueOf(q));
                        int bookPrice = basePrice * q;
                            int bookPriceDoller = baseDoller * q;
                            setPriceDoller = String.valueOf(bookPriceDoller);
                            doller.setText("$"+setPriceDoller);
                        setnewPrice = String.valueOf(bookPrice);
                        total.setText("Tk. "+setnewPrice);
                    }}
                }
            });

            SharedPreferences sharedPreferences1 = getSharedPreferences("total", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("total",setnewPrice);
            editor.apply();


            addCart.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(q>0 ){
                    SaveCart();
                    SharedPreferences shared = getSharedPreferences("pref", MODE_PRIVATE);
                    Gson gson1 = new Gson();
                    String json1 = shared.getString("books", null);
                    Type type1 = new TypeToken<ArrayList<CartList>>() {}.getType();
                    cartBooks = gson1.fromJson(json1, type1);
                    if (cartBooks == null) {
                        cartBooks = new ArrayList<>();
                    }

                   

                    for(int i=0;i<cartBooks.size();i++){
                        if(cartBooks.get(i).getBookId().equals(productId)){
                            updateCart(cartBooks.get(i).getBookId(),quantity.getText().toString(),setnewPrice);

                        }
                    }
                    Intent intent = new Intent(SingleBook.this,CartActivity.class);
                    startActivity(intent);

                }

                else{
                    Toast.makeText(SingleBook.this, "Please enter the book quantity", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


   }

    protected void OnResume(){
        super.onResume();
    }


    private void SaveCart(){

        try {
                    dbAdapter.open();
                    dbAdapter.insertBook(productId,bName1,setnewPrice,total1,dis_price1,doller1,setPriceDoller,setQuantity,userId);
                    dbAdapter.close();

                }catch (Exception e){
                    e.printStackTrace();
                }

    }

    private void updateCart(String productId,String setQuantity,String setnewPrice){

        try {
            dbAdapter.open();
            dbAdapter.updateAll(productId,setQuantity,setnewPrice,setPriceDoller);
            dbAdapter.close();


        }catch (Exception e){
            e.printStackTrace();
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
            pd = ProgressDialog.show(SingleBook.this, " Processing",
                    "Please wait...");
        }

        @Override
        protected String doInBackground(Void... params) {


            String result = "";

            try {

                int count = 0;


                try {
                    String response = CustomHttpClientGet.execute("");
                    result = response.toString();
                    //result=result.replaceAll("[^a-zA-Z0-9]+","");

                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection!!" + e.toString());
                }

                JSONArray jsonArray = new JSONArray(result.toString());

                while (count < jsonArray.length()){

                JSONObject JO = jsonArray.getJSONObject(count);


                sDescription1 = JO.getString("product_SHORT_DESC");
                total1 = JO.getString("product_MRP");
                dis_price1 = JO.getString("disc_PRICE");
                qty1 = JO.getString("product_QTY");
                catName1 = JO.getString("catagories_NAME");
                bName1 = JO.getString("product_NAME");
                author1 = JO.getString("sub_CATAGORIES_NAME");
                image1 = JO.getString("product_IMAGE");
                detailDescription= JO.getString("product_DESC");
                doller1= JO.getString("rat_DOLLAR");



                image1= image1.substring(2);
                imageUrL=""+image1;//url for getting image 


                bName.setText(bName1);
                catName.setText(catName1+":");
                author.setText(author1);
                sDescription.setText(sDescription1);
                total.setText("Tk. "+total1);
                dis_price.setText("Tk. "+dis_price1 );
                stock.setText(qty1);
                doller.setText("$"+doller1);
                loadimage(image);


                book.setPrice(dis_price1);
                book.setProductName(bName1);

                count++;



            }

            }catch (Exception e) {
                Log.e("log_tag", "Error in http connection!!" + e.toString());
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();

            SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("book_price",total1);
            editor.apply();

       

            SharedPreferences sharedPreferences2 = getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString("dis_price1",dis_price1);
            editor2.apply();

            detailDes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SingleBook.this, BookDescription.class);
                    i.putExtra("product_DESC", detailDescription);
                    startActivity(i);
                }
            });
        }

    }


    public void loadimage(View view)
    {
        class ImageLoadTask extends AsyncTask<Void, Void, Bitmap>
        {


            private String url;
            private ImageView imageView;

            public ImageLoadTask(String url, ImageView imageView) {
                this.url = url;
                this.imageView = imageView;
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    URL connection = new URL(url);

                    InputStream input = connection.openStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                    Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 1000, 400, true);
                    return resized;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
            }

        }

        ImageLoadTask obj=new ImageLoadTask(imageUrL,image);
        obj.execute();
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
            Intent intent = new Intent(SingleBook.this, CategoryListView.class);
            startActivity(intent);
        }
        if (id == R.id.cart_btn){
            Intent intent = new Intent(SingleBook.this, CartActivity.class);
            startActivity(intent);
        }
        if (id == R.id.login_btn1){
            Intent intent = new Intent(SingleBook.this, Login.class);
            startActivity(intent);
        }
        if (id == R.id.logout_btttn){
            Intent intent = new Intent(SingleBook.this, Login.class);
            startActivity(intent);
        }

        if (id == R.id.book_req_btttn){
            Intent intent = new Intent(SingleBook.this, BookRequest.class);
            startActivity(intent);
        }
        


        return super.onOptionsItemSelected(item);
    }

    public  void cartUpdate() {
        if (tv != null && books != null)
            tv.setText(Integer.toString(books.size()));
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










