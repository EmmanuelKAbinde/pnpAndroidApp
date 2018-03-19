package com.example.user.pnpapp.Fragment.dummy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
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


public class PaymentFragment extends Fragment {
    public static final String PREFS = "preferencesfFile";
    final String LOG = "PaymentFragment.this";

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View  view = inflater.inflate(R.layout.fragment_payment, container, false);

        Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Payment");

        SharedPreferences preferences = PaymentFragment.this.getActivity().getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);

        final    Spinner   cardSpinner =  view.findViewById(R.id.spinnercardtype);
      final   EditText Cvc = view.findViewById(R.id.Cvc);
        final   TextView Cardno = view.findViewById(R.id.Cardno);


        ArrayAdapter<String> myAdaptor = new ArrayAdapter<String>(PaymentFragment.this.getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));

        myAdaptor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        cardSpinner.setAdapter(myAdaptor);
Button btnPay = view.findViewById(R.id.btnPay);


        final    Spinner   monthSpinner = view.findViewById(R.id.spinnerMonth);

        ArrayAdapter<String> myAdaptormonth = new ArrayAdapter<String>(PaymentFragment.this.getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.month));

        myAdaptor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        monthSpinner.setAdapter(myAdaptormonth);

        final     Spinner   yearSpinner = view.findViewById(R.id.spinnerYear);

        ArrayAdapter<String> myAdaptoryear = new ArrayAdapter<String>(PaymentFragment.this.getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.year));

        myAdaptor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        yearSpinner.setAdapter(myAdaptoryear);


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap postData = new HashMap();

                postData.put("cardType", cardSpinner.getSelectedItem().toString());
                postData.put("cardNo", Cardno.getText().toString());
                postData.put("expMonth", monthSpinner.getSelectedItem().toString());
                postData.put("expYear", yearSpinner.getSelectedItem().toString());
                postData.put("txtCvc", Cvc.getText().toString());

                postData.put("customer", customer);


                PostResponseAsyncTask cardTask = new PostResponseAsyncTask(PaymentFragment.this.getActivity(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(LOG, s);
                        if (s.contains("success")) {

                            Toast.makeText(PaymentFragment.this.getActivity(), "Payment Successfully Made", Toast.LENGTH_SHORT).show();

                            DeliveryFragment deliveryFragment= new DeliveryFragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction().replace(R.id.content_main1, deliveryFragment,
                                    deliveryFragment.getTag()).commit();
                        }
                    }
                });

                cardTask.execute("https://pnpapp.000webhostapp.com/checkout.php");
            }





        });

        return view;
    }

    }


