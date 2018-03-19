package com.example.user.pnpapp.Fragment.dummy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.user.pnpapp.Interface.Products;
import com.example.user.pnpapp.Interface.product_category;
import com.example.user.pnpapp.MainActivity;
import com.example.user.pnpapp.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;

public class ProductCategoryFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener {
    final static String url = "https://pnpapp.000webhostapp.com/product_category.php";

    private ArrayList<product_category> product_categoryList;
    private ListView lv;

    FunDapter<product_category> adapter;

    View view;

    public ProductCategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_category, container, false);

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(ProductCategoryFragment.this.getActivity(), this);
        taskRead.execute(url);



        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Product Categories");



        //  ProductCategoryFragment.getActivity().setSupportActionBar(toolbar);

        return view;
    }


    @Override
    public void processFinish(String s) {

        if (s.contains("null"))
        {
            Toast.makeText(ProductCategoryFragment.this.getActivity(), "There are no categories", Toast.LENGTH_SHORT).show();
        }

        else
        {

            product_categoryList = new JsonConverter<product_category>().toArrayList(s, product_category.class);
            BindDictionary dic = new BindDictionary();

            dic.addStringField(R.id.tvCategory, new StringExtractor<product_category>() {
                @Override
                public String getStringValue(product_category item, int position) {
                    return item.category_code;
                }
            });


            adapter = new FunDapter<>(ProductCategoryFragment.this.getActivity(), product_categoryList,
                    R.layout.fragment_category_row, dic);
            lv = view.findViewById(R.id.lvProduct);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(this);
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        product_category selectedCategory = product_categoryList.get(position);

        Fragment detailFragment = new HomePageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product_categoryList", selectedCategory);
        detailFragment.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_main1, detailFragment)
                .addToBackStack("HomeDetail").commit();


    }

}
