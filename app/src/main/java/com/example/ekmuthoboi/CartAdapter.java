package com.example.ekmuthoboi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import static com.example.ekmuthoboi.CartActivity.Total;
import static com.example.ekmuthoboi.CartActivity.TotalwithDel;
import static com.example.ekmuthoboi.CartActivity.amount;
import static com.example.ekmuthoboi.CartActivity.cartPrice;


import static com.example.ekmuthoboi.CartActivity.delType1;
import static com.example.ekmuthoboi.CartActivity.deliveryType1;

import static com.example.ekmuthoboi.CartActivity.delvId;
import static com.example.ekmuthoboi.CartActivity.grandTotal;
import static com.example.ekmuthoboi.CartActivity.grandTotalDoller;
import static com.example.ekmuthoboi.CartActivity.totalDEl;
import static com.example.ekmuthoboi.CartActivity.tv;


public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder> {

private Context context;
private CartAdapter mCartAdapter;
    private ArrayList<CartList> books;





    @NonNull
@Override
public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_cart, parent, false);
        return new CartViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        holder.cartName.setText(books.get(position).getBookName());
        holder.cartQuantity.setText(books.get(position).getQuantity());
        holder.carttotalPrice.setText((books.get(position).getBookTotalPrice()) + " Tk.");
        holder.cartdoller.setText("$"+(books.get(position).getBookDollerPrice()));



        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
    CartList item = books.get(position);
    MeterReaderDBAdapter dbAdapter = new MeterReaderDBAdapter(v.getContext());
    dbAdapter.open();


    if (books!=null && books.size()!=0){
        dbAdapter.deleteFromDB(books.get(position).getBookId());
        books.remove(item);
    notifyItemRemoved(position);
    notifyItemChanged(position,books.size());}
    dbAdapter.close();


    if( delvId.equals("2")){
        TotalwithDel=Double.parseDouble(amount)+grandTotalDoller(books);
        cartPrice.setText("$"+Double.toString(TotalwithDel));
        Total = String.valueOf(TotalwithDel);}
    else {
        TotalwithDel=Double.parseDouble(amount)+grandTotal(books);
        cartPrice.setText(Double.toString(TotalwithDel)+ " Tk.");
        Total = String.valueOf(TotalwithDel);}




    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("cartPrice", Total);
    editor.apply();


    if (tv != null && books != null)
        tv.setText(Integer.toString(books.size()));


} });


        }



@Override
public int getItemCount() {
        return books.size();
        }


public class CartViewHolder extends RecyclerView.ViewHolder {

    TextView cartName;
    TextView carttotalPrice;
    TextView cartdoller;
    TextView cartQuantity;
    ImageButton cartDelete;

    public CartViewHolder(View itemView) {
        super(itemView);

        cartName = itemView.findViewById(R.id.cart_book_name);
        carttotalPrice = itemView.findViewById(R.id.cart_book_price);
        cartQuantity = itemView.findViewById(R.id.cart_book_quantity);
        cartDelete = itemView.findViewById(R.id.cart_book_delete);
        cartdoller = itemView.findViewById(R.id.cart_book_price_doller);

    }
}

    public CartAdapter(Context context,ArrayList<CartList> books){
        this.context = context;
        this.books =  books;

    }


}
