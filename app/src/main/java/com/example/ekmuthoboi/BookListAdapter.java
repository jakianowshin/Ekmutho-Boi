package com.example.ekmuthoboi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.ekmuthoboi.R.layout.*;

public class BookListAdapter extends BaseAdapter {

    private ArrayList<BookList> list;
    Button togoPackage;
    Bitmap myBitmap;
    Context context;

    private LayoutInflater inflator;
    private ArrayList<BookList> newlist = null;
    private ItemFilter mFilter = new ItemFilter();
    public BookListAdapter(Context applicationContext, ArrayList<BookList> newlist) {
      this.context = applicationContext;
        this.newlist = newlist;

        this.list = new ArrayList<BookList>();
        this.list.addAll(newlist);

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BookList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BookListHolder bookListHolder;

        if(convertView == null){

            inflator=(LayoutInflater.from(context));
            convertView= inflator.inflate(row_booklist,null);
            bookListHolder = new BookListHolder();

            bookListHolder.tx_book_name = (TextView)convertView.findViewById(R.id.bookName);
            bookListHolder.tx_book_price = (TextView)convertView.findViewById(R.id.bookPrice);
            bookListHolder.tx_book_image= (ImageView) convertView.findViewById(R.id.bimage);

            convertView.setTag(bookListHolder);


        }

        else{
            bookListHolder = (BookListHolder) convertView.getTag();

        }


        bookListHolder.tx_book_name.setText(String.valueOf(list.get(position).getProductName()));
        bookListHolder.tx_book_price.setText(String.valueOf(list.get(position).getPrice())+" Tk.");
        ImageLoadTask obj=new ImageLoadTask(list.get(position).getImage(),bookListHolder.tx_book_image);
        obj.execute();

        return convertView;

    }


    public Filter getFilter() {
        return mFilter;
    }



    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<BookList> orglist = newlist;

            int count = orglist.size();
            final ArrayList<BookList> nlist = new ArrayList<BookList>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = orglist.get(i).getProductName();


                if (filterableString.toLowerCase().contains(filterString)) {


                    nlist.add(orglist.get(i));
                }
            }


            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (ArrayList<BookList>) results.values;
            notifyDataSetChanged();
        }

    }

    private class BookListHolder{

         TextView tx_book_name;
         TextView tx_book_price;
         ImageView tx_book_image;



    }

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
                    //Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
            }

        }


    }


