package com.example.ekmuthoboi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AuthorListAdapter  extends BaseAdapter {

    private ArrayList<AuthorList> list;
    Context context;
    Button togoPackage;

    private LayoutInflater inflator;
    private ArrayList<AuthorList> newlist = null;
    private ItemFilter mFilter = new ItemFilter();
    public AuthorListAdapter(Context context, ArrayList<AuthorList> newlist) {
        this.context=context;
       // super(context, R.layout.row_author_list, newlist);
        this.newlist = newlist;
        //inflator = context.getLayoutInflater();
        this.list = new ArrayList<AuthorList>();
        this.list.addAll(newlist);

    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AuthorList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AuthorNameHolder authorNameHolder;

        if(convertView == null){

            inflator=(LayoutInflater.from(context));
            convertView= inflator.inflate(R.layout.row_author_list,null);
            authorNameHolder = new AuthorNameHolder();

            authorNameHolder.tx_author_name = (TextView)convertView.findViewById(R.id.author_name);

            convertView.setTag(authorNameHolder);
            //convertView.setTag(R.id.author_name, authorNameHolder.tx_author_name);

        }

        else{
            authorNameHolder = (AuthorNameHolder) convertView.getTag();

        }


        //BookHistory bookHistory = (BookHistory)this.getItem(position);


        authorNameHolder.tx_author_name.setText(String.valueOf(list.get(position).getAuthorName()));

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

            final ArrayList<AuthorList> orglist = newlist;

            int count = orglist.size();
            final ArrayList<AuthorList> nlist = new ArrayList<AuthorList>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = orglist.get(i).getAuthorName();



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
            list = (ArrayList<AuthorList>) results.values;
            notifyDataSetChanged();
        }

    }

    private class AuthorNameHolder{

        TextView tx_author_name;



    }

}
