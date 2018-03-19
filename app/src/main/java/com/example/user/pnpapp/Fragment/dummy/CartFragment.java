package com.example.user.pnpapp.Fragment.dummy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.interfaces.ItemClickListener;
import com.example.user.pnpapp.Interface.Cart;
import com.example.user.pnpapp.Interface.Products;
import com.example.user.pnpapp.Main3Activity;
import com.example.user.pnpapp.MainActivity;
import com.example.user.pnpapp.R;
import com.kosalgeek.genasync12.AsyncResponse;

import com.example.user.pnpapp.Interface.UILConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;



import com.example.user.pnpapp.R;


public class CartFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener  {

    public static final String PREFS = "preferencesfFile";
    final static String url = "https://pnpapp.000webhostapp.com/Cart.php";
    Button btnCheckout;
    private ArrayList<Cart> CartList;
    private ListView lv;

    TextView tvTotal,tvTotal2;

    FunDapter<Cart> adapter;

    View view;

    String tvName;

    public CartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        ImageLoader.getInstance().init(UILConfig.config(CartFragment.this.getActivity()));

        SharedPreferences preferences = CartFragment.this.getActivity().getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);

        final HashMap postData = new HashMap();

        postData.put("customer", customer);


        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(CartFragment.this.getActivity(),postData, this);
        taskRead.execute(url);

        tvTotal2 = view.findViewById(R.id.textView2);
        tvTotal = view.findViewById(R.id.textView5);
        PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(CartFragment.this.getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String l) {

                tvTotal.setText("R"+l);
                tvTotal2.setText("Total:");
            }
        });
        taskInsert.execute("https://pnpapp.000webhostapp.com/CartTotal.php");






        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
          toolbar.setTitle("Cart");

        //  ProductCategoryFragment.getActivity().setSupportActionBar(toolbar);


        Button btnContinue = view.findViewById(R.id.btnContinue);
         btnCheckout= view.findViewById(R.id.btnCheckout);


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                PaymentFragment paymentFragment = new PaymentFragment();

                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main1, paymentFragment,
                        paymentFragment.getTag()).commit();
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                ProductCategoryFragment homeFragment = new ProductCategoryFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main1, homeFragment,
                        homeFragment.getTag()).commit();

            }
        });


        return view;

    }



    @Override
    public void processFinish(String s) {

if (s.contains("null"))
{
  Toast.makeText(CartFragment.this.getActivity(), "Cart is empty", Toast.LENGTH_SHORT).show();
    tvTotal2.setVisibility(view.INVISIBLE);
    tvTotal.setVisibility(view.INVISIBLE);
    btnCheckout.setVisibility(view.INVISIBLE);
}
else
{
    CartList = new JsonConverter<Cart>().toArrayList(s, Cart.class);
    BindDictionary dic = new BindDictionary();

    dic.addStringField(R.id.tvName, new StringExtractor<Cart>() {
        @Override
        public String getStringValue(Cart item, int position) {
            return item.itemName;
        }
    });

    dic.addStringField(R.id.tvDesc2, new StringExtractor<Cart>() {
        @Override
        public String getStringValue(Cart item, int position) {

            return "R"+item.price;
        }


    });

    dic.addStringField(R.id.tvDesc, new StringExtractor<Cart>() {
        @Override
        public String getStringValue(Cart item, int position) {

            return "R"+(item.price/item.quantity);
        }


    });

    dic.addStringField(R.id.qty_quantity, new StringExtractor<Cart>() {
        @Override
        public String getStringValue(Cart item, int position) {
            return "" +item.quantity;
        }


    });

    dic.addDynamicImageField(R.id.ivImage, new StringExtractor<Cart>() {
        @Override
        public String getStringValue(Cart item, int position) {
            return item.img_url;
        }
    }, new DynamicImageLoader() {
        @Override
        public void loadImage(String url, ImageView img) {
            //Set image
            ImageLoader.getInstance().displayImage(url, img);
        }

    });


    dic.addBaseField(R.id.qty_increase).onClick(new ItemClickListener() {
        @Override
        public void onClick(Object item, int position, final View view) {

            final Cart selectedItem = CartList.get(position);

            HashMap postData = new HashMap();
            int select =  selectedItem.id; ;

            postData.put("cartID", ""+ selectedItem.ProductID);

            final PostResponseAsyncTask incTask = new PostResponseAsyncTask(CartFragment.this.getActivity(),
                    postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {

                    if(s.contains("success"))
                    {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(CartFragment.this).attach(CartFragment.this).commit();

                        adapter.notifyDataSetChanged();
                    }
                }

            });
            incTask.execute("https://pnpapp.000webhostapp.com/quantityIncrease.php");
        }
    });


    dic.addBaseField(R.id.qty_decrease).onClick(new ItemClickListener() {
        @Override
        public void onClick(Object item, int position, final View view) {

            final Cart selectedItem = CartList.get(position);

            HashMap postData = new HashMap();
            int select =  selectedItem.id; ;

            postData.put("cartID", ""+ selectedItem.ProductID);

            final PostResponseAsyncTask incTask = new PostResponseAsyncTask(CartFragment.this.getActivity(),
                    postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {

                    if(s.contains("success"))
                    {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(CartFragment.this).attach(CartFragment.this).commit();

                        adapter.notifyDataSetChanged();
                    }
                }

            });
            incTask.execute("https://pnpapp.000webhostapp.com/quantityDecrease.php");
        }
    });



    dic.addBaseField(R.id.btnDelete).onClick(new ItemClickListener() {
        @Override
        public void onClick(Object item, int position, final View view) {

            final Cart selectedItem = CartList.get(position);

            HashMap postData = new HashMap();
            int select =  selectedItem.id; ;

            postData.put("cartID", ""+ selectedItem.ProductID);

            final PostResponseAsyncTask incTask = new PostResponseAsyncTask(CartFragment.this.getActivity(),
                    postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {

                    if(s.contains("success"))
                    {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(CartFragment.this).attach(CartFragment.this).commit();

                        adapter.notifyDataSetChanged();
                    }
                }

            });
            incTask.execute("https://pnpapp.000webhostapp.com/deleteCart.php");
        }
    });


    adapter = new FunDapter<>(CartFragment.this.getActivity(), CartList,
            R.layout.fragment_cart_row, dic);
    lv = view.findViewById(R.id.lvProduct);
    lv.setAdapter(adapter);

    lv.setOnItemClickListener(this);
}





    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }



}
