package com.example.user.pnpapp.Fragment.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.user.pnpapp.Interface.Order;
import com.example.user.pnpapp.Interface.UILConfig;
import com.example.user.pnpapp.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderFragment extends Fragment   implements AsyncResponse, AdapterView.OnItemClickListener{


    public static final String PREFS = "preferencesfFile";
    final static String url = "https://pnpapp.000webhostapp.com/order.php";
    Button btnCheckout;
    private ArrayList<Order> OrderList;
    private ListView lv;

    FunDapter<Order> adapter;

    View view;

    String tvName;

    public OrderFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);
        ImageLoader.getInstance().init(UILConfig.config(OrderFragment.this.getActivity()));

        SharedPreferences preferences = OrderFragment.this.getActivity().getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);

        final HashMap postData = new HashMap();

        postData.put("customer", customer);


        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(OrderFragment.this.getActivity(),postData, this);
        taskRead.execute(url);


        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Orders");

        //  ProductCategoryFragment.getActivity().setSupportActionBar(toolbar);



        return view;

    }



    @Override
    public void processFinish(String s) {

        if (s.contains("null"))
        {
            Toast.makeText(OrderFragment.this.getActivity(), "Order is empty", Toast.LENGTH_SHORT).show();



        }
        else
        {
            OrderList = new JsonConverter<Order>().toArrayList(s, Order.class);
            BindDictionary dic = new BindDictionary();

            dic.addStringField(R.id.tvOrderID, new StringExtractor<Order>() {
                @Override
                public String getStringValue(Order item, int position) {
                    return item.orderID;
                }
            });

            dic.addStringField(R.id.tvOrderStatus, new StringExtractor<Order>() {
                @Override
                public String getStringValue(Order item, int position) {
                    return item.orderStatus;
                }


            });

            dic.addStringField(R.id.tvOrderDate, new StringExtractor<Order>() {
                @Override
                public String getStringValue(Order item, int position) {
                    return item.orderDate;
                }


            });


            adapter = new FunDapter<>(OrderFragment.this.getActivity(), OrderList,
                    R.layout.fragment_order_row, dic);
            lv = view.findViewById(R.id.lvOrder);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(this);
        }





    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Order selectedOrder= OrderList.get(position);

        Fragment detailFragment = new OrderItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderList", selectedOrder);
        detailFragment.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_main1, detailFragment)
                .addToBackStack("HomeDetail").commit();


    }

}
