package com.sumgeeks.foresight;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

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

public class VerifyActivity extends AppCompatActivity {
    String ent_pin, ent_mail;
    EditText one,two,three,four;
    String s_one,s_two,s_three,s_four;
    String s_response, s_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.verify_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ent_pin = bundle.getString("pin");
            ent_mail = bundle.getString("mail");
        }
        one = findViewById(R.id.editText4);
        two = findViewById(R.id.editText6);
        three = findViewById(R.id.editText5);
        four = findViewById(R.id.editText7);

        one.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    two.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        two.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    three.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        three.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    four.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        four.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    //verify
                    s_one = one.getText().toString();
                    s_two = two.getText().toString();
                    s_three = three.getText().toString();
                    s_four = four.getText().toString();
                    String s_comb = s_one+s_two+s_three+s_four;

                    if(s_comb.equals(ent_pin)){
                        new SendPostRequest().execute();
                    }
                    else{
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(VerifyActivity.this);
                        builder3.setTitle("Error!");
                        builder3.setMessage("Verification code mismatch.");

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

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://foresight.rocsera.com/api_folder/verification.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_email", ent_mail);
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

                Toast.makeText(getApplicationContext(), "Verification successful. Please sign in to continue.",
                        Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE).edit();
                editor.putString("status", "done");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //intent.putExtra("pin",pin);
                //intent.putExtra("mail",s_mail);
                startActivity(intent);
            }
            else{
                AlertDialog.Builder builder3 = new AlertDialog.Builder(VerifyActivity.this);
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
