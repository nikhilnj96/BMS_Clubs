package com.example.nikhil.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.type;
import static android.provider.Telephony.Carriers.PASSWORD;

public class MainActivity extends AppCompatActivity {

    EditText usn, password;
    Button register;
    Context ctx=this;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View view){
        type="register";
        usn=(EditText) findViewById(R.id.usn);
        password=(EditText) findViewById(R.id.password);

        Background b= new Background();
        b.execute(usn.getText().toString(),password.getText().toString());
    }

    public void login(View view){
        type="login";
        usn=(EditText) findViewById(R.id.usn);
        password=(EditText) findViewById(R.id.password);

        Background b= new Background();
        b.execute(usn.getText().toString(),password.getText().toString());
    }

    class Background extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params){
            String usn=params[0];
            String password=params[1];
            int tmp;
            try{
                URL url = null;
                if(type.equals("register"))
                    url=new URL("http://www.bmsclubs.esy.es/register.php");
                else if(type.equals("login"))
                    url=new URL("http://www.bmsclubs.esy.es/login.php");

                String urlParams="usn="+usn+"&password="+password;
                String data="";

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                OutputStream os=httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is=httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+=(char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();
                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception "+e.getMessage();
            }
            catch(IOException e){
                e.printStackTrace();
                return "Exception "+e.getMessage();
            }

        }
        @Override
        protected void onPostExecute(String s){
            if(type=="register") {
                if (s.equals("")) {
                    s = "Successfully registered";
                }
                Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            }
            else{
                String fetched_id="";
                String message="";
                String err=null;
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject user_data = root.getJSONObject("user_data");
                    fetched_id=user_data.getString("id");
                    message="Log in successful. Credentials found at row ";
                } catch (JSONException e) {
                    message = "Wrong usn or password";
                    e.printStackTrace();
                    err = "Exception: "+e.getMessage();
                }

                Intent i = new Intent(ctx, NextActivity.class);
                i.putExtra("id", fetched_id);
                i.putExtra("message", message);
                i.putExtra("err", err);
                startActivity(i);
            }
        }
    }
}
