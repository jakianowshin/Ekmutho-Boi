package com.example.ekmuthoboi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CategoryList  {
    private String cat_name,cat_id;

    public CategoryList(String cat_name,String cat_id) {
        this.cat_name = cat_name;
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}
