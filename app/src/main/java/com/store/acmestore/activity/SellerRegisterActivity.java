package com.store.acmestore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.store.acmestore.R;

public class SellerRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button submitBtn,cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Seller Register");

        submitBtn = findViewById(R.id.submitBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == R.id.submitBtn){
            Intent intent = new Intent(SellerRegisterActivity.this, StoreAddActivity.class);
            startActivity(intent);
        }else if(id == R.id.cancelBtn){
            Intent intent = new Intent(SellerRegisterActivity.this, AddItemActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
