package com.example.user.pnpapp.Interface;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    @SerializedName("orderID")
    public String orderID;

    @SerializedName("orderStatus")
    public String orderStatus;

    @SerializedName("orderDate")
    public String orderDate;


}
