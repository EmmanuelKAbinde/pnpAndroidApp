package com.example.user.pnpapp.Interface;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cart implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String itemName;

    @SerializedName("ProductID")
    public String ProductID;

    @SerializedName("Quantity")
    public int quantity;

    @SerializedName("Price")
    public double price;

    @SerializedName("img_url")
    public String img_url;

    @SerializedName("customer")
    public String customer;

}
