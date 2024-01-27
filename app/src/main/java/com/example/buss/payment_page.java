package com.example.buss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class payment_page extends AppCompatActivity implements PaymentResultListener {
    Button paybtn,genbtn;
    TextView paytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        Checkout.preload(getApplicationContext());

        paytext = findViewById(R.id.paytext);
        paybtn = findViewById(R.id.paybtn);
        genbtn=findViewById(R.id.button6);

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment();

            }
        });
    }


    private void makePayment() {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_GgDpLXMDqdQktm");

        checkout.setImage(R.drawable.logo);


        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "REVA UNIVERSITY");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "4800000");//pass amount in currency subunits
            options.put("prefill.email", "www.reva.edu.in");
            options.put("prefill.contact","7259447948");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        paytext.setText("Successfull payment: "+s);
        genbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(payment_page.this,ReadData.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        paytext.setText("Failed and cause: "+s);
    }
}