package com.sumgeeks.foresight;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    EditText uname;
    EditText psw;
    String username,password;
    Button signin;
    TextView signup,help;
    String s_response = "true";
    String s_name, s_role;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);

        signin = findViewById(R.id.button2);
        uname = findViewById(R.id.editText);
        psw = findViewById(R.id.editText2);
        help = findViewById(R.id.textView4);
        signup = findViewById(R.id.textView3);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = uname.getText().toString();
                password = psw.getText().toString();

                if(username.equals("")){
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(LoginActivity.this);
                        builder3.setTitle("Cannot be empty!");
                        builder3.setMessage("Please enter your username to continue.");

                        builder3.setPositiveButton("Close",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder3.create();
                        dialog.show();
                }
                else if(password.equals("")){
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);
                            builder2.setTitle("Cannot be empty!");
                            builder2.setMessage("Please enter your password to continue.");

                            builder2.setPositiveButton("Close",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder2.create();
                            dialog.show();
                }
                else{
                            //api to check login creds are correct
                            new SendPostRequest().execute();

                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }



    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://foresight.rocsera.com/api_folder/login.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_email", username);
                postDataParams.put("user_password", password);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                s_response = obj.getString("error");
                s_name = obj.getString("user_name");
                s_role = obj.getString("user_profile_type");

            } catch (Throwable t) {
                Log.e("Foresight", "Could not parse malformed JSON: \"" + result + "\"");
            }

            if(!s_response.equals("true")) {

                SharedPreferences.Editor editor = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE).edit();
                editor.putString("name", s_name);
                editor.putString("status", "in");
                editor.putString("role", s_role);
                editor.apply();

                Log.e("MAIN RESP : ",result);
                Log.e("MAIN SORTED : ",s_name+" is "+s_role+" with un "+username+ " and pw "+password);

                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                //intent.putExtra("role_intent",selected_role);
                //intent.putExtra("username_intent",username);
                //intent.putExtra("password_intent",password);
                //intent.putExtra("sname_intent",sc_name);
                startActivity(intent);
            }
            else{
                AlertDialog.Builder builder3 = new AlertDialog.Builder(LoginActivity.this);
                builder3.setTitle("Error!");
                builder3.setMessage("Email or password is incorrect.");

                builder3.setPositiveButton("Close",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder3.create();
                dialog.show();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
