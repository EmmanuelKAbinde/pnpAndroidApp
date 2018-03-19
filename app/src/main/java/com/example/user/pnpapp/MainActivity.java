package com.example.user.pnpapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText user_name,passwd;
    public static final String PREFS = "preferencesfFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_name = (EditText)findViewById(R.id.txtUsername);
        passwd = (EditText)findViewById(R.id.txtPassword);


        Button btnNext = (Button)findViewById(R.id.btnRegister);

btnNext.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent nextIntent = new Intent(getApplicationContext(),Main3Activity.class);
    startActivity(nextIntent);

    }

});

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

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }

                }).create().show();
    }

    public void onLogin(View view){

        final String str_UsernName = user_name.getText().toString();
        String str_passwd= passwd.getText().toString();


       // Toast.makeText(MainActivity.this,str_passwd +"Successful login" + str_passwd, Toast.LENGTH_LONG).show();


        HashMap postData = new HashMap();

        postData.put("user_name", str_UsernName);
        postData.put("password", str_passwd);

        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {

             //   Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();


                if (s.contains("Successfully Logged-in")) {
                    //if the username and password match the corresponding contents, then the user
                    //navigates to the appropriate page

                    SharedPreferences preferences = getSharedPreferences(PREFS, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", str_UsernName);
                    editor.commit();

                    Toast.makeText(MainActivity.this, "Successful login", Toast.LENGTH_LONG).show();
                    user_name.setText("");
                    passwd.setText("");

                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);

                } else {
                    //if the username and/or password is invalid, a dialog box appears informs them of possible errors
                    AlertDialog.Builder dialogBox = new AlertDialog.Builder(MainActivity.this);
                    dialogBox.setMessage("Incorrect username and/or password")
                            .setCancelable(false)
                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();//closes the dialog box
                                }
                            });

                    AlertDialog dialog = dialogBox.create();
                    dialog.setTitle("ERROR!");
                    dialog.show();
                }


            }
        });
        task.execute("https://pnpapp.000webhostapp.com/login.php");
    }



    }

