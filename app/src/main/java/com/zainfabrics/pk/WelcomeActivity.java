package com.zainfabrics.pk;



import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class WelcomeActivity extends AppCompatActivity {
    PrefManager prefManager;
    RadioGroup radioGroupCountry;
    RadioButton radioButtonCountry;
    RadioButton radioButtonCountryPak;
    RadioButton radioButtonCountryUk;
    Button btnDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            View decorView = getWindow().getDecorView();
// Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        else {
            prefManager.setFirstTimeLaunch(false);

        }
        setContentView(R.layout.activity_welcome);
        if(prefManager.getCountryName().equals("Pakistan")){
            radioButtonCountryPak = (RadioButton) findViewById(R.id.radioButtonPak);
            radioButtonCountryPak.setChecked(true);
        }
        else{
            radioButtonCountryUk = (RadioButton) findViewById(R.id.radioButtonUK);
            radioButtonCountryUk.setChecked(true);
        }
        addListenerOnButton();
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
        finish();
    }
    public void addListenerOnButton() {
        btnDisplay = (Button) findViewById(R.id.btnNext);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                radioGroupCountry = (RadioGroup) findViewById(R.id.radioGender);
                int selectedId = radioGroupCountry.getCheckedRadioButtonId();
                radioButtonCountry = (RadioButton) findViewById(selectedId);
                prefManager.setCountryName(radioButtonCountry.getText().toString());
                startActivity(new Intent(WelcomeActivity.this,SplashActivity.class));
                finish();
            }

        });

    }

}
