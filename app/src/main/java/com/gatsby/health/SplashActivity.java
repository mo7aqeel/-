package com.gatsby.health;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        ImageView imageView = findViewById(R.id.imageViewMain);
        TextView textView = findViewById(R.id.welcomText);
        ProgressBar progressBar = findViewById(R.id.progressBar5);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.img_anim);

        imageView.startAnimation(animation2);
        textView.startAnimation(animation);

        new CountDownTimer(4000, 1000){

            @Override
            public void onTick(long l) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFinish() {
                GpsTracker gpsTracker = new GpsTracker(SplashActivity.this);
                if (!gpsTracker.canGetLocation()) {
                    gpsTracker.showSettingsAlert();
                    return;
                }
                    if (!isNetworkConnected()) {
                        showAlertMessage();
                    } else {
                        SharedPreferences sharedPref = getSharedPreferences("my shared pref", MODE_PRIVATE);
                        String email = sharedPref.getString("email", "");
                        String password = sharedPref.getString("password", "");
                        if (!email.isEmpty() && !password.isEmpty()) {
                            progressBar.setVisibility(View.VISIBLE);
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(SplashActivity.this, DataUserActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Splash Activity : ", "onResume()");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetwork() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void showAlertMessage(){
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Field")
                .setMessage(getString(R.string.no_internet))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                }).show();
    }
}