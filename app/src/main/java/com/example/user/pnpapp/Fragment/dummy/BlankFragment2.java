package com.example.user.pnpapp.Fragment.dummy;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.pnpapp.Interface.Products;
import com.example.user.pnpapp.Main3Activity;
import com.example.user.pnpapp.MainActivity;
import com.example.user.pnpapp.R;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.Serializable;
import java.util.HashMap;


public class BlankFragment2 extends Fragment {

    public static final String PREFS = "preferencesfFile";
    public static final String PREF = "File";


    final String LOG = "BlankFragment2.this";

    TextView tvName,tvDesc,tvPrice,tvQuant;
Button btnBack, btnCart;
    Serializable sez;
    public BlankFragment2() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
      //  ImageLoader.getInstance().init(UILConfig.config(HomeDetailFragment.this.getActivity()));
      //  sez  = getArguments().getSerializable("product_category");


        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Add to Cart");



        final Products product = (Products) getArguments().getSerializable("productList");

        btnBack = view.findViewById(R.id.button);
        btnCart = view.findViewById(R.id.btnCheckout);
        tvName = (TextView) view.findViewById(R.id.tvName);

        tvDesc = (TextView) view.findViewById(R.id.tvDesc);

        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvQuant = view.findViewById(R.id.tvQuant);


        final TextView name,description,price,quantity,imgURI;


        if(product !=null)
        {
            tvName.setText(product.name);
            tvDesc.setText(product.description);
            tvPrice.setText("" + product.price);
           }

        SharedPreferences preferences = BlankFragment2.this.getActivity().getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);

        SharedPreferences preference = BlankFragment2.this.getActivity().getSharedPreferences(PREFS, 0);
      //  final String customer = preferences.getString("username", null);

        name = tvName;
        description = tvDesc;
        price=tvPrice;
        quantity = tvQuant;

        ;



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Fragment detailFragment = new HomePageFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("product_categoryList",   getArguments().getSerializable("product_category"));
                detailFragment.setArguments(bundle);

                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main1, detailFragment)
                        .addToBackStack("HomeDetail").commit();
            }
        });



    btnCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


                final HashMap postData = new HashMap();

                postData.put("Name", name.getText().toString());
                postData.put("Description", description.getText().toString());
                postData.put("Quantity", quantity.getText().toString());
                postData.put("Price", price.getText().toString());
              postData.put("image",  product.img_url.toString());
                postData.put("customerINFO", customer);


                PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(BlankFragment2.this.getActivity(),
                        postData, new AsyncResponse() {

                    @Override
                    public void processFinish(String s) {
                        Log.d(LOG, s);
                        String aName = name.toString();

                        if (s.contains("success")) {
                            Toast.makeText(BlankFragment2.this.getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                taskInsert.execute("https://pnpapp.000webhostapp.com/addCart.php");




        }
    });


        return view;
    }


}


