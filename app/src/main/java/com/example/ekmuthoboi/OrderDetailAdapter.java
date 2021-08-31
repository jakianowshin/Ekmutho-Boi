package com.example.ekmuthoboi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.ekmuthoboi.CartActivity.delvId;

public class OrderDetailAdapter extends ArrayAdapter<OrderDetailModel> {

    private List<OrderDetailModel> list;

    private LayoutInflater inflator;
    public OrderDetailAdapter(Activity context, List<OrderDetailModel> list) {
        super(context, R.layout.activity_order_details, list);
        this.list = list;
        inflator = context.getLayoutInflater();
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        OrderDetailHoldder orderDetailHoldder;

        if(convertView == null){

            convertView= inflator.inflate(R.layout.activity_order_details,null);
            orderDetailHoldder = new OrderDetailHoldder();


            orderDetailHoldder.prodctId = convertView.findViewById(R.id.pro_id);
            orderDetailHoldder.price = convertView.findViewById(R.id.singel_price);
            orderDetailHoldder.qty = convertView.findViewById(R.id.pro_qty);
            orderDetailHoldder.prodctName = convertView.findViewById(R.id.pro_name);
            orderDetailHoldder.TotalPrice = convertView.findViewById(R.id.pro_total);


            convertView.setTag(orderDetailHoldder);


            convertView.setTag(R.id.pro_id, orderDetailHoldder.prodctId);
            convertView.setTag(R.id.singel_price, orderDetailHoldder.price);
            convertView.setTag(R.id.pro_qty, orderDetailHoldder.qty);
            convertView.setTag(R.id.pro_name, orderDetailHoldder.prodctName);
            convertView.setTag(R.id.pro_total, orderDetailHoldder.TotalPrice);

        }

        else{
            orderDetailHoldder = (OrderDetailHoldder) convertView.getTag();
        }


        orderDetailHoldder.prodctId.setText(list.get(position).getProductId());
        orderDetailHoldder.qty.setText(list.get(position).getQty());
        orderDetailHoldder.prodctName.setText(list.get(position).getProductName());
        if(delvId.equals("2")){

        orderDetailHoldder.price.setText(list.get(position).getDollerprice());


        orderDetailHoldder.TotalPrice.setText(list.get(position).getDollerTotal());}
        else{
            if(Double.parseDouble(list.get(position).getDis_price())>0){
                orderDetailHoldder.price.setText(list.get(position).getDis_price());
            }
            else {
                orderDetailHoldder.price.setText(list.get(position).getSingle_price());

            }
            orderDetailHoldder.TotalPrice.setText(list.get(position).getSingle_total());

        }

        return convertView;
    }


    static class OrderDetailHoldder{


        private EditText prodctId;
        private EditText qty;
        private EditText price;
        private EditText prodctName;
        private EditText TotalPrice;

    }


}
