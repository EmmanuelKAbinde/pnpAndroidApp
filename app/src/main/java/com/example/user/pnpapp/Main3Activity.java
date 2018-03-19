package com.example.user.pnpapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class Main3Activity extends AppCompatActivity {

    EditText name, surname,username,password;
    String  str_name, str_surname,str_username,str_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        name = (EditText)findViewById(R.id.txtNameReg);
        surname = (EditText)findViewById(R.id.txtSurnameReg);
        username = (EditText)findViewById(R.id.txtUsernameReg);
        password = (EditText)findViewById(R.id.txtPasswd);



/*
        Button btnNext2 = (Button)findViewById(R.id.btnLoginPage);

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(nextIntent);


            }

        });*/
    }


    public void onRegistration(View view)
    {



            HashMap postData = new HashMap();

            postData.put("name", name.getText().toString());
            postData.put("surname", surname.getText().toString());

            postData.put("user_name", username.getText().toString());
            postData.put("password", password.getText().toString());

        PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(Main3Activity.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("Successfully Registered")) {
                        Log.d("Main3Activity", s);
                        Toast.makeText(Main3Activity.this, "Insert Successful", Toast.LENGTH_LONG).show();

                       Intent intent = new Intent(Main3Activity.this, MainActivity.class);
                        startActivity(intent);
                    }


                    //If the username (or password) has under 8 characters, or the phone number length is less than 11, then an error box presents itself
                   /* else if (s.contains("failed") || uN.getText().toString().length() < 8
                            || pW.getText().toString().length() < 8 || phoneNum.getText().toString().length() != 11) {
                        AlertDialog.Builder dialogBox = new AlertDialog.Builder(Main3Activity.this);
                        dialogBox.setMessage("These reasons cause errors..."+"\n1. The username has been taken"+"\n2. The username and/or password are of the wrong length (i.e. under 8 characters)"
                                + "\n3. The phone number is of the wrong length (i.e under 11 digits)")
                                .setCancelable(false)
                                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();//closes the dialog box
                                    }
                                });
                        AlertDialog dialog = dialogBox.create();
                        dialog.setTitle("An Error was Detected!");
                        dialog.show();
                    }*/

                }
            });
            taskInsert.execute("https://pnpapp.000webhostapp.com/registration.php");


    }


    public void onUsername(View view)
    {

        AlertDialog.Builder dialogBox = new AlertDialog.Builder(Main3Activity.this);
        dialogBox.setMessage("Enter your username")
                .setCancelable(false)
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();//closes the dialog box
                    }
                });

        AlertDialog dialog = dialogBox.create();
        dialog.setTitle("Help");
        dialog.show();
    }




    public void onPassword(View view)
    {

        AlertDialog.Builder dialogBox = new AlertDialog.Builder(Main3Activity.this);
        dialogBox.setMessage("Provide a password containing 6 or more characters")
                .setCancelable(false)
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();//closes the dialog box
                    }
                });

        AlertDialog dialog = dialogBox.create();
        dialog.setTitle("Help");
        dialog.show();

    }

}
