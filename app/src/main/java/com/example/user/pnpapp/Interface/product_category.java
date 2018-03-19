package com.example.user.pnpapp.Interface;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class product_category implements Serializable {

    @SerializedName("product_category_code")
    public String category_code;

    @SerializedName("categoty_description")
    public String categoty_description;




}
