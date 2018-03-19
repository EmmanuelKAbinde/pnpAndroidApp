package com.example.user.pnpapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.pnpapp.Fragment.dummy.CartFragment;
import com.example.user.pnpapp.Fragment.dummy.DeliveryFragment;
import com.example.user.pnpapp.Fragment.dummy.HomeFragment;
import com.example.user.pnpapp.Fragment.dummy.HomePageFragment;
import com.example.user.pnpapp.Fragment.dummy.BlankFragment2;
import com.example.user.pnpapp.Fragment.dummy.MessageFragment;
import com.example.user.pnpapp.Fragment.dummy.OrderFragment;
import com.example.user.pnpapp.Fragment.dummy.ProductCategoryFragment;


public class Main2Activity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS = "preferencesfFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);


        DrawerLayout drawer =  findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        username.setText("Welcome, " + customer);
        ImageView profilePic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imgUser);
        profilePic.setImageResource(R.drawable.ic_profile_pic);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main1, homeFragment,
                homeFragment.getTag()).commit();


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Press ok to exit")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));

                       Main2Activity.super.onBackPressed();

                        Intent nextIntent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(nextIntent);

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }

                }).create().show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       int id = item.getItemId();

        if (id == R.id.home)
        {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main1, homeFragment,
                    homeFragment.getTag()).commit();
        }
        else   if (id == R.id.newOrder)
        {
            ProductCategoryFragment homeFragment = new ProductCategoryFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main1, homeFragment,
                    homeFragment.getTag()).commit();
        }


        else   if (id == R.id.baby)
        {
            CartFragment homeFragment = new CartFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main1, homeFragment,
                    homeFragment.getTag()).commit();
        }


        else   if (id == R.id.order)
        {
            OrderFragment orderFragment = new OrderFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main1, orderFragment,
                    orderFragment.getTag()).commit();
        }



        else  if (id == R.id.logout)
       {

           new AlertDialog.Builder(this)
                   .setTitle("Logout?")
                   .setMessage("Press ok to logout")
                   .setNegativeButton(android.R.string.no, null)
                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                       public void onClick(DialogInterface arg0, int arg1) {
                           setResult(RESULT_OK, new Intent().putExtra("EXIT", true));

                           Intent nextIntent = new Intent(getApplicationContext(),MainActivity.class);
                           startActivity(nextIntent);


                       }

                   }).create().show();




           //Toast.makeText(this, "Home",Toast.LENGTH_SHORT).show();
       }




  /*    if (id == R.id.about)
       {

           Intent nextIntent = new Intent(getApplicationContext(),Main4Activity.class);
           startActivity(nextIntent);
       }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
  return true;
    }
}
