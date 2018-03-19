package com.example.user.pnpapp.Fragment.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.pnpapp.R;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;


public class DeliveryFragment extends Fragment {


    public static final String PREFS = "preferencesfFile";
    final String LOG = "DeliveryFragment.this";

    public DeliveryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_delivery, container, false);

        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Delivery");

        SharedPreferences preferences = DeliveryFragment.this.getActivity().getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);

        final Spinner province =  view.findViewById(R.id.spinnercardtype);
        final EditText code = view.findViewById(R.id.Cvc);
        final TextView Address = view.findViewById(R.id.Cardno);


        ArrayAdapter<String> myAdaptor = new ArrayAdapter<String>(DeliveryFragment.this.getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.province));

        myAdaptor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        province.setAdapter(myAdaptor);

        Button submit = view.findViewById(R.id.btnPay);





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap postData = new HashMap();

                postData.put("province", province.getSelectedItem().toString());
                postData.put("Address", Address.getText().toString());
                postData.put("code", code.getText().toString());

                postData.put("customer", customer);


                PostResponseAsyncTask cardTask = new PostResponseAsyncTask(DeliveryFragment.this.getActivity(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(LOG, s);
                        if (s.contains("success")) {

                            Toast.makeText(DeliveryFragment.this.getActivity(), "Done", Toast.LENGTH_SHORT).show();

                            MessageFragment messageFragment= new MessageFragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction().replace(R.id.content_main1, messageFragment,
                                    messageFragment.getTag()).commit();
                        }
                    }
                });

                cardTask.execute("https://pnpapp.000webhostapp.com/delivery.php");
            }





        });

        return view;
    }
}


