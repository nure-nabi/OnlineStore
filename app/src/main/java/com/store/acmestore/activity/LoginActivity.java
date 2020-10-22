package com.store.acmestore.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.store.acmestore.FragmentMainActivity;
import com.store.acmestore.MainActivity;
import com.store.acmestore.R;
import com.store.acmestore.gps.Globle;
import com.store.acmestore.gps.GpsLocationReceiver;
import com.store.acmestore.preference.UserPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_RUNTIME_PERMISSIONS = 105;
    private static final String _REGISTERID = "registerid";
    private UserPreference userPreference;
    private HashMap<String, String> userDetails;
    private Button registerBtn,loginBtn;
    EditText usernameEdt,passwordEdt;
    String username,password;
    AlertDialog.Builder builder;
    GpsLocationReceiver gpsLocationReceiver;
    Globle internetCheck;

    public static void requestLocationPerm() {
        LoginActivity activity = new LoginActivity();
        ActivityCompat.requestPermissions(activity, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION
        }, 50);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // getSupportActionBar().hide();
        usernameEdt = findViewById(R.id.edt_username);
        passwordEdt = findViewById(R.id.edt_password);
        loginBtn = findViewById(R.id.loginbtn);
        registerBtn = findViewById(R.id.registerbtn);
        checkRuntimePermissions();
        internetCheck = new Globle(this);
        //gps
        gpsLocationReceiver = new GpsLocationReceiver();
        registerReceiver(gpsLocationReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        registerReceiver(gpsLocationReceiver, new IntentFilter("oms.gps.status.check"));
        //shared preferences
        userPreference = new UserPreference(getApplicationContext());
        userDetails = userPreference.getUser();
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

//        if (userPreference.isLoggedIn()) {
//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(i);
//            finish();
//        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.loginbtn){

            username = usernameEdt.getText().toString().trim();
            password = passwordEdt.getText().toString().trim();
            builder = new AlertDialog.Builder(LoginActivity.this);
            if(username.equals("") || password.equals("")){
                builder.setTitle("Something went wrong.....");
                displayAlert("Enter a valid username and password...");
            }
            else
            {
                if(internetCheck.getConnectivityStatus(getApplicationContext())) {
                Login();
            }else {
                    Toast.makeText(LoginActivity.this, "No internet available....", Toast.LENGTH_SHORT).show();
                }
            }
        }else if(id == R.id.registerbtn){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void Login() {
        final ProgressDialog progressView = new ProgressDialog(this);
        progressView.setMessage("Logging...");
        progressView.setCancelable(false);
        progressView.show();
        String url = "http://islamictime.ramaulnews.com/loginOnline.php";
        StringRequest requestLogin = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String username ="null";
                            String phoneNo ="null";
                            String email ="null";
                            String address ="null";
                            String state ="null";
                            String city ="null";
                            String zip ="null";
                            String registerid ="null";
                            String type ="null";
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("login_failed")){
                                builder.setTitle("Login Error...");
                                displayAlert(jsonObject.getString("message"));
                                progressView.dismiss();
                             //   Toast.makeText(LoginActivity.this, "faild", Toast.LENGTH_SHORT).show();
                            }
                            else if(code.equals("login_success"))
                            {//username,phoneNo,email,address,state,city,zip,password,type
                                registerid = jsonObject.getString("registerId");
                                username = jsonObject.getString("username");
                                phoneNo = jsonObject.getString("phoneNo");
                                email = jsonObject.getString("email");
                                address = jsonObject.getString("address");
                                state = jsonObject.getString("state");
                                city = jsonObject.getString("city");
                                zip = jsonObject.getString("zip");
                                type = jsonObject.getString("type");
                                userPreference.saveRegisterId(registerid);
                                userPreference.saveUserName(username);
                                userPreference.savePhoneNo(phoneNo);
                                userPreference.saveEmail(email);
                                userPreference.saveAddress(address);
                                userPreference.saveState(state);
                                userPreference.saveCity(city);
                                userPreference.saveZip(zip);
                                userPreference.setLo(true);
                                if(type.equals("User")){
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                }else if(type.equals("Seller")) {
                                    Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
                                    startActivity(intent);
                                }
                                progressView.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressView.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressView.dismiss();
                Toast.makeText(LoginActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("username",username);
                param.put("password",password);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(requestLogin);
    }

    public void displayAlert(final String message){
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usernameEdt.setText("");
                passwordEdt.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void checkRuntimePermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int fineLocationPerm = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int coarseLocationPerm = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            int readPhoneStatePerm = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (fineLocationPerm != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (coarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (readPhoneStatePerm != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_RUNTIME_PERMISSIONS);
            }
        }
    }
}
