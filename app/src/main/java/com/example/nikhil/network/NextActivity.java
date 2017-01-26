package com.example.nikhil.network;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.nikhil.network.R.id.usn;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        TextView tv=(TextView) findViewById(R.id.message);
        tv.setText(getIntent().getStringExtra("message")+getIntent().getStringExtra("id"));
    }
}
