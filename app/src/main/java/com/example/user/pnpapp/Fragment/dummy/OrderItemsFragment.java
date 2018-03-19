package com.example.user.pnpapp.Fragment.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.amigold.fundapter.interfaces.ItemClickListener;
import com.example.user.pnpapp.Interface.Cart;
import com.example.user.pnpapp.Interface.Products;
import com.example.user.pnpapp.Interface.Order;
import com.example.user.pnpapp.Interface.UILConfig;
import com.example.user.pnpapp.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItemsFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener {


    public static final String PREFS = "preferencesfFile";
    final static String url = "https://pnpapp.000webhostapp.com/Order_items.php";
    Button btnCheckout;
    private ArrayList<Cart> CartList;
    private ListView lv;

    TextView tvTotal,tvTotal2;

    FunDapter<Cart> adapter;

    View view;

    public OrderItemsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_items, container, false);

        final Order order = (Order) getArguments().getSerializable("orderList");

        ImageLoader.getInstance().init(UILConfig.config(OrderItemsFragment.this.getActivity()));

        SharedPreferences preferences = OrderItemsFragment.this.getActivity().getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);

        final HashMap postData = new HashMap();

        postData.put("orderNum", order.orderID);


        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(OrderItemsFragment.this.getActivity(),postData, this);
        taskRead.execute(url);


        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Order Items");

        //  ProductCategoryFragment.getActivity().setSupportActionBar(toolbar);


        Button btnBack = view.findViewById(R.id.btnBack4);




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                OrderFragment orderFragment = new OrderFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main1, orderFragment,
                        orderFragment.getTag()).commit();

            }
        });


        return view;

    }



    @Override
    public void processFinish(String s) {

        if (s.contains("null"))
        {
            Toast.makeText(OrderItemsFragment.this.getActivity(), "No order items", Toast.LENGTH_SHORT).show();

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



            adapter = new FunDapter<>(OrderItemsFragment.this.getActivity(), CartList,
                    R.layout.fragment_order_items_row, dic);
            lv = view.findViewById(R.id.lvProduct);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(this);
        }





    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }



}
