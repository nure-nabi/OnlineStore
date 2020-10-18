package com.store.acmestore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.store.acmestore.MainActivity;
import com.store.acmestore.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginbtn,registerbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // getSupportActionBar().hide();
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtn);
        loginbtn.setOnClickListener(this);
        registerbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.loginbtn){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }else if(id == R.id.registerbtn){
            Intent intent = new Intent(LoginActivity.this, SellerRegisterActivity.class);
            startActivity(intent);
        }
    }
}
