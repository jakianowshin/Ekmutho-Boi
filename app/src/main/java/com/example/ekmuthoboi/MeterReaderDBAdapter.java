package com.example.ekmuthoboi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class MeterReaderDBAdapter {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;
    public static int count=0;

    public static String dir="";

    public MeterReaderDBAdapter(Context mCtx) {
        this.mCtx = mCtx;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            /*File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+File.separator+"/ekmutho" + "/" + "BOI");
            if(!filePath.exists())
                filePath.mkdirs();
            dir=filePath.getAbsolutePath();*/
            dir="BOI";
        } else{
            dir=Environment.getExternalStorageDirectory().toString() + "/ekmutho" + "/" + "BOI";
        }
    }


    private static class DatabaseHelper
            extends SQLiteOpenHelper
    {


        public DatabaseHelper(Context context)
        {
            super(context, dir, null, 1);
        }

        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("create table order_book ( Book_id text primary key,Book_name text, Book_price text ,Book_Singleprice text,Book_dis_price text,Book_dollerprice text,Book_doller text, Book_quantity text,user_id text );");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS order_book");

            onCreate(db);
        }
    }

    public MeterReaderDBAdapter open()
            throws SQLException
    {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        this.mDbHelper.close();
    }





    public String[][] getDataToRead(){
        try
        {
            String getdata = "SELECT * FROM order_book";
            Cursor read = this.mDb.rawQuery(getdata, null);

            long p = read.getCount();
            int a = (int)p;
            String[][] result = new String[9][a];
            int i = 0;

            int a1 = read.getColumnIndex("Book_id");
            int a2 = read.getColumnIndex("Book_name");
            int a3 = read.getColumnIndex("Book_price");
            int a4 = read.getColumnIndex("Book_Singleprice");
            int a5 = read.getColumnIndex("Book_dis_price");
            int a6 = read.getColumnIndex("Book_dollerprice");
            int a7 = read.getColumnIndex("Book_doller");
            int a8= read.getColumnIndex("Book_quantity");
            int a9 = read.getColumnIndex("user_id");


            for (read.moveToFirst(); !read.isAfterLast(); read.moveToNext())
            {
                result[0][i] = read.getString(a1);
                result[1][i] = read.getString(a2);
                result[2][i] = read.getString(a3);
                result[3][i] = read.getString(a4);
                result[4][i] = read.getString(a5);
                result[5][i] = read.getString(a6);
                result[6][i] = read.getString(a7);
                result[7][i] = read.getString(a8);
                result[8][i] = read.getString(a9);

                i++;
            }
            return result;
        }
        catch (Exception e) {}
        return null;
    }


    public void deleteFromDB(String bookId)
    {
        this.mDb.execSQL("delete from order_book where Book_id="+bookId+";");
    }

    public void updateAll(String bookId,String bQuantity,String bprice,String bdoller)
    {
        this.mDb.execSQL("update order_book set Book_quantity="+bQuantity+", Book_price="+bprice+",Book_doller="+bdoller+" where Book_id="+bookId+";");
    }

    public void removeAll()
    {
        this.mDb.execSQL("delete from order_book;");
    }



    public long insertBook( String bookId,String bookName, String bprice,String bSinglePrice,String bDisPrice,String bDollerPrice ,String bdoller,String bQuantity,String userId )
    {
        ContentValues cv = new ContentValues();
        cv.put("Book_id", bookId);
        cv.put("Book_name", bookName);
        cv.put("Book_price", bprice);
        cv.put("Book_Singleprice", bSinglePrice);
        cv.put("Book_dis_price", bDisPrice);
        cv.put("Book_dollerprice", bDollerPrice);
        cv.put("Book_doller", bdoller);
        cv.put("Book_quantity", bQuantity);
        cv.put("user_id", userId);


        return this.mDb.insert("order_book", null, cv);
    }



    public long getTotalNoOfWriteTag(String bookNo)
    {
        long total = 0L;
        String sql = "select count(CardNo) as total from mbill_customer where blockNumber='" +
                bookNo + "' and CardNo!='null' limit 1";
        Cursor cr = this.mDb.rawQuery(sql, null);
        if ((cr.getCount() > 0) &&
                (cr.moveToFirst())) {
            if (cr.getLong(0) != 0L) {
                total = cr.getLong(0);
            } else {
                total = 0L;
            }
        }
        cr.close();
        return total;
    }

}
