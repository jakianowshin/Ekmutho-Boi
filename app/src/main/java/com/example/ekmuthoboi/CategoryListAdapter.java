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

public class CategoryListAdapter extends BaseAdapter {

    private ArrayList<CategoryList> list;
    Button togoPackage;
    Context context;

    private LayoutInflater inflator;
    private ArrayList<CategoryList> newlist = null;
    private ItemFilter mFilter = new ItemFilter();
    public CategoryListAdapter(Context applicationContext, ArrayList<CategoryList> newlist) {
       this.context=applicationContext;
        this.newlist = newlist;
        this.list = new ArrayList<CategoryList>();
        this.list.addAll(newlist);


    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CategoryList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CategoryNameHolder categoryNameHolder;

        if(convertView == null){
            inflator = (LayoutInflater.from(context));


            convertView= inflator.inflate(R.layout.row_category_list,null);
            categoryNameHolder = new CategoryNameHolder();

            categoryNameHolder.tx_category_name = (TextView)convertView.findViewById(R.id.cat_name);

            convertView.setTag(categoryNameHolder);
            //convertView.setTag(R.id.cat_name, categoryNameHolder.tx_category_name);


        }

        else{
            categoryNameHolder = (CategoryNameHolder)convertView.getTag();

        }


        //BookHistory bookHistory = (BookHistory)this.getItem(position);


        categoryNameHolder.tx_category_name.setText(String.valueOf(list.get(position).getCat_name()));


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

            final ArrayList<CategoryList> orglist = newlist;

            int count = orglist.size();
            final ArrayList<CategoryList> nlist = new ArrayList<CategoryList>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = orglist.get(i).getCat_name();



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
            list = (ArrayList<CategoryList>) results.values;
            notifyDataSetChanged();
        }

    }

    private class CategoryNameHolder{

      TextView tx_category_name;



    }


}
