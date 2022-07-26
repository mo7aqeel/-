package com.gatsby.health;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class DataUserActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private EditText dataEditText;
    private ProgressBar progressBar;
    private Spinner spinner;
    private StringBuilder locationString = new StringBuilder();
    private HashMap<String, Integer> myHash;
    private String mFullName;
    private final String[] x = {"Blood pressure", "The percentage of oxygen", "Heart beats"};
    String key;
    int c = 0;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Life sycle : ", "onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Life sycle :", "onStart()");
        getFullName();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user);

        myHash = new HashMap<>();

        gpsTracker = new GpsTracker(DataUserActivity.this);

        dataEditText = findViewById(R.id.temperature_degree);
        progressBar = findViewById(R.id.progressBar3);
        spinner = findViewById(R.id.my_spinner);

        ((Button) findViewById(R.id.Enter_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterData();
            }
        });

        if(getEmailName().equals("gatsby")){
            ((Button) findViewById(R.id.get_btn)).setVisibility(View.VISIBLE);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.data_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("i = ", String.valueOf(i));
                if (i != c){
                    key = x[c];
                    String deg = dataEditText.getText().toString();
                    if (!deg.isEmpty()) {
                        int d = Integer.parseInt(deg);
                        myHash.put(key, d);
                        dataEditText.setText("");
                    }
                }
                c = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // get location
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getLocation(){
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        locationString.append(String.valueOf(latitude)).append("\n");
        locationString.append(String.valueOf(longitude));
        sendMessage();
    }

    private void enterData() {
        progressBar.setVisibility(View.VISIBLE);

        String temp = dataEditText.getText().toString();
        myHash.put(x[c], Integer.parseInt(temp));
        dataEditText.setText("");

        Date date = new Date();


        String current_date = String.valueOf(date.getDate()) + "/" + String.valueOf(date.getMonth() + 1)
                + "/" + String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes());
        if (!temp.isEmpty()) {

            DataInputs dataInputs = new DataInputs(mFullName,
                    (myHash.containsKey("Blood pressure")?
                            String.valueOf(myHash.get("Blood pressure")) : ""),
                    (myHash.containsKey("The percentage of oxygen")?
                            String.valueOf(myHash.get("The percentage of oxygen")) : ""),
                    (myHash.containsKey("Heart beats")?
                            String.valueOf(myHash.get("Heart beats")) : ""),
                    current_date);

            String da = String.valueOf(date.getYear() + 1900) + "/" + String.valueOf(date.getMonth() + 1)
                    + "/" + String.valueOf(date.getDate()) + "/" + String.valueOf(date.getHours())
                    + ":" + String.valueOf(date.getMinutes() + ":" + String.valueOf(date.getSeconds()));

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users data")
                    .child(da).child(getEmailName());
            db.setValue(dataInputs).addOnCompleteListener(new OnCompleteListener<Void>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(DataUserActivity.this, "Data has been registered successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        int blood = 0;
                        int o2 = 0;
                        int pressure = 0;
                        if (myHash.containsKey("Blood pressure"))
                            blood = myHash.get("Blood pressure");
                        if (myHash.containsKey("The percentage of oxygen"))
                            o2 = myHash.get("The percentage of oxygen");
                        if (myHash.containsKey("Heart beats"))
                            pressure = myHash.get("Heart beats");

                        Log.i("the pressure is equal to : ", myHash.keySet().toString());
                        Log.i("the pressure is equal to : ", String.valueOf(pressure));
                        if (((blood > 120 || blood < 80) && blood != 0) ||
                                ((o2 < 80 || o2 > 140) && o2 != 0) ||
                                ((pressure <90 || pressure > 140) && pressure != 0)){
                            getLocation();

                        }
                    }//end if
                }//end onComplete
            });
        }//end if
    }//end enterData

    public void getData(View view) {
        startActivity(new Intent(DataUserActivity.this, GetDataActivity.class));
    }

    public static String getEmailName(){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        StringBuilder mail = new StringBuilder();
        for (int i=0; i<email.length(); i++){
            char c = email.charAt(i);
            if (!(c == '.' || c=='#' || c=='$' || c=='[' || c==']')){
                if (!(c == '@'))
                    mail.append(c);
                else
                    break;
            }
        }
        return mail.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_item) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("my shared pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DataUserActivity.this, MainActivity.class));
        finish();
    }

    private void sendMessage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        else
            sendSMS();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void getFullName(){
        String email = getEmailName();
        StringBuilder name = new StringBuilder();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.append(snapshot.getValue(String.class));
                mFullName = name.toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.i("Full name of user is :::::::", name.toString());
    }
    @SuppressLint("UnlocalizedSms")
    private void sendSMS(){
        @SuppressLint("DefaultLocale")
        String msg = String.format("The temporary case in location " +
                "\n%s\nUsername : %s\n", locationString.toString(), getEmailName());

        msg = msg + (myHash.containsKey("Blood pressure")? "Blood pressure :" +
                String.valueOf(myHash.get("Blood pressure")) + "\n" : "");

        msg = msg + (myHash.containsKey("Heart beats")? "Heart beats :" +
                String.valueOf(myHash.get("Heart beats")) + "\n" : "");

        msg = msg + (myHash.containsKey("The percentage of oxygen")? "The percentage of oxygen :" +
                String.valueOf(myHash.get("The percentage of oxygen")) : "");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("009647727343544", null, msg, null, null);
        Toast.makeText(DataUserActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
    }
}