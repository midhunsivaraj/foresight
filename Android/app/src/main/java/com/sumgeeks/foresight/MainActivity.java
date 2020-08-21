package com.sumgeeks.foresight;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if(!isOnline()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet needed!");
            builder.setMessage("Please connect to a working internet connection and try again.");

            builder.setPositiveButton("Close",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    MainActivity.this.finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    SharedPreferences prefs = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
                    String name = prefs.getString("name", "aaa");
                    String status = prefs.getString("status", "aaa");
                    String role = prefs.getString("role", "aaa");
                    String username = prefs.getString("username", "aaa");
                    String pin = prefs.getString("pin", "aaa");
                    Log.e("MAIN CHECK PRO: ",name+" with mail "+username+" is a "+role+" and status is "+status+
                            " (Verification code is "+pin+")");
                    if(status.equals("in")){
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        //intent.putExtra("school_code_intent",school_code);
                        startActivity(intent);
                    }
                    else if(status.equals("up")){
                        Intent intent = new Intent(getApplicationContext(), VerifyActivity.class);
                        intent.putExtra("pin",pin);
                        intent.putExtra("mail",username);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }, 4000);

        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

}
