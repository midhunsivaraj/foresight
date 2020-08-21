package com.sumgeeks.foresight;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner role;
    String selected_role;
    Button signup;
    TextView login;
    EditText name,mail,password;
    String s_name,s_mail,s_password,s_response,pin,s_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup_activity);

        role = findViewById(R.id.spinner);
        role.setOnItemSelectedListener(this);
        signup = findViewById(R.id.button);
        login = findViewById(R.id.textView8);
        name = findViewById(R.id.editText3);
        mail = findViewById(R.id.editText31);
        password = findViewById(R.id.editText311);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_name = name.getText().toString();
                s_mail = mail.getText().toString();
                s_password = password.getText().toString();

                if(s_name.equals("")){
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(SignupActivity.this);
                    builder3.setTitle("Cannot be empty!");
                    builder3.setMessage("Please enter your name to continue.");

                    builder3.setPositiveButton("Close",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder3.create();
                    dialog.show();
                }
                else if(s_mail.equals("")){
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(SignupActivity.this);
                    builder3.setTitle("Cannot be empty!");
                    builder3.setMessage("Please enter your mail to continue.");

                    builder3.setPositiveButton("Close",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder3.create();
                    dialog.show();
                }
                else if(s_password.equals("")){
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(SignupActivity.this);
                    builder2.setTitle("Cannot be empty!");
                    builder2.setMessage("Please enter a password to continue.");

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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#3D3DEB"));
        Log.e("SPINNER", (String.valueOf(role.getSelectedItem())).toLowerCase());
        selected_role = (String.valueOf(role.getSelectedItem())).toLowerCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://foresight.rocsera.com/api_folder/signup.php");
                pin = ""+((int)(Math.random()*9000)+1000);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_name", s_name);
                postDataParams.put("user_email", s_mail);
                postDataParams.put("user_password", s_password);
                postDataParams.put("user_profile_type", selected_role);
                postDataParams.put("user_verification_number", pin);
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
                s_error = obj.getString("msg");

            } catch (Throwable t) {
                Log.e("Foresight", "Could not parse malformed JSON: \"" + result + "\"");
            }

            if(!s_response.equals("true")) {

                Log.e("MAIN RESP : ",result);
                SharedPreferences.Editor editor = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE).edit();
                editor.putString("username", s_mail);
                editor.putString("pin", pin);
                editor.putString("status", "up");
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), VerifyActivity.class);
                intent.putExtra("pin",pin);
                intent.putExtra("mail",s_mail);
                startActivity(intent);
            }
            else{
                AlertDialog.Builder builder3 = new AlertDialog.Builder(SignupActivity.this);
                builder3.setTitle("Error!");
                builder3.setMessage(s_error);

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
