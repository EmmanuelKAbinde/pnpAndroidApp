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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.pnpapp.Interface.Products;
import com.example.user.pnpapp.R;
import com.kosalgeek.genasync12.AsyncResponse;
import com.example.user.pnpapp.Interface.product_category;
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




public class HomePageFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener   {

    public static final String PREF = "File";
    final static String url = "https://pnpapp.000webhostapp.com/product2.php";

   private ArrayList<Products> productList;
    private ListView lv;

    FunDapter<Products> adapter;

    View view;

    String tvName;
 Serializable serializable;
    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
 view = inflater.inflate(R.layout.fragment_blank, container, false);
         ImageLoader.getInstance().init(UILConfig.config(HomePageFragment.this.getActivity()));

        serializable =  getArguments().getSerializable("product_categoryList");


        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Products");


        product_category  product_category = (product_category) getArguments().getSerializable("product_categoryList");

        if(product_category !=null)
        {
             tvName = product_category.category_code;

        }
        else
        {
            tvName = " ";
        }




        final TextView name;

      //  name = tvName;

        final HashMap postData = new HashMap();

    postData.put("Name", tvName);

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(HomePageFragment.this.getActivity(),postData ,this);

        taskRead.execute(url);

        Button btnBack = view.findViewById(R.id.btnBack2);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductCategoryFragment homeFragment = new ProductCategoryFragment();

                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main1, homeFragment,
                        homeFragment.getTag()).commit();
            }
        });


        //  Toast.makeText(HomePageFragment.this.getActivity(), "Name := " +tvName    , Toast.LENGTH_SHORT).show();


        return view;
    }

    @Override
    public void processFinish(String s) {

        if (s.contains("null"))
        {
            Toast.makeText(HomePageFragment.this.getActivity(), "The selected category does not have products", Toast.LENGTH_SHORT).show();
        }

        else
        {

            productList = new JsonConverter<Products>().toArrayList(s, Products.class);
            BindDictionary dic = new BindDictionary();

            dic.addStringField(R.id.tvName, new StringExtractor<Products>() {
                @Override
                public String getStringValue(Products item, int position) {
                    return item.name;
                }
            });
            dic.addStringField(R.id.tvDesc, new StringExtractor<Products>() {
                @Override
                public String getStringValue(Products item, int position) {
                    return item.description;
                }
            }).visibilityIfNull(View.GONE);

            dic.addDynamicImageField(R.id.ivImage, new StringExtractor<Products>() {
                @Override
                public String getStringValue(Products item, int position) {
                    return item.img_url;
                }
            }, new DynamicImageLoader() {
                @Override
                public void loadImage(String url, ImageView img) {
                    //Set image
                    ImageLoader.getInstance().displayImage(url, img);
                }

            });

            adapter = new FunDapter<>(HomePageFragment.this.getActivity(), productList, R.layout.fragment_home_row, dic);
            lv = view.findViewById(R.id.lvProduct);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(this);

        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Products selectedProduct = productList.get(position);


        SharedPreferences preferences = this.getActivity().getSharedPreferences(PREF, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("category", (getArguments().getSerializable("product_categoryList")).toString());
        editor.commit();

        Fragment detailFragment = new BlankFragment2();

        Bundle bundle = new Bundle();
        bundle.putSerializable("productList", selectedProduct);
        bundle.putSerializable("product_category",  getArguments().getSerializable("product_categoryList"));
        detailFragment.setArguments(bundle);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_main1, detailFragment)
                .addToBackStack("HomeDetail").commit();
    }

}
